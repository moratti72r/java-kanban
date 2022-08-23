package task;

import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String specification;
    protected TaskStatus status;


    public Task(Integer id, String name, String specification, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.specification = specification;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id)
                && Objects.equals(name, task.name)
                && Objects.equals(specification, task.specification)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specification, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specification='" + specification + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}