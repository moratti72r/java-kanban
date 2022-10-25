package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final Integer epicId;

    public Subtask(Integer id, String name, String specification, TaskStatus status, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(id, name, specification, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String specification, TaskStatus status, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(name, specification, status, startTime, duration);
        this.epicId = epicId;
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
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" + super.toString()+
                "epicId=" + epicId +
                "} " ;
    }
}


