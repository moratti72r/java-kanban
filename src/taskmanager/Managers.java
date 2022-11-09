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

    private static final HTTPTaskManager HTTP_TASK_MANAGER;

    static {
        try {
            HTTP_TASK_MANAGER = new HTTPTaskManager("http://localhost:8078/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static TaskManager getInMemoryTaskTaskManager(){return IN_MEMORY_TASK_TASK_MANAGER;}
    public static TaskManager getDefault(){
        return HTTP_TASK_MANAGER;
    }

    public static HistoryManager getDefaultHistory(){ return historyManager; }

    public static TaskManager getFileBackedTasksTaskManager(){
        return FILE_BACKED_TASKS_TASK_MANAGER;
    }

    public static TaskManager getHTTPTaskManager() {return HTTP_TASK_MANAGER;}
}
