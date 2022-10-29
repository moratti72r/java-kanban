package testmanager;


import org.junit.jupiter.api.Test;
import taskmanager.TaskManager;

abstract class TaskManagerTest <T extends TaskManager> {

    protected T taskManager;


    @Test
    void catchExceptionWhenGetTaskByIdFromEmptyMap(){}

    @Test
    void catchExceptionWhenRemoveTaskByIdFromEmptyMap() {}

    @Test
    void catchExceptionWhenUpDateTaskFromEmptyMapOrIdNotExist() {}

    @Test
    void catchExceptionWhenCreateSubtaskWithoutEpic () {}

    @Test
    void putTasksWithoutIdToEmptyMap() {}

    @Test
    void checkUpDateStatusWhenAddSubtasks() {}

    @Test
    void checkUpDateTimeWhenAddSubtasks() {}

    @Test
    void whenViewedAgainTaskMoveToEndOfHistory() { }

    @Test
    void checkRemoveByIdFromHistoryAndMap() {}

    @Test
    void catchExceptionIntersectionOfTimesWhenAddTask() {}

    @Test
    void checkIntersectionOfTimes (){}
}