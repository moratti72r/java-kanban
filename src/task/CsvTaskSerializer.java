package task;



import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static task.TaskType.*;

public class CsvTaskSerializer implements TaskSerializer {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");

    @Override
    public Task deserialize(String value) {
        String[] split = value.split(",");
        Integer id = Integer.parseInt(split[0]);
        TaskType taskType = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus taskStatus = TaskStatus.valueOf(split[3]);
        String specification = split[4];

        LocalDateTime startTime = split.length > 5 ? LocalDateTime.parse(split[5],formatter) : LocalDateTime.MAX;
        LocalDateTime endTime = split.length > 5 ? LocalDateTime.parse(split[6],formatter) : LocalDateTime.MAX;
        Duration duration = split.length > 5 ? Duration.between(startTime,endTime) : null;
        Integer idEpic = split.length > 7 ? Integer.parseInt(split[7]) : null;

        Task task = null;


        if (taskType == EPIC) {
            task = new Epic(id, name, specification);
        } else if (taskType == SUBTASK) {
            task = new Subtask(id, name, specification, taskStatus, startTime, duration, idEpic);
        } else if (taskType == TASK) {
            task = new Task(id, name, specification, taskStatus,startTime,duration);
        }
        return task;
    }

    @Override
    public String serialize(Task task) {
        String value = task.getId() + ","
                + task.getType() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getSpecification();

        if (task.getStartTime()!=LocalDateTime.MAX){
            value = value + "," + task.getStartTime().format(formatter) + "," + task.getEndTime().format(formatter);
        }
        if (task instanceof Subtask) {
            value = value + "," + ((Subtask) task).getEpicId();
        }
        return value;
    }

    @Override
    public String getHeadLine() {
        return "id,type,name,status,description,start,end,epic\n";
    }


}
