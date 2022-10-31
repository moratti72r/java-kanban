package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    LocalDateTime endTime;


    public Epic(Integer id, String name, String specification) {
        super();
        this.id = id;
        this.name = name;
        this.specification = specification;

        updateStatus();
        calculateTime();
    }

    public Epic(String name, String specification) {
        super();
        this.name = name;
        this.specification = specification;

        updateStatus();
        calculateTime();
    }

    public void calculateTime() {
        if (!subtasks.isEmpty()) {
            ArrayList<Subtask> subtasksList = new ArrayList<>(subtasks.values());
            startTime = subtasksList.get(0).getStartTime();
            endTime = subtasksList.get(0).getEndTime();
            for (Subtask subtask : subtasksList) {
                if (startTime.isAfter(subtask.getStartTime())) {
                    startTime = subtask.getStartTime();
                }
                if (endTime.isBefore(subtask.getEndTime())){
                    endTime = subtask.getEndTime();
                }
            }
        }else {
            this.startTime = LocalDateTime.MAX;
            this.endTime = LocalDateTime.MAX;
        }
        duration = Duration.between(startTime,endTime);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateStatus();
        calculateTime();
    }

    public void updateStatus() {
        if (!subtasks.isEmpty()) {
            ArrayList<Subtask> subtasksList = new ArrayList<>(subtasks.values());
            TaskStatus status = subtasksList.get(0).status;
            for (Subtask subtask : subtasksList) {
                if (status.equals(subtask.status)) {
                    this.status = status;
                } else {
                    this.status = TaskStatus.IS_PROGRESS;
                    break;
                }
            }
        } else {
            this.status = TaskStatus.NEW;
        }
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public LocalDateTime getEndTime(){
        return endTime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks, endTime);
    }

    @Override
    public String toString() {
        return "Epic{" + super.toString()+
                "subtasks=" + subtasks.keySet() +
                ", endTime=" + endTime +
                "} " ;
    }
}


