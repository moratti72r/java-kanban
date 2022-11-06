package taskserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import task.Epic;
import task.Subtask;
import task.Task;
import taskmanager.Managers;
import taskmanager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public HttpTaskServer() throws IOException {
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            TaskManager taskManager = Managers.getFileBackedTasksTaskManager();
            Gson gson = new Gson();
            String method = httpExchange.getRequestMethod();
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

            String path = httpExchange.getRequestURI().getPath();
            String rawQuery = httpExchange.getRequestURI().getRawQuery();

            String response = "";
            switch(method) {
                case "GET":
                    if (path.endsWith("/tasks")){
                        if (rawQuery!=null) {
                            if (rawQuery.startsWith("id")) {
                                Integer idTask = Integer.parseInt(rawQuery.substring(3));
                                response = gson.toJson(taskManager.getById(idTask));
                            } else {
                                System.out.println("Номер задачи введен не верно");
                            }
                        }else {
                                response = gson.toJson(taskManager.getPrioritizedTasks().addAll(taskManager.getMapEpics().values()));
                            }
                    }else if (path.endsWith("/task")){
                        response = gson.toJson(taskManager.getMapTasks());
                    }else if (path.endsWith("/subtask")){
                        response = gson.toJson(taskManager.getMapSubtasks());
                    }else if (path.endsWith("/epic")) {
                        response = gson.toJson(taskManager.getMapEpics());
                    }else if (path.endsWith("/history")) {
                        response = gson.toJson(taskManager.getHistory().getHistory());
                    }else {response = "По данному пути возникла ошибка";}
                    break;

                case "POST":
                    if (path.endsWith("/task")){
                        Task task = gson.fromJson(body, Task.class);
                        if (taskManager.getMapTasks().containsKey(task.getId())){
                            taskManager.upDateTask(task);
                            response = "Задача успешно обновлена";
                        }else {
                            taskManager.createTask(task);
                            response = "Задача успешно добавлена";
                        }
                    }else if (path.endsWith("/subtask")){
                        Subtask subtask = gson.fromJson(body,Subtask.class);
                        if (taskManager.getMapSubtasks().containsKey(subtask.getId())){
                            taskManager.upDateTask(subtask);
                            response = "Подзадача успешно обновлена";
                        }else {taskManager.createTask(subtask);
                            response = "Подзадача успешно добавлена";}
                    }else if (path.endsWith("/epic")) {
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (taskManager.getMapEpics().containsKey(epic.getId())){
                            taskManager.upDateTask(epic);
                            response = "Эпик успешно обновлен";
                        }else {taskManager.createTask(epic);
                            response = "Эпик успешно добавлен";}
                    }else {response = "По данному пути возникла ошибка";}
                    break;

                case "DELETE":
                    if (rawQuery!=null) {
                        if (rawQuery.startsWith("id")) {
                            Integer idTask = Integer.parseInt(rawQuery.substring(3));
                            taskManager.removeById(idTask);
                            response ="Задача под данным id удалена";
                        } else {
                            response ="Номер задачи введен не верно";
                        }
                    } else if (path.endsWith("tasks")) {
                        taskManager.removeTasks();
                        taskManager.removeSubtasks();
                        taskManager.removeEpic();
                        response ="Все задачи и подзадачи удалены";
                    } else if (path.endsWith("/task")) {
                        taskManager.removeTasks();
                        response ="Все задачи удалены";
                    } else if (path.endsWith("/subtask")) {
                        taskManager.removeSubtasks();
                        response ="Все подзадачи удалены";
                    } else if (path.endsWith("/epic")) {
                        taskManager.removeEpic();
                        response ="Все эпики удалены";
                    } else {
                        response = "По данному пути возникла ошибка";
                    }
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }

            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
