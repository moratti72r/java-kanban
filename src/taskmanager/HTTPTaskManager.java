package taskmanager;

import com.google.gson.*;
import task.Epic;
import task.Subtask;
import task.Task;
import taskclient.KVTaskClient;

import java.io.IOException;

public class HTTPTaskManager extends FileBackedTasksTaskManager {
    KVTaskClient client;
    Gson gson = new Gson();

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        super();
        client = new KVTaskClient(url);
        createTasksFromJson(client.load("tasks/task"));
        createTasksFromJson(client.load("tasks/subtask"));
        createTasksFromJson(client.load("tasks/epic"));
        createTasksFromJson(client.load("tasks/history"));
    }


    @Override
    public void save() {
        if (!mapTasks.isEmpty()) {
            String jsonTasks = gson.toJson(mapTasks.values());
            client.put("tasks/task", jsonTasks);
        }
        if (!mapEpics.isEmpty()) {
            String jsonEpics = gson.toJson(mapEpics.values());
            client.put("tasks/epic", jsonEpics);
        }
        if (!mapSubtasks.isEmpty()) {
            String jsonSubtasks = gson.toJson(mapSubtasks.values());
            client.put("tasks/subtask", jsonSubtasks);
        }
        if (!getHistory().getHistory().isEmpty()) {
            String jsonHistory = gson.toJson(getHistory().getHistory());
            client.put("tasks/history", jsonHistory);
        }
    }

    private void createTasksFromJson(String jsonTasks) {
        if (!jsonTasks.isEmpty()) {
            Integer idMax = 0;
            JsonElement jsonElement = JsonParser.parseString(jsonTasks);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement jsEl : jsonArray) {
                JsonObject jsObj = jsEl.getAsJsonObject();
                if (jsObj.has("subtasks")) {
                    Epic epic = gson.fromJson(jsObj, Epic.class);
                    mapEpics.put(epic.getId(), epic);
                    idMax = idMax < epic.getId() ? epic.getId() : idMax;
                } else if (jsObj.has("epicId")) {
                    Subtask subtask = gson.fromJson(jsObj, Subtask.class);
                    mapSubtasks.put(subtask.getId(), subtask);
                    idMax = idMax < subtask.getId() ? subtask.getId() : idMax;
                } else {
                    Task task = gson.fromJson(jsObj, Task.class);
                    mapTasks.put(task.getId(), task);
                    idMax = idMax < task.getId() ? task.getId() : idMax;
                }
            }
            setIdTaskGenerator(idMax);
        }
    }


}
