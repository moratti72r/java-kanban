package task;

import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;
    Epic epic;

    public Subtask(Integer id, String name, String specification, TaskStatus status, Integer epicId) {
        super(id, name, specification, status);
        this.epicId = epicId;
    }

    public void setEpic(Epic epic){
        this.epic = epic;
    }

    public Epic getEpic(){
        return epic;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                "} " + super.toString();
    }
}


