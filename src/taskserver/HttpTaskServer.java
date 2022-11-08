package taskserver;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
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
    Gson gson = new Gson();
    TaskManager taskManager = Managers.getFileBackedTasksTaskManager();
    HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this::handleTasks);
        httpServer.createContext("/tasks/task", this::handleTask);
        httpServer.createContext("/tasks/subtask", this::handleSubtask);
        httpServer.createContext("/tasks/epic", this::handleEpic);
        httpServer.createContext("/tasks/history", this::handleHistory);
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void handleTask(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            String rawQuery = httpExchange.getRequestURI().getRawQuery();
            String response = "";

            switch (method) {
                case ("GET"):
                    if (rawQuery != null) {
                        if (rawQuery.startsWith("id")) {
                            Integer idTask = Integer.parseInt(rawQuery.substring(3));
                            if (taskManager.getMapTasks().containsKey(idTask)) {
                                response = gson.toJson(taskManager.getById(idTask));
                            } else {
                                System.out.println("Задача под данным id отсутствует");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else {
                            System.out.println("Ввдена не корректная строка запроса");
                            httpExchange.sendResponseHeaders(403, 0);
                            return;
                        }
                    } else {
                        response = gson.toJson(taskManager.getMapTasks().values());
                    }
                    break;

                case ("POST"):
                    if (!body.isEmpty()) {
                        JsonElement jsonElement = JsonParser.parseString(body);
                        if (jsonElement.isJsonObject()) {
                            boolean result = isCreateTaskFromJson(body, jsonElement, taskManager);
                            if (!result) {
                                System.out.println("Отправленные данные не правильного формата");
                                httpExchange.sendResponseHeaders(400, 0);
                                return;
                            } else {
                                System.out.println("Задача успешно создана");
                            }
                        } else {
                            System.out.println("Отправленные данные не правильного формата");
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                    } else {
                        System.out.println("Тело запроса пустой");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    break;

                case ("DELETE"):
                    if (rawQuery != null) {
                        if (rawQuery.startsWith("id")) {
                            Integer idTask = Integer.parseInt(rawQuery.substring(3));
                            if (taskManager.getMapTasks().containsKey(idTask)) {
                                taskManager.removeById(idTask);
                                System.out.println("Задача под данным id удалена");
                            } else {
                                System.out.println("Задача под данным id отсутствует");
                                httpExchange.sendResponseHeaders(403, 0);
                                return;
                            }
                        } else {
                            System.out.println("Ввдена не корректная строка запроса");
                            httpExchange.sendResponseHeaders(403, 0);
                            return;
                        }
                    } else {
                        taskManager.removeTasks();
                        System.out.println("Все задачи удалены");
                    }
                    break;

                default:
                    System.out.println("Получен не верный запрос");
                    httpExchange.sendResponseHeaders(405, 0);
            }

            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } finally {
            httpExchange.close();
        }
    }

    public void handleSubtask(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath().substring("/tasks/".length());
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            String rawQuery = httpExchange.getRequestURI().getRawQuery();
            String response = "";

            switch (method) {
                case ("GET"):
                    if (rawQuery != null) {
                        Integer idTask;
                        if (rawQuery.startsWith("id")) {
                            idTask = Integer.parseInt(rawQuery.substring(3));
                        } else {
                            System.out.println("Введена не корректная строка запроса");
                            httpExchange.sendResponseHeaders(403, 0);
                            return;
                        }

                        if (path.endsWith("/epic")) {
                            if (taskManager.getMapEpics().containsKey(idTask)) {
                                response = gson.toJson(taskManager.getMapEpics().get(idTask).getSubtasks().values());
                            } else {
                                System.out.println("Эпик под данным id отсутствует");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.isEmpty()) {
                            if (taskManager.getMapSubtasks().containsKey(idTask)) {
                                response = gson.toJson(taskManager.getById(idTask));
                            } else {
                                System.out.println("Задача под данным id отсутствует");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else {
                            System.out.println("Путь указан не верно");
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                    } else {
                        response = gson.toJson(taskManager.getMapSubtasks().values());
                    }
                    break;

                case "POST":
                    if (!body.isEmpty()) {
                        JsonElement jsonElement = JsonParser.parseString(body);
                        if (jsonElement.isJsonObject()) {
                            boolean result = isCreateTaskFromJson(body, jsonElement, taskManager);
                            if (!result) {
                                System.out.println("Отправленные данные не правильного формата");
                                httpExchange.sendResponseHeaders(400, 0);
                                return;
                            } else {
                                System.out.println("Задача успешно создана");
                            }
                        } else {
                            System.out.println("Отправленные данные не правильного формата");
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                    } else {
                        System.out.println("Тело запроса пустой");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    break;

                case ("DELETE"):
                    if (rawQuery != null) {
                        if (rawQuery.startsWith("id")) {
                            Integer idTask = Integer.parseInt(rawQuery.substring(3));
                            if (taskManager.getMapSubtasks().containsKey(idTask)) {
                                taskManager.removeById(idTask);
                                System.out.println("Задача под данным id удалена");
                            } else {
                                System.out.println("Задача под данным id отсутствует");
                                httpExchange.sendResponseHeaders(403, 0);
                                return;
                            }
                        } else {
                            System.out.println("Ввдена не корректная строка запроса");
                            httpExchange.sendResponseHeaders(403, 0);
                            return;
                        }
                    } else {
                        taskManager.removeSubtasks();
                        System.out.println("Все подзадачи удалены");
                    }
                    break;

                default:
                    System.out.println("Получен не верный запрос");
                    httpExchange.sendResponseHeaders(405, 0);
            }
            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } finally {
            httpExchange.close();
        }
    }

    public void handleEpic(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            String rawQuery = httpExchange.getRequestURI().getRawQuery();
            String response = "";

            switch (method) {
                case ("GET"):
                    if (rawQuery != null) {
                        Integer idTask;
                        if (rawQuery.startsWith("id")) {
                            idTask = Integer.parseInt(rawQuery.substring(3));
                        } else {
                            System.out.println("Ввдена не корректная строка запроса");
                            httpExchange.sendResponseHeaders(403, 0);
                            return;
                        }

                        if (taskManager.getMapEpics().containsKey(idTask)) {
                            response = gson.toJson(taskManager.getById(idTask));
                        } else {
                            System.out.println("Эпик под данным id отсутствует");
                            httpExchange.sendResponseHeaders(404, 0);
                        }

                    } else {
                        response = gson.toJson(taskManager.getMapEpics().values());
                    }
                    break;

                case "POST":
                    if (!body.isEmpty()) {
                        JsonElement jsonElement = JsonParser.parseString(body);
                        if (jsonElement.isJsonObject()) {
                            boolean result = isCreateTaskFromJson(body, jsonElement, taskManager);
                            if (!result) {
                                System.out.println("Отправленные данные не правильного формата");
                                httpExchange.sendResponseHeaders(400, 0);
                                return;
                            } else {
                                System.out.println("Задача успешно создана");
                            }
                        } else {
                            System.out.println("Отправленные данные не правильного формата");
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                    } else {
                        System.out.println("Тело запроса пустой");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    break;

                case ("DELETE"):
                    if (rawQuery != null) {
                        if (rawQuery.startsWith("id")) {
                            Integer idTask = Integer.parseInt(rawQuery.substring(3));
                            if (taskManager.getMapEpics().containsKey(idTask)) {
                                taskManager.removeById(idTask);
                                System.out.println("Эпик под данным id удалена");
                            } else {
                                System.out.println("Задача под данным id отсутствует");
                                httpExchange.sendResponseHeaders(403, 0);
                                return;
                            }
                        } else {
                            System.out.println("Ввдена не корректная строка запроса");
                            httpExchange.sendResponseHeaders(403, 0);
                            return;
                        }
                    } else {
                        taskManager.removeEpic();
                        System.out.println("Все эпики удалены");
                    }
                    break;

                default:
                    System.out.println("Получен не верный запрос");
                    httpExchange.sendResponseHeaders(405, 0);
            }

            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } finally {
            httpExchange.close();
        }
    }

    public void handleTasks(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String response = "";

            switch (method) {
                case ("GET"):
                    response = gson.toJson(taskManager.getPrioritizedTasks());
                    break;

                case ("DELETE"):
                    taskManager.removeSubtasks();
                    taskManager.removeEpic();
                    taskManager.removeTasks();
                    taskManager.getPrioritizedTasks().clear();
                    System.out.println("Все задачи удалены");
                    break;
                default:
                    System.out.println("Получен не верный запрос");
                    httpExchange.sendResponseHeaders(405, 0);
            }

            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } finally {
            httpExchange.close();
        }
    }

    public void handleHistory(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String response;

            if ("GET".equals(method)) {
                response = gson.toJson(taskManager.getHistory().getHistory());
            } else {
                System.out.println("Получен не верный запрос");
                httpExchange.sendResponseHeaders(405, 0);
                return;
            }

            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } finally {
            httpExchange.close();
        }
    }


    public Boolean isCreateTaskFromJson(String body, JsonElement jsonElement, TaskManager taskManager) {
        boolean result = true;
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("subtasks")) {
                Epic epic = gson.fromJson(body, Epic.class);
                if (taskManager.getMapEpics().containsKey(epic.getId())) {
                    taskManager.upDateTask(epic);
                } else {
                    taskManager.createTask(epic);
                }
            } else if (jsonObject.has("epicId")) {
                Subtask subtask = gson.fromJson(body, Subtask.class);
                if (taskManager.getMapSubtasks().containsKey(subtask.getId())) {
                    taskManager.upDateTask(subtask);
                } else {
                    taskManager.createTask(subtask);
                }
            } else {
                Task task = gson.fromJson(body, Task.class);
                if (taskManager.getMapTasks().containsKey(task.getId())) {
                    taskManager.upDateTask(task);
                } else {
                    taskManager.createTask(task);
                }
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public void stop() {
        httpServer.stop(0);
    }
}

