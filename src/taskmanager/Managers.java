package taskmanager;

import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;

public class Managers {
    private static final Manager inMemoryTaskManager = new InMemoryTaskManager();
    private static final HistoryManager historyManager = new InMemoryHistoryManager();


    public static Manager getDefault(){
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory(){ return historyManager; }
}
