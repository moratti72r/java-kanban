package taskmanager;

import historymanager.HistoryManager;

public class Managers {
    private static final TaskManager IN_MEMORY_TASK_TASK_MANAGER = new InMemoryTaskTaskManager();
    private static final HistoryManager historyManager = IN_MEMORY_TASK_TASK_MANAGER.getHistory();


    public static TaskManager getDefault(){
        return IN_MEMORY_TASK_TASK_MANAGER;
    }

    public static HistoryManager getDefaultHistory(){ return historyManager; }
}
