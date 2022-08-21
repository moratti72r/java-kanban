package task;

import java.util.Objects;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(Integer id, String name, String specification, TaskStatus status, Epic epic){
        super(id, name, specification, status);
        this.epic = epic;
//        for (Subtask subtask : epic.getSubtasks()) {
//            if (this.id== subtask.getId()){
//                epic.getSubtasks().remove(subtask);
//            }
//        }
//        epic.addSubtask(this);
//        epic.updateStatus();
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epic, subtask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specification='" + specification + '\'' +
                ", status='" + status + '\'' +
                " epicName='" + epic.getName() + '\'' +
                '}';
    }
}

