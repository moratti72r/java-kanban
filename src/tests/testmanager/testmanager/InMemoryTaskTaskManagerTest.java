package testmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;
import taskmanager.InMemoryTaskTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskTaskManagerTest extends TaskManagerTest<InMemoryTaskTaskManager> {

    @BeforeEach
    public void newInMemoryTaskManager() {
        taskManager = new InMemoryTaskTaskManager();
    }

    @Test
    void catchExceptionWhenGetTaskByIdFromEmptyMap() {
        Exception ex = assertThrows(Exception.class, () -> taskManager.getById(2));
        assertEquals("Задача под введенным id отсутствует", ex.getMessage());
    }

    @Test
    void catchExceptionWhenRemoveTaskByIdFromEmptyMap() {
        Exception ex = assertThrows(RuntimeException.class, () -> taskManager.removeById(2));
        assertEquals("Задача под введенным id отсутствует", ex.getMessage());
    }

    @Test
    void catchExceptionWhenUpDateTaskFromEmptyMapOrIdNotExist() {
        Exception exTask = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Task(6, "Что то 6", "Что то сделать 6",
                        TaskStatus.DONE, LocalDateTime.of(2000, 10, 10, 10, 10), Duration.ofHours(6))));

        Exception exSubtask = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Subtask(10, "Что то 10", "Что то сделать 10",
                        TaskStatus.NEW, LocalDateTime.of(2000, 10, 10, 10, 10), Duration.ofHours(6), 4)));

        Exception exEpic = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Epic(4, "Что то 4", "Что то сделать 4")));

        taskManager.createTask(new Task(6, "Что то 6", "Что то сделать 6",
                TaskStatus.DONE, LocalDateTime.of(2000, 10, 10, 10, 10), Duration.ofHours(6)));

        taskManager.createTask(new Epic(4, "Что то 4", "Что то сделать 4"));

        Exception exSubtask1 = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Subtask(11, "Что то 10", "Что то сделать 10",
                        TaskStatus.NEW, LocalDateTime.of(2000, 10, 11, 10, 10), Duration.ofHours(6), 4)));

        assertAll(
                () -> assertEquals("Задача с таким id отсутствует", exTask.getMessage()),
                () -> assertEquals("Задача с таким id отсутствует", exSubtask.getMessage()),
                () -> assertEquals("Задача с таким id отсутствует", exEpic.getMessage()),
                () -> assertEquals("Задача с таким id отсутствует", exSubtask1.getMessage()));

    }

    @Test
    void catchExceptionWhenCreateSubtaskWithoutEpic() {
        Subtask subtask1 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofHours(6), 2);

        Exception exSubtasks = assertThrows(Exception.class, () -> taskManager.createTask(subtask1));
        assertEquals("Эпик с таким id отсутствует", exSubtasks.getMessage());
        assertNull(subtask1.getId());
    }

    @Test
    void putTasksWithoutIdToEmptyMap() {
        Task task1 = new Task("Что то 1",
                "Что то сделать 1",
                TaskStatus.DONE,
                LocalDateTime.now().minusMinutes(6),
                Duration.ofHours(6));


        Epic epic2 = new Epic("Что то 2",
                "Что то сделать 2");


        Subtask subtask3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10, 10, 10),
                Duration.ofHours(6), 2);


        taskManager.createTask(task1); //id = 1
        taskManager.createTask(epic2); //id = 2
        taskManager.createTask(subtask3); //id = 3 , epic id = 2

        assertAll(
                () -> assertTrue(taskManager.getMapTasks().containsValue(task1)),
                () -> assertEquals(1, task1.getId()),
                () -> assertTrue(taskManager.getMapEpics().containsValue(epic2)),
                () -> assertEquals(2, epic2.getId()),
                () -> assertEquals(subtask3, epic2.getSubtasks().get(3)),
                () -> assertTrue(taskManager.getMapSubtasks().containsValue(subtask3)),
                () -> assertEquals(3, subtask3.getId()));
    }

    @Test
    void statusEpicUpDateToProgressWhenAddSubtasksWithDifferentStatuses() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");
        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.NEW,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.IS_PROGRESS,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        assertEquals(TaskStatus.IS_PROGRESS, epicId1.getStatus());
    }

    @Test
    void statusEpicUpDateToNewWhenNoSubtaskOrAddSubtasksWithStatusNew() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");
        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.NEW,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.NEW,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.NEW,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        Epic epicId5 = new Epic("Что то 5",
                "Что то сделать 5");

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);
        taskManager.createTask(epicId5);

        assertEquals(TaskStatus.NEW, epicId1.getStatus());
        assertEquals(TaskStatus.NEW, epicId5.getStatus());
    }

    @Test
    void statusEpicUpDateToDoneWhenAddSubtasksWithStatusDone() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");
        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        assertEquals(TaskStatus.DONE, epicId1.getStatus());
    }

    @Test
    void checkUpDateTimeWhenAddSubtasks() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");
        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        assertEquals(subtaskId2.getStartTime(), epicId1.getStartTime());
        assertEquals(subtaskId4.getEndTime(), epicId1.getEndTime());
    }

    @Test
    void historyIsEmptyWhenCreateTask() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        assertTrue(taskManager.getHistory().getHistory().isEmpty());
    }

    @Test
    void whenViewedAgainTaskMoveToEndOfHistory() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Epic epicHistory1 = (Epic) taskManager.getById(1);
        Subtask subtaskHistory2 = (Subtask) taskManager.getById(2);
        Subtask subtaskHistory3 = (Subtask) taskManager.getById(3);
        Subtask subtaskHistory4 = (Subtask) taskManager.getById(4);
        Subtask subtaskHistory5 = (Subtask) taskManager.getById(2);

        assertEquals(4, taskManager.getHistory().getHistory().size());
        assertEquals(epicId1, taskManager.getHistory().getHistory().get(0));
        assertNotEquals(subtaskId2, taskManager.getHistory().getHistory().get(1));
        assertEquals(subtaskId3, taskManager.getHistory().getHistory().get(1));
        assertEquals(subtaskId4, taskManager.getHistory().getHistory().get(2));
        assertEquals(subtaskId2, taskManager.getHistory().getHistory().get(3));
    }

    @Test
    void removeTaskFromHistoryWhenRemoveById() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);


        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Epic epicHistory1 = (Epic) taskManager.getById(1);
        Subtask subtaskHistory2 = (Subtask) taskManager.getById(2);
        Subtask subtaskHistory3 = (Subtask) taskManager.getById(3);
        Subtask subtaskHistory4 = (Subtask) taskManager.getById(4);

        taskManager.removeById(3);
        assertEquals(3, taskManager.getHistory().getHistory().size());
        assertTrue(taskManager.getHistory().getHistory().contains(epicId1));
        assertTrue(taskManager.getHistory().getHistory().contains(subtaskId2));
        assertTrue(taskManager.getHistory().getHistory().contains(subtaskId4));
        assertFalse(taskManager.getHistory().getHistory().contains(subtaskId3));
    }

    @Test
    void catchExceptionWhenAddTaskWithStartTimeThatIntersectIntervalAnotherTask() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        Task taskId5 = new Task("Что то 5",
                "Что то сделать 5",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 5, 5),
                Duration.ofHours(5));


        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Exception ex = assertThrows(Exception.class, () -> taskManager.createTask(taskId5));
        assertEquals("Данное время уже занято", ex.getMessage());
    }

    @Test
    void catchExceptionWhenAddTaskWithEndTimeThatIntersectIntervalAnotherTask() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        Task taskId5 = new Task("Что то 5",
                "Что то сделать 5",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 1, 5),
                Duration.ofHours(5));


        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Exception ex = assertThrows(Exception.class, () -> taskManager.createTask(taskId5));
        assertEquals("Данное время уже занято", ex.getMessage());
    }

    @Test
    void catchExceptionWhenAddTaskWithStartTimeEqualsStartTimeAnotherTask() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        Task taskId5 = new Task("Что то 5",
                "Что то сделать 5",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(5));


        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Exception ex = assertThrows(Exception.class, () -> taskManager.createTask(taskId5));
        assertEquals("Данное время уже занято", ex.getMessage());
    }

    @Test
    void catchExceptionWhenAddTaskWithEndTimeEqualsEndTimeAnotherTask() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        Task taskId5 = new Task("Что то 5",
                "Что то сделать 5",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 3, 4),
                Duration.ofHours(5));


        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Exception ex = assertThrows(Exception.class, () -> taskManager.createTask(taskId5));
        assertEquals("Данное время уже занято", ex.getMessage());
    }

    @Test
    void catchExceptionWhenAddTaskWithStartTimeEqualsEndTimeAnotherTask() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        Task taskId5 = new Task("Что то 5",
                "Что то сделать 5",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 8, 4),
                Duration.ofHours(5));


        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Exception ex = assertThrows(Exception.class, () -> taskManager.createTask(taskId5));
        assertEquals("Данное время уже занято", ex.getMessage());
    }

    @Test
    void catchExceptionWhenAddTaskWithEndTimeEqualsStartTimeAnotherTask() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        Task taskId5 = new Task("Что то 5",
                "Что то сделать 5",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 0, 4),
                Duration.ofHours(5));


        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Exception ex = assertThrows(Exception.class, () -> taskManager.createTask(taskId5));
        assertEquals("Данное время уже занято", ex.getMessage());
    }

    @Test
    void checkIntersectionOfTimes() {

        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        Task taskId5 = new Task("Что то 5",
                "Что то сделать 5",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 5, 5, 5, 5),
                Duration.ofHours(5));

        Task taskId6 = new Task("Что то 6",
                "Что то сделать 6",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 6, 6, 6, 6),
                Duration.ofHours(6));

        Task taskId7 = new Task("Что то 7",
                "Что то сделать 7",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 7, 7, 7, 7),
                Duration.ofHours(7));

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);
        taskManager.createTask(taskId5);
        taskManager.createTask(taskId6);
        taskManager.createTask(taskId7);

        boolean isIntersectionOfTimes = false;
        ArrayList<Task> tasksList = new ArrayList<>(new ArrayList<>(taskManager.getPrioritizedTasks()));
        for (int i = 0; i < tasksList.size() - 1; i++) {
            if (isIntersectionOfTimes) break;
            if (tasksList.get(i).getStartTime().isBefore(tasksList.get(i + 1).getStartTime())) {
                isIntersectionOfTimes = Duration.between(tasksList.get(i).getStartTime(), tasksList.get(i + 1).getStartTime()).toMinutes() <= tasksList.get(i).getDuration().toMinutes();
            } else if (tasksList.get(i).getStartTime().isAfter(tasksList.get(i + 1).getStartTime())) {
                isIntersectionOfTimes = Duration.between(tasksList.get(i + 1).getStartTime(), tasksList.get(i).getStartTime()).toMinutes() <= tasksList.get(i + 1).getDuration().toMinutes();
            } else isIntersectionOfTimes = tasksList.get(i).getStartTime().equals(tasksList.get(i + 1).getStartTime());
        }

        assertEquals(6, taskManager.getPrioritizedTasks().size());
        assertFalse(isIntersectionOfTimes);
    }
}