package historymanager;

import task.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (customLinkedList.getTasks() != null && customLinkedList.getTasks().contains(task)) {
            customLinkedList.removeNode(task.getId());
        }
        customLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(id);

    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }
}
