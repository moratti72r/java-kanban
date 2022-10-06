package taskmanager;

import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.HashMap;


public class InMemoryTaskManager implements Manager {
    private Integer idTask = 0;
    private final HistoryManager history = new InMemoryHistoryManager();

    private final HashMap<Integer, Task> mapTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> mapSubtasks = new HashMap<>();
    private final HashMap<Integer, Epic> mapEpics = new HashMap<>();

    @Override
    public HashMap<Integer, Task> getMapTasks() {
        return mapTasks;
    }

    @Override
    public HashMap<Integer, Subtask> getMapSubtasks() {
        return mapSubtasks;
    }

    @Override
    public HashMap<Integer, Epic> getMapEpics() {
        return mapEpics;
    }

    @Override
    public void createTask(Task task) {
        idTask++;
        task.setId(idTask);
        mapTasks.put(task.getId(), task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        idTask++;
        subtask.setId(idTask);
        if (mapEpics.containsKey(subtask.getEpicId())) {
            mapEpics.get(subtask.getEpicId()).addSubtask(subtask);
            mapEpics.get(subtask.getEpicId()).updateStatus();
            mapSubtasks.put(subtask.getId(), subtask);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        idTask++;
        epic.setId(idTask);
        mapEpics.put(epic.getId(), epic);
    }

    @Override
    public void removeTasks() {
        mapTasks.clear();
    }

    @Override
    public void removeSubtasks() {
        for (Epic epic : mapEpics.values()) {
            epic.getSubtasks().clear();
            epic.updateStatus();
        }
        mapSubtasks.clear();
    }

    @Override
    public void removeEpic() {
        mapEpics.clear();
        mapSubtasks.clear();
    }

    @Override
    public void removeById(Integer id) {
        if (mapTasks.containsKey(id)) {
            mapTasks.remove(id);
        } else if (mapSubtasks.containsKey(id)) {
            mapEpics.get(mapSubtasks.get(id).getEpicId()).getSubtasks().remove(id);
            mapEpics.get(mapSubtasks.get(id).getEpicId()).updateStatus();
            mapSubtasks.remove(id);
        } else if (mapEpics.containsKey(id)) {
            for (Integer idSubtask : mapEpics.get(id).getSubtasks().keySet()) {
                mapSubtasks.remove(idSubtask);
            }
            mapEpics.remove(id);
        }
    }

    @Override
    public Task getById(Integer id) {
        Task task = null;
        if (mapTasks.containsKey(id)) {
            task = mapTasks.get(id);
            history.add(task);
        } else if (mapSubtasks.containsKey(id)) {
            task = mapSubtasks.get(id);
            history.add(task);
        } else if (mapEpics.containsKey(id)) {
            task = mapEpics.get(id);
            history.add(task);
        }
        return task;
    }

    @Override
    public void upDateTask(Task task) {
        if (mapTasks.containsKey(task.getId())) {
            mapTasks.put(task.getId(), task);
        }
    }

    @Override
    public void upDateSubtask(Subtask subtask) {
        if (mapSubtasks.containsKey(subtask.getId())) {
            mapSubtasks.put(subtask.getId(), subtask);
            if (mapEpics.containsKey(subtask.getEpicId())) {
                mapEpics.get(subtask.getEpicId()).getSubtasks().put(subtask.getId(), subtask);
                mapEpics.get(subtask.getEpicId()).updateStatus();
            }
        }
    }

    @Override
    public void upDateEpic(Epic epic) {
        if (mapEpics.containsKey(epic.getId())) {
            mapEpics.put(epic.getId(), epic);
        }
    }

    @Override
    public HistoryManager getHistory() {
        return history;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasksFromEpic(Epic epic) {
        return epic.getSubtasks();
    }

}
