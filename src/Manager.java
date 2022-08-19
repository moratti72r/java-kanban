import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    Integer idTask = 0;
    public String NEW = "NEW";
    public String IN_PROGRESS = "IN_PROGRESS";
    public String DONE = "DONE";
    HashMap<Integer, Task> mapTasks = new HashMap<>();
    HashMap<Integer, Task> mapSubtasks = new HashMap<>();
    HashMap<Integer, Task> mapEpics = new HashMap<>();

    void createTask(Task task) {
        idTask++;
        task.taskId = idTask;
        if (task.getClass() == Subtask.class) {
            mapSubtasks.put(task.taskId, task);
        } else if (task.getClass() == Epic.class) {
            mapEpics.put(task.taskId, task);
        } else {
            mapTasks.put(task.taskId, task);
        }
    }

    ArrayList<Task> getTasks(HashMap<Integer, Task> tasks) {
        ArrayList<Task> listTasks = new ArrayList<>();
        for (Task task :
                tasks.values()) {
            listTasks.add(task);
        }
        return listTasks;
    }

    void removeTasks(HashMap<Integer, Task> tasks) {
        tasks.clear();
    }

    void removeById(Integer id){
        for (Integer idTask :
                mapTasks.keySet()) {
            if (idTask == id) {
                mapTasks.remove(id);
            }
        }
        for (Integer idTask :
                mapSubtasks.keySet()) {
            if (idTask == id) {
                mapSubtasks.remove(id);
            }
        }
        for (Integer idTask :
                mapEpics.keySet()) {
            if (idTask == id) {
                mapEpics.remove(id);
            }
        }
    }

    Task getById(Integer id) {
        Task task = null;
        for (Integer idTask :
                mapTasks.keySet()) {
            if (idTask == id) {
                task = mapTasks.get(id);
            }
        }
        for (Integer idTask :
                mapSubtasks.keySet()) {
            if (idTask == id) {
                task = mapSubtasks.get(id);
            }
        }
        for (Integer idTask :
                mapEpics.keySet()) {
            if (idTask == id) {
                task = mapEpics.get(id);
            }
        }
        return task;
    }


    void upDateTask(Task task) {
        for (Task values :
                mapTasks.values()) {
            if (values.taskId.equals(task.taskId)) {
                mapTasks.put(task.taskId, task);
            }
        }
        for (Task values :
                mapTasks.values()) {
            if (values.taskId.equals(task.taskId)) {
                mapSubtasks.put(task.taskId, task);
            }
        }
        for (Task values :
                mapTasks.values()) {
            if (values.taskId.equals(task.taskId)) {
                mapEpics.put(task.taskId, task);
            }
        }
    }


}
