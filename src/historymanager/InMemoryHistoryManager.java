package historymanager;

import task.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size()<=9){
            history.add (task);
        }else {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskHistory = new LinkedList<>();
        taskHistory = history;
        return taskHistory;
    }
}
