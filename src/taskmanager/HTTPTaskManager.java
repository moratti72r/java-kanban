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
        if (client.load("tasks/task")!=null) {
            String jsonTasks = client.load("tasks/task");
            JsonElement jsonElement = JsonParser.parseString(jsonTasks);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement jsEl : jsonArray) {
                JsonObject jsObj = jsEl.getAsJsonObject();
                Task task = gson.fromJson(jsObj, Task.class);
                mapTasks.put(task.getId(), task);
            }
        }

        if (client.load("tasks/subtask")!=null) {
            String jsonTasks = client.load("tasks/subtask");
            JsonElement jsonElement = JsonParser.parseString(jsonTasks);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement jsEl : jsonArray) {
                JsonObject jsObj = jsEl.getAsJsonObject();
                Subtask subtask = gson.fromJson(jsObj, Subtask.class);
                mapSubtasks.put(subtask.getId(), subtask);
            }
        }

        if (client.load("tasks/epic")!=null) {
            String jsonTasks = client.load("tasks/epic");
            JsonElement jsonElement = JsonParser.parseString(jsonTasks);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement jsEl : jsonArray) {
                JsonObject jsObj = jsEl.getAsJsonObject();
                Epic epic = gson.fromJson(jsObj, Epic.class);
                mapEpics.put(epic.getId(), epic);
            }
        }

        if (client.load("tasks/history")!=null) {
            String jsonTasks = client.load("tasks/history");
            JsonElement jsonElement = JsonParser.parseString(jsonTasks);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement jsEl : jsonArray) {
                JsonObject jsObj = jsEl.getAsJsonObject();
                Task task = gson.fromJson(jsObj, Task.class);
                getHistory().getHistory().add(task);
            }
        }
    }



    @Override
    public void save(){
        if (!mapTasks.isEmpty()){
            String jsonTasks = gson.toJson(mapTasks.values());
            client.put("tasks/task",jsonTasks);
        }
        if (!mapEpics.isEmpty()){
            String jsonEpics = gson.toJson(mapEpics.values());
            client.put("tasks/epic",jsonEpics);
        }
        if (!mapSubtasks.isEmpty()){
            String jsonSubtasks = gson.toJson(mapSubtasks.values());
            client.put("tasks/subtask",jsonSubtasks);
        }
        if (!getHistory().getHistory().isEmpty()){
            String jsonHistory = gson.toJson(getHistory().getHistory());
            client.put("tasks/history",jsonHistory);
        }
    }

}
