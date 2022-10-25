package testmanager;


import taskmanager.TaskManager;

abstract class TaskManagerTest <T extends TaskManager> {

    protected T taskManager;


    void getTaskFromEmptyMap() {}

    void removeTaskFromEmptyMap(){}

    void removeTasksFromEmptyMap() {}
    void upDateTaskFromEmptyMap(){}

    void putTaskWithoutIdToEmptyMap(){}
    void upDateStatusAndTimeWith3Subtasks(){}
    void AddAndRemoveTaskFromMapAndHistory(){}

}