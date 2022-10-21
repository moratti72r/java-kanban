package task;

import taskmanager.InMemoryTaskManager;


import static task.TaskType.*;

public class CsvTaskSerializer implements TaskSerializer {

    private final InMemoryTaskManager tasksManager;
    private Integer maxId = 0;

    public CsvTaskSerializer(InMemoryTaskManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    @Override
    public Task deserialize(String value) {
        String[] split = value.split(",");
        Integer id = Integer.parseInt(split[0]);
        String type = split[1];
        String name = split[2];
        String status = split[3];
        String specification = split[4];
        Integer idEpic = split.length > 5 ? Integer.parseInt(split[5]) : null;
        Task task = null;

        TaskStatus taskStatus = TaskStatus.valueOf(status);
        TaskType taskType = TaskType.valueOf(type);


        if (taskType == EPIC) {
            task = new Epic(id, name, specification, taskStatus);
        } else if (taskType == SUBTASK) {
            task = new Subtask(id, name, specification, taskStatus, tasksManager.getMapEpics().get(idEpic));
        } else if (taskType == TASK) {
            task = new Task(id, name, specification, taskStatus);
        }
        tasksManager.setIdTask(task.getId() - 1);
        tasksManager.createTask(task);
        maxId = maxId < task.getId() ? task.id : maxId;
        tasksManager.setIdTask(maxId);
        return task;
    }

    @Override
    public String serialize(Task task) {
        String value = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getSpecification();
        if (task instanceof Subtask) {
            value = value + "," + ((Subtask) task).getEpicId();
        }
        return value;
    }

    @Override
    public String getHeadLine() {
        return "id,type,name,status,description,epic\n";
    }

}
