package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String specification;
    protected TaskStatus status;
    protected LocalDateTime startTime;
    protected Duration duration;

    protected Task() {
    }

    public Task(Integer id, String name, String specification, TaskStatus status, LocalDateTime startTime, Duration duration ) {
        this.id = id;
        this.name = name;
        this.specification = specification;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String specification, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.specification = specification;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public String getName() {
        return name;
    }

    public String getSpecification() {
        return specification;
    }

    public LocalDateTime getEndTime(){
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id)
                && Objects.equals(name, task.name)
                && Objects.equals(specification, task.specification)
                && status == task.status && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specification, status, startTime, duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specification='" + specification + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}