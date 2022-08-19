import java.util.Objects;

public class Subtask extends Task{
    Epic epic;
    String epicName;
    Subtask(Integer taskId, String taskName, String taskSpecification, String taskStatus, Epic epic){
        super(taskId, taskName, taskSpecification, taskStatus);
        epicName=epic.taskName;
        epic.addSubtask(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicName, subtask.epicName)
                && Objects.equals(taskId, subtask.taskId)
                && Objects.equals(taskName, subtask.taskName)
                && Objects.equals(taskSpecification, subtask.taskSpecification)
                && Objects.equals(taskStatus, subtask.taskStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicName);
    }

    @Override
    public String toString() {
        return "Subtask{"+
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskSpecification='" + taskSpecification + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", epicName=" + epicName +
                '}';
    }
}

