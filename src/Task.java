import java.util.Objects;

public class Task {
    protected Integer taskId;
    protected String taskName;
    protected String taskSpecification;
    protected String taskStatus;


    protected Task(Integer taskId, String taskName, String taskSpecification, String taskStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskSpecification = taskSpecification;
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId)
                && Objects.equals(taskName, task.taskName)
                && Objects.equals(taskSpecification, task.taskSpecification)
                && Objects.equals(taskStatus, task.taskStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, taskName, taskSpecification, taskStatus);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskSpecification='" + taskSpecification + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }
}