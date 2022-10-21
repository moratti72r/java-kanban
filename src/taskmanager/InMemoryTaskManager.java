package taskmanager;

import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.util.HashMap;


public class InMemoryTaskManager implements Manager {
    protected Integer idTask = 0;
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
        if (task instanceof Epic) {
            task.setId(idTask);
            mapEpics.put(task.getId(), (Epic) task);
        }else if (task.getType() == TaskType.SUBTASK) {
            task.setId(idTask);
            if (mapEpics.containsKey(((Subtask) task).getEpicId())) {
                mapEpics.get(((Subtask) task).getEpicId()).addSubtask((Subtask) task);
                mapEpics.get(((Subtask) task).getEpicId()).updateStatus();
                mapSubtasks.put(task.getId(), (Subtask) task);
            }
        }else {
            task.setId(idTask);
            mapTasks.put(task.getId(), task);
        }
    }

    @Override
    public void removeTasks() {
        for (Integer id : mapTasks.keySet()) {
            for (Task task : history.getHistory()) {
                if (id.equals(task.getId())) {
                    history.remove(id);
                }
            }
        }
        mapTasks.clear();
    }

    @Override
    public void removeSubtasks() {
        for (Integer id : mapSubtasks.keySet()) {
            for (Task task : history.getHistory()) {
                if (id.equals(task.getId())) {
                    history.remove(id);
                }
            }
        }
        mapSubtasks.clear();
        for (Epic epic : mapEpics.values()) {
            epic.getSubtasks().clear();
            epic.updateStatus();
        }
    }

    @Override
    public void removeEpic() {
        removeSubtasks();
        for (Integer id : mapEpics.keySet()) {
            for (Task task : history.getHistory()) {
                if (id.equals(task.getId())) {
                    history.remove(id);
                }
            }
        }
        mapEpics.clear();
    }

    @Override
    public void removeById(Integer id) {
        if (mapTasks.containsKey(id)) {
            mapTasks.remove(id);
            history.remove(id);
        } else if (mapSubtasks.containsKey(id)) {
            mapEpics.get(mapSubtasks.get(id).getEpicId()).getSubtasks().remove(id);
            mapEpics.get(mapSubtasks.get(id).getEpicId()).updateStatus();
            mapSubtasks.remove(id);
            history.remove(id);
        } else if (mapEpics.containsKey(id)) {
            for (Integer idSubtask : mapEpics.get(id).getSubtasks().keySet()) {
                mapSubtasks.remove(idSubtask);
            }
            mapEpics.remove(id);
            history.remove(id);
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
        if (task instanceof Subtask) {
            if (mapSubtasks.containsKey(task.getId())) {
                mapSubtasks.put(task.getId(), (Subtask) task);
                if (mapEpics.containsKey(((Subtask) task).getEpicId())) {
                    mapEpics.get(((Subtask) task).getEpicId()).getSubtasks().put(task.getId(), (Subtask) task);
                    mapEpics.get(((Subtask) task).getEpicId()).updateStatus();
                }
            }
        } else if (task instanceof Epic) {
            if (mapEpics.containsKey(task.getId())) {
                mapEpics.put(task.getId(), (Epic) task);
            }
        } else {
            if (mapTasks.containsKey(task.getId())) {
                mapTasks.put(task.getId(), task);
            }
        }
    }

    public void setIdTask(Integer id){
        idTask=id;
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
