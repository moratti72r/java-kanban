package historymanager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        for (Task task : customLinkedList.getTasks()) {
            if (task instanceof Epic) {
                for (Subtask subtask : ((Epic) task).getSubtasks().values()) {
                    customLinkedList.removeNode(subtask.getId());
                }
                customLinkedList.removeNode(id);
            } else customLinkedList.removeNode(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }
}
