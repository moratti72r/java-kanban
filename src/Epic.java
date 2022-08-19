import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Subtask> subtasks = new ArrayList<>();

    Epic(Integer taskId, String taskName, String taskSpecification, String taskStatus) {
        super(taskId, taskName, taskSpecification, taskStatus);
    }

    public void addSubtask(Subtask subtask) {
        if (subtasks.isEmpty()) {
            this.taskStatus = subtask.taskStatus;

        } else {
            for (Subtask values : subtasks) {
                if (subtask.taskStatus.equals(values.taskStatus)) {
                    this.taskStatus = subtask.taskStatus;
                } else {
                    this.taskStatus = "IN_PROGRESS";
                    break;
                }
            }
        }
        subtasks.add(subtask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks)
                && Objects.equals(taskId, epic.taskId)
                && Objects.equals(taskName, epic.taskName)
                && Objects.equals(taskSpecification, epic.taskSpecification)
                && Objects.equals(taskStatus, epic.taskStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskSpecification='" + taskSpecification + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", subtasks='" + subtasks + '\'' +
                '}';
    }
}

