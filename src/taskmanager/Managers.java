package taskmanager;

import historymanager.HistoryManager;

import java.io.IOException;

public class Managers {
    private static final TaskManager IN_MEMORY_TASK_TASK_MANAGER = new InMemoryTaskTaskManager();
    private static final HistoryManager historyManager = IN_MEMORY_TASK_TASK_MANAGER.getHistory();

    private static final TaskManager FILE_BACKED_TASKS_TASK_MANAGER;

    static {
        try {
            FILE_BACKED_TASKS_TASK_MANAGER = new FileBackedTasksTaskManager("src\\resources\\file1.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public static TaskManager getDefault(){
        return IN_MEMORY_TASK_TASK_MANAGER;
    }

    public static HistoryManager getDefaultHistory(){ return historyManager; }

    public static TaskManager getFileBackedTasksTaskManager(){
        return FILE_BACKED_TASKS_TASK_MANAGER;
    }
}
