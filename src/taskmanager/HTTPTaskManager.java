package taskmanager;

import com.google.gson.Gson;
import task.Epic;
import task.Subtask;
import task.Task;
import taskclient.KVTaskClient;

import java.io.IOException;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksTaskManager {
//Здравствуйте, Антон. В работе много недочетов. Работу отправляю т.к. не могу сам разобраться и нужны конкретные замечания.
    //не могу разобраться как в строке запроса находить определенную таску по id для дальнейшей работы с ней.
    //Спасибо!!!
    private final static String textFile = "src\\resources\\file1.csv";
    KVTaskClient client;
    Gson gson = new Gson();

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        super(textFile);
        client = new KVTaskClient(url);
        if (!getPrioritizedTasks().isEmpty() || !getMapEpics().isEmpty()){
            placeOnServer();
        }
    }

    public void separateFromJson(){
        String jsonTasks = client.load("tasks/task");
        if (!jsonTasks.isEmpty()) {
            Task[] tasks = gson.fromJson(jsonTasks, Task[].class);
            List<Task> list = List.of(tasks);
            for (Task task : list) {
                super.createTask(task);
            }
        }

        String jsonEpics = client.load("tasks/epic");
        if (!jsonEpics.isEmpty()) {
            Epic[] epics = gson.fromJson(jsonEpics, Epic[].class);
            List<Epic> list = List.of(epics);
            for (Epic epic : list) {
                super.createTask(epic);
            }
        }

        String jsonSubtasks = client.load("tasks/subtasks");
        if (!jsonSubtasks.isEmpty()) {
            Subtask[] subtasks = gson.fromJson(jsonSubtasks, Subtask[].class);
            List<Subtask> list = List.of(subtasks);
            for (Subtask subtask : list) {
                super.createTask(subtask);
            }
        }

        String jsonHistory = client.load("tasks/history");
        if (!jsonHistory.isEmpty()) {
            Task[] tasks = gson.fromJson(jsonSubtasks, Task[].class);
            List<Task> list = List.of(tasks);
            for (Task task : list) {
                super.getById(task.getId());
            }
        }
    }

    public void placeOnServer(){
        if (!mapTasks.isEmpty()){
            String jsonTasks = gson.toJson(mapTasks);
            client.put("tasks/task",jsonTasks);
        }
        if (!mapEpics.isEmpty()){
            String jsonEpics = gson.toJson(mapEpics);
            client.put("tasks/subtask",jsonEpics);
        }
        if (!mapSubtasks.isEmpty()){
            String jsonSubtasks = gson.toJson(mapSubtasks);
            client.put("tasks/subtask",jsonSubtasks);
        }
        if (!getHistory().getHistory().isEmpty()){
            String jsonHistory = gson.toJson(getHistory().getHistory());
            client.put("tasks/subtask",jsonHistory);
        }
    }

    @Override
    public void save(){
        super.save();
        placeOnServer();
    }

    @Override
    public Integer createTask(Task task) {
        super.createTask(task);
        placeOnServer();
        return task.getId();
    }

    @Override
    public Task getById(Integer id) {
        Task task = super.getById(id);
        placeOnServer();
        return task;
    }

    @Override
    public void removeById(Integer id) {
        super.removeById(id);
        placeOnServer();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        placeOnServer();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        placeOnServer();
    }

    @Override
    public void removeEpic() {
        super.removeEpic();
        placeOnServer();
    }

    @Override
    public void upDateTask(Task task) {
        super.upDateTask(task);
        placeOnServer();
    }

}
