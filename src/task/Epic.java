package task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
//    private ArrayList<Subtask> subtasks = new ArrayList<>();
    HashMap<Integer,Subtask> subtasks = new HashMap<>();
    public Epic(Integer id, String name, String specification, TaskStatus status) {
        super(id, name, specification, status);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(),subtask);
    }

    public void updateStatus() {
        if (!subtasks.isEmpty()) {
            ArrayList<Subtask>subtasksList = new ArrayList<>();
            for (Subtask subtask : subtasks.values()) {
                subtasksList.add(subtask);
            }
            TaskStatus status=subtasksList.get(0).status;
             for (Subtask subtask:subtasksList) {
                 if (status.equals(subtask.status)) {
                    this.status = status;
                } else {
                    this.status = TaskStatus.getIsProgress();
                    break;
                }
            }
        } else {
            this.status = TaskStatus.getNEW();
        }
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specification='" + specification + '\'' +
                ", status='" + status + '\'' +
                ", subtasks=" + subtasks +
                '}';
    }
}



