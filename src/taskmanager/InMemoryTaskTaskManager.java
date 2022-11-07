package taskmanager;

import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;


public class InMemoryTaskTaskManager implements TaskManager {
    protected Integer idTaskGenerator = 0;
    private final HistoryManager history = new InMemoryHistoryManager();

    protected final HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> mapSubtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> mapEpics = new HashMap<>();
    protected final TreeSet<Task> allTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
    public Integer createTask(Task task) {
        if (allTasks.stream().anyMatch((Task task1) -> intersectionOfTimes(task, task1))) {
            throw new RuntimeException("Данное время уже занято");
        }
        idTaskGenerator++;
        if (task.getType() == TaskType.EPIC) {
            task.setId(idTaskGenerator);
            mapEpics.put(task.getId(), (Epic) task);

        } else if (task.getType() == TaskType.SUBTASK) {
            if (mapEpics.containsKey(((Subtask) task).getEpicId())) {
                task.setId(idTaskGenerator);
                mapEpics.get(((Subtask) task).getEpicId()).addSubtask((Subtask) task);
                mapSubtasks.put(task.getId(), (Subtask) task);
                allTasks.add(task);
            } else throw new RuntimeException("Эпик с таким id отсутствует");
        } else {
            task.setId(idTaskGenerator);
            mapTasks.put(task.getId(), task);
            allTasks.add(task);
        }
        return idTaskGenerator;
    }

    private boolean intersectionOfTimes(Task task1, Task task2) {
        if (task1.getStartTime()!=null) {
            if (task1.getStartTime().isBefore(task2.getStartTime())) {
                return (Duration.between(task1.getStartTime(), task2.getStartTime()).toMinutes()) <= task1.getDuration().toMinutes();
            } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
                return (Duration.between(task2.getStartTime(), task1.getStartTime()).toMinutes()) <= task2.getDuration().toMinutes();
            } else return task1.getStartTime().equals(task2.getStartTime());
        }else return false;
    }

    @Override
    public void removeTasks() {
        for (Integer id : mapTasks.keySet()) {
            for (Task task : history.getHistory()) {
                if (id.equals(task.getId())) {
                    history.remove(id);
                }
            }
            allTasks.removeIf(task -> id.equals(task.getId()));
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
            }allTasks.removeIf(task -> id.equals(task.getId()));
        }
        mapSubtasks.clear();
        for (Epic epic : mapEpics.values()) {
            epic.getSubtasks().clear();
            epic.updateStatus();
            epic.calculateTime();
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
        } else throw new RuntimeException("Задача под введенным id отсутствует");
    }

    @Override
    public Task getById(Integer id) {
        if (mapTasks.containsKey(id)) {
            history.add(mapTasks.get(id));
            return mapTasks.get(id);
        } else if (mapSubtasks.containsKey(id)) {
            history.add(mapSubtasks.get(id));
            return mapSubtasks.get(id);
        } else if (mapEpics.containsKey(id)) {
            history.add(mapEpics.get(id));
            return mapEpics.get(id);
        } else throw new RuntimeException("Задача под введенным id отсутствует");
    }

    @Override
    public void upDateTask(Task task) {
        if (allTasks.stream()
                .filter((Task task1) -> !task1.getId().equals(task.getId()))
                .anyMatch((Task task1) -> intersectionOfTimes(task, task1))) {
            throw new RuntimeException("Данное время уже занято");
        }
        if (task.getType() == TaskType.SUBTASK && mapSubtasks.containsKey(task.getId())) {
            mapSubtasks.put(task.getId(), (Subtask) task);
            if (mapEpics.containsKey(((Subtask) task).getEpicId())) {
                mapEpics.get(((Subtask) task).getEpicId()).addSubtask((Subtask) task);
            } else throw new RuntimeException("Эпик с таким id отсутствует");
        } else if (task.getType() == TaskType.EPIC && mapEpics.containsKey(task.getId())) {
            allTasks.stream()
                    .filter((Task task1) -> ((Subtask) task1).getEpicId().equals(task.getId()))
                    .map((Task task1) -> (Subtask) task1)
                    .forEach(((Epic) task)::addSubtask);
            mapEpics.put(task.getId(), (Epic) task);
        } else if (task.getType() == TaskType.TASK && mapTasks.containsKey(task.getId())) {
            mapTasks.put(task.getId(), task);
        } else {
            throw new RuntimeException("Задача с таким id отсутствует");
        }

        allTasks.removeIf(delTask -> delTask.getId().equals(task.getId()));
        allTasks.add(task);

    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return allTasks;
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
