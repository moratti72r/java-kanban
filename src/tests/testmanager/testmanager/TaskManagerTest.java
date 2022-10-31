package testmanager;

import org.junit.jupiter.api.Test;
import taskmanager.TaskManager;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void catchExceptionWhenGetTaskByIdFromEmptyMap() {
    }

    @Test
    void catchExceptionWhenRemoveTaskByIdFromEmptyMap() {
    }

    @Test
    void catchExceptionWhenUpDateTaskFromEmptyMapOrIdNotExist() {
    }

    @Test
    void catchExceptionWhenCreateSubtaskWithoutEpic() {
    }

    @Test
    void putTasksWithoutIdToEmptyMap() {
    }

    @Test
    void statusEpicUpDateToProgressWhenAddSubtasksWithDifferentStatuses() {
    }

    @Test
    void statusEpicUpDateToNewWhenNoSubtaskOrAddSubtasksWithStatusNew() {
    }

    @Test
    void statusEpicUpDateToDoneWhenAddSubtasksWithStatusDone() {
    }

    @Test
    void checkUpDateTimeWhenAddSubtasks() {
    }

    @Test
    void historyIsEmptyWhenCreateTask() {
    }

    @Test
    void whenViewedAgainTaskMoveToEndOfHistory() {
    }

    @Test
    void removeTaskFromHistoryWhenRemoveById() {
    }

    @Test
    void catchExceptionWhenAddTaskWithStartTimeThatIntersectIntervalAnotherTask() {
    }

    @Test
    void catchExceptionWhenAddTaskWithEndTimeThatIntersectIntervalAnotherTask() {
    }

    @Test
    void catchExceptionWhenAddTaskWithStartTimeEqualsStartTimeAnotherTask() {
    }

    @Test
    void catchExceptionWhenAddTaskWithEndTimeEqualsEndTimeAnotherTask() {
    }

    @Test
    void catchExceptionWhenAddTaskWithStartTimeEqualsEndTimeAnotherTask() {
    }

    @Test
    void catchExceptionWhenAddTaskWithEndTimeEqualsStartTimeAnotherTask() {
    }

    @Test
    void checkIntersectionOfTimes() {
    }
}