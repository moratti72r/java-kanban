package taskmanager;

import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;
import java.util.*;


public class InMemoryTaskTaskManager implements TaskManager {
    protected Integer idTaskGenerator = 0;
    private final HistoryManager history = new InMemoryHistoryManager();

    protected final HashMap<Integer, Task> mapTasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> mapSubtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> mapEpics = new HashMap<>();

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
        try {
            if (getPrioritizedTasks().stream().anyMatch((Task task1) -> task1.getStartTime().equals(task.getStartTime()))){
                throw new Exception ();
            }
            if (task.getType() == TaskType.EPIC) {
                idTaskGenerator++;
                task.setId(idTaskGenerator);
                mapEpics.put(task.getId(), (Epic) task);
            } else if (task.getType() == TaskType.SUBTASK) {
                if (mapEpics.containsKey(((Subtask) task).getEpicId())) {
                    idTaskGenerator++;
                    task.setId(idTaskGenerator);
                    mapEpics.get(((Subtask) task).getEpicId()).addSubtask((Subtask) task);
                    mapSubtasks.put(task.getId(), (Subtask) task);
                }else throw new EmptyStackException ();
            } else {
                idTaskGenerator++;
                task.setId(idTaskGenerator);
                mapTasks.put(task.getId(), task);
            }
        } catch (EmptyStackException e) {
            throw new RuntimeException("Эпик с таким id отсутствует");
        } catch (Exception e){
            throw new RuntimeException("Данное время уже занято");
        }
    }


    @Override
    public void removeTasks() {
        try {
            if (mapTasks.isEmpty()){
                throw new Exception ();
            }
            for (Integer id : mapTasks.keySet()) {
                for (Task task : history.getHistory()) {
                    if (id.equals(task.getId())) {
                        history.remove(id);
                    }
                }
            }
            mapTasks.clear();
        }catch (Exception e){
            throw new RuntimeException("Список задач уже пуст");
        }
    }

    @Override
    public void removeSubtasks() {
        try {
            if (mapTasks.isEmpty()){
                throw new Exception ();
            }
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
        }catch (Exception e){
            throw new RuntimeException("Список задач уже пуст");
        }
    }

    @Override
    public void removeEpic() {
        try {
            if (mapTasks.isEmpty()){
                throw new Exception ();
            }
        removeSubtasks();
        for (Integer id : mapEpics.keySet()) {
            for (Task task : history.getHistory()) {
                if (id.equals(task.getId())) {
                    history.remove(id);
                }
            }
        }
        mapEpics.clear();
        }catch (Exception e){
            throw new RuntimeException("Список задач уже пуст");
        }
    }

    @Override
    public void removeById(Integer id) {
        try {
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
            } else throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException("Задача под введенным id отсутствует");
        }
    }

    @Override
    public Task getById(Integer id) {
        try {
            if (mapTasks.containsKey(id)) {
                history.add(mapTasks.get(id));
                return mapTasks.get(id);
            } else if (mapSubtasks.containsKey(id)) {
                history.add(mapSubtasks.get(id));
                return mapSubtasks.get(id);
            } else if (mapEpics.containsKey(id)) {
                history.add(mapEpics.get(id));
                return mapEpics.get(id);
            } else throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException("Задача под введенным id отсутствует");
        }
    }

    @Override
    public void upDateTask(Task task) {
        try {
            if (getPrioritizedTasks().stream().anyMatch((Task task1) -> task1.getStartTime().equals(task.getStartTime()))) {
                throw new ExceptionInInitializerError();
            }
            if (task.getType()==TaskType.SUBTASK) {
                if (mapSubtasks.containsKey(task.getId())) {
                    mapSubtasks.put(task.getId(), (Subtask) task);
                    if (mapEpics.containsKey(((Subtask) task).getEpicId())) {
                        mapEpics.get(((Subtask) task).getEpicId()).addSubtask ((Subtask) task);
                    }else throw new IllegalArgumentException ();
                }else throw new Exception ();
            } else if (task.getType()==TaskType.EPIC) {
                if (mapEpics.containsKey(task.getId())) {
                    ((Epic) task).updateStatus();
                    mapEpics.put(task.getId(), (Epic) task);
                }else throw new Exception ();
            } else if (task.getType()==TaskType.TASK) {
                if (mapTasks.containsKey(task.getId())) {
                    mapTasks.put(task.getId(), task);
                }else throw new Exception ();
            } else throw new Exception ();
        }catch (ExceptionInInitializerError e){
            throw new RuntimeException("Данное время уже занято");
        }catch (IllegalArgumentException e) {
            throw new RuntimeException("Эпик с таким id отсутствует");
        }catch (Exception e) {
            throw new RuntimeException("Задача с таким id отсутствует");
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        Set<Task> treeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime).thenComparing(Task::getId));
        treeSet.addAll(mapTasks.values());
        treeSet.addAll(mapSubtasks.values());
        treeSet.addAll(mapEpics.values());
        return treeSet;
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
