package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private Integer idTask = 0;

    private HashMap<Integer, Task> mapTasks = new HashMap<>();
    private HashMap<Integer, Subtask> mapSubtasks = new HashMap<>();
    private HashMap<Integer, Epic> mapEpics = new HashMap<>();

    public HashMap<Integer, Task> getMapTasks() {
        return mapTasks;
    }

    public HashMap<Integer, Subtask> getMapSubtasks() {
        return mapSubtasks;
    }

    public HashMap<Integer, Epic> getMapEpics() {
        return mapEpics;
    }

    public void createTask(Task task) {
        idTask++;
        task.setId(idTask);
        mapTasks.put(task.getId(), task);
    }

    public void createSubtask(Subtask subtask) {
        idTask++;
        subtask.setId(idTask);
        subtask.getEpic().addSubtask(subtask);
        mapSubtasks.put(subtask.getId(), subtask);
    }

    public void createEpic(Epic epic) {
        idTask++;
        epic.setId(idTask);
        mapEpics.put(epic.getId(), epic);
    }


    public void removeTasks() {
        mapTasks.clear();
    }

    public void removeSubtasks(){
        for (Epic epic: mapEpics.values()) {
            epic.getSubtasks().clear();
            epic.updateStatus();
        }
        mapSubtasks.clear();
    }

    public void removeEpic(){
        mapEpics.clear();
        mapSubtasks.clear();
    }

    public void removeById(Integer id){
        if (mapTasks.containsKey(id)){
            mapTasks.remove(id);
        }else if (mapSubtasks.containsKey(id)){
            for (Epic epic: mapEpics.values()) {
                epic.getSubtasks().remove(id);
            }
            mapSubtasks.remove(id);
        }else if (mapEpics.containsKey(id)){
            mapEpics.remove(id);
        }
    }

    public Task getById(Integer id) {
        Task task = null;
        if (mapTasks.containsKey(id)){
            task = mapTasks.get(id);
        }else if (mapSubtasks.containsKey(id)){
            task = mapSubtasks.get(id);
        }else if (mapEpics.containsKey(id)){
            task = mapEpics.get(id);
        }
        return task;
    }


    public void upDateTask(Task task) {
        if (mapTasks.containsKey(task.getId())){
            mapTasks.put(task.getId(),task);
        }
    }

    public void upDateSubtask(Subtask subtask) {
        if (mapSubtasks.containsKey(subtask.getId())){

            mapSubtasks.put(subtask.getId(),subtask);
            subtask.getEpic().updateStatus();
        }
    }

    public void upDateEpic (Epic epic){
        if (mapEpics.containsKey(epic.getId())){
            epic.getSubtasks().clear();
            for (Subtask subtask: mapSubtasks.values()) {
                if (subtask.getEpic().equals(epic)){
                    mapSubtasks.remove(subtask.getId());
                }
            }
            mapEpics.put(epic.getId(), epic);
        }
    }

    public HashMap<Integer, Subtask> getSubtasksFromEpic(Epic epic){
        return epic.getSubtasks();
    }

}
