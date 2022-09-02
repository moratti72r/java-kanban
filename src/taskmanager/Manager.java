package taskmanager;

import historymanager.HistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.HashMap;

public interface Manager {

    public HashMap<Integer, Task> getMapTasks() ;

    public HashMap<Integer, Subtask> getMapSubtasks();

    public HashMap<Integer, Epic> getMapEpics();
    public void createTask(Task task);

    public void createSubtask(Subtask subtask) ;

    public void createEpic(Epic epic) ;

    public void removeTasks() ;

    public void removeSubtasks();

    public void removeEpic();

    public void removeById(Integer id);

    public Task getById(Integer id) ;

    public void upDateTask(Task task) ;

    public void upDateSubtask(Subtask subtask) ;

    public void upDateEpic (Epic epic);

    public HistoryManager getHistory();

    public HashMap<Integer, Subtask> getSubtasksFromEpic(Epic epic);

}
