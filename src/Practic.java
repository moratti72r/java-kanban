import com.google.gson.*;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;
import taskmanager.InMemoryTaskTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Practic {
    public static void main(String[] args) {

        InMemoryTaskTaskManager tm = new InMemoryTaskTaskManager();

        Task task = new Task(1, "Имя", "спецификация", TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10), Duration.ofMinutes(10));

        Task task4 = new Task(4, "Имя", "спецификация", TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 12, 10, 10), Duration.ofMinutes(10));

        Task task5 = new Task(5, "Имя", "спецификация", TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 13, 10, 10), Duration.ofMinutes(10));

        Gson gson = new Gson();

        String taskJson = gson.toJson(task);
        System.out.println(taskJson);

        Subtask subtask = new Subtask(3, "Имя", "спецификация", TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 11, 10, 10), Duration.ofMinutes(10),2);
        String subtaskJson = gson.toJson(subtask);
        System.out.println(subtaskJson);

        Epic epic = new Epic(2, "Имя", "спецификация");
        String epicJson = gson.toJson(epic);
        System.out.println(epicJson);

        tm.createTask(task);
        tm.createTask(epic);
        tm.createTask(subtask);
        tm.createTask(task4);
        tm.createTask(task5);

        String tasks = gson.toJson(tm.getMapTasks().values());

        JsonElement jsonElement = JsonParser.parseString(tasks);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement jsEl : jsonArray) {
            System.out.println(jsEl);
//            JsonObject jsObj = jsEl.getAsJsonObject();

        }
//        System.out.println(gson.toJson(tm.getMapTasks().values()));
//        System.out.println(gson.toJson(tm.getMapEpics().values()));
//        System.out.println(gson.toJson(tm.getPrioritizedTasks()));
    }
}
