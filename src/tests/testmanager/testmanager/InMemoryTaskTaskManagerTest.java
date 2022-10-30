package testmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;
import taskmanager.InMemoryTaskTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskTaskManagerTest extends TaskManagerTest <InMemoryTaskTaskManager>
{   //При таком наследовании ТЕСТЫ сроабатывают только один раз.
    //При повторном RUN выдает ошибку java: cannot find symbol symbol: class TaskManagerTest
    //Также хотел применить аннотацию BeforeAll, но он также отказывается работать -
    // - сначала при его объявлении import его не видел
    // - затем после какой-то магии он его увидел, но безрезультатно
    // - ошибки возникающие без указания static учел

        @BeforeEach
    public void newInMemoryTaskManager() {
        taskManager = new InMemoryTaskTaskManager();
    }

    @Test
    void catchExceptionWhenGetTaskByIdFromEmptyMap(){
        Exception ex = assertThrows (Exception.class, ()-> taskManager.getById(2));
        assertEquals("Задача под введенным id отсутствует",ex.getMessage());
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
                        TaskStatus.DONE, LocalDateTime.now().minusMinutes(6), Duration.ofHours(6))));

        Exception exSubtask = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Subtask(10, "Что то 10", "Что то сделать 10",
                        TaskStatus.NEW, LocalDateTime.now().minusMinutes(10), Duration.ofHours(6), 4)));

        Exception exEpic = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Epic(4, "Что то 4", "Что то сделать 4")));

        taskManager.createTask(new Task(6, "Что то 6", "Что то сделать 6",
                TaskStatus.DONE, LocalDateTime.now().minusMinutes(6), Duration.ofHours(6)));

        taskManager.createTask(new Epic(4, "Что то 4", "Что то сделать 4"));

        Exception exSubtask1 = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Subtask(11, "Что то 10", "Что то сделать 10",
                        TaskStatus.NEW, LocalDateTime.now().minusMinutes(10), Duration.ofHours(6), 4)));

        assertAll(
                () -> assertEquals("Задача с таким id отсутствует", exTask.getMessage()),
                () -> assertEquals("Задача с таким id отсутствует", exSubtask.getMessage()),
                () -> assertEquals("Задача с таким id отсутствует", exEpic.getMessage()),
                () -> assertEquals("Задача с таким id отсутствует", exSubtask1.getMessage()));

    }

    @Test
    void catchExceptionWhenCreateSubtaskWithoutEpic () {
        Subtask subtask1 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10, 10, 10),
                Duration.ofHours(6), 2);

        Exception exSubtasks = assertThrows(Exception.class, () -> taskManager.createTask(subtask1)); // throw Exception
        assertEquals("Эпик с таким id отсутствует", exSubtasks.getMessage());                  //бросить ошибку т.к. нет соответ.эпика
        assertNull(subtask1.getId());                                                                 //отсутствие id т.к. возникла ошибка выше
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
                () -> assertTrue(taskManager.getMapTasks().containsValue(task1)),                    //добавить в мапуТасок
                () -> assertEquals(1, task1.getId()),                                         //соответствие id
                () -> assertTrue(taskManager.getMapEpics().containsValue(epic2)),                    //дабавить в мапуЭпик
                () -> assertEquals(2, epic2.getId()),                                         //соответствие id
                () -> assertTrue(taskManager.getMapSubtasks().containsValue(subtask3)),              //добавить в мапуСабтасок т.к. есть соотв.эпик
                () -> assertEquals(3, subtask3.getId()));                                     //соответствие id
    }

    @Test
    void checkUpDateStatusWhenAddSubtasks() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");
        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);

        assertEquals(TaskStatus.DONE, epicId1.getStatus());

        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        assertEquals(TaskStatus.DONE, epicId1.getStatus());                   //3 сабтаски со статусом DONE

        subtaskId4 = new Subtask(4, "Что то 4",
                "Что то сделать 4",
                TaskStatus.NEW,
                LocalDateTime.of(2000, 1, 4, 4, 4, 4, 4),
                Duration.ofHours(4), 1);
        taskManager.upDateTask(subtaskId4);

        assertEquals(TaskStatus.IS_PROGRESS, epicId1.getStatus());           //обновили статус 4 сабтаски на NEW


        subtaskId3 = new Subtask(3, "Что то 3",
                "Что то сделать 3",
                TaskStatus.NEW,
                LocalDateTime.of(2000, 2, 3, 3, 3, 3, 3),
                Duration.ofHours(3), 1);
        taskManager.upDateTask(subtaskId3);

        subtaskId2 = new Subtask(2, "Что то 2",
                "Что то сделать 2",
                TaskStatus.NEW,
                LocalDateTime.of(2000, 3, 2, 2, 2, 2, 2),
                Duration.ofHours(2), 1);
        taskManager.upDateTask(subtaskId2);

        assertEquals(TaskStatus.NEW, epicId1.getStatus());                   // обновили стстусы всех сабтасок на NEW
    }

    @Test
    void checkUpDateTimeWhenAddSubtasks() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");
        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);

        assertEquals(subtaskId2.getStartTime(), epicId1.getStartTime());
        assertEquals(subtaskId2.getEndTime(), epicId1.getEndTime());

        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        assertEquals(subtaskId2.getStartTime(), epicId1.getStartTime());     //соответствие время старта
        assertEquals(subtaskId4.getEndTime(), epicId1.getEndTime());         //соответствие времени завершения
    }

    @Test
    void whenViewedAgainTaskMoveToEndOfHistory() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4, 4, 4),
                Duration.ofHours(4), 1);

        taskManager.createTask(epicId1);
        taskManager.createTask(subtaskId2);
        taskManager.createTask(subtaskId3);
        taskManager.createTask(subtaskId4);

        Epic epicHistory1 = (Epic) taskManager.getById(1);
        Subtask subtaskHistory2 = (Subtask) taskManager.getById(2);
        Subtask subtaskHistory3 = (Subtask) taskManager.getById(3);
        Subtask subtaskHistory4 = (Subtask) taskManager.getById(4);

        assertEquals(4, taskManager.getHistory().getHistory().size());
        assertTrue(subtaskId2.equals(taskManager.getHistory().getHistory().get(1)));

        subtaskHistory2 = (Subtask) taskManager.getById(2);
        assertEquals(4, taskManager.getHistory().getHistory().size());
        assertFalse(subtaskId2.equals(taskManager.getHistory().getHistory().get(1)));
        assertTrue(subtaskId2.equals(taskManager.getHistory().getHistory().get(3)));
    }

    @Test
    void checkRemoveByIdFromHistoryAndMap() {
        Epic epicId1 = new Epic("Что то 1",
                "Что то сделать 1");

        Subtask subtaskId2 = new Subtask("Что то 2",
                "Что то сделать 2",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 2, 2, 2, 2, 2, 2),
                Duration.ofHours(2), 1);

        Subtask subtaskId3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 3, 3, 3, 3, 3, 3),
                Duration.ofHours(3), 1);

        Subtask subtaskId4 = new Subtask("Что то 4",
                "Что то сделать 4",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 4, 4, 4, 4, 4, 4),
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
        assertEquals(2, taskManager.getMapSubtasks().size());
        assertTrue(taskManager.getMapSubtasks().containsKey(subtaskId2.getId()));
        assertFalse(taskManager.getMapSubtasks().containsKey(subtaskId3.getId()));

        assertEquals(3, taskManager.getHistory().getHistory().size());
        assertFalse(taskManager.getHistory().getHistory().contains(subtaskId3));
    }

        @Test
    void catchExceptionIntersectionOfTimesWhenAddTask() {
            Epic epicId1 = new Epic("Что то 1",
                    "Что то сделать 1");

            Subtask subtaskId2 = new Subtask("Что то 2",
                    "Что то сделать 2",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 2, 2, 2, 2, 2, 2),
                    Duration.ofHours(2), 1);

            Subtask subtaskId3 = new Subtask("Что то 3",
                    "Что то сделать 3",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 3, 3, 3, 3, 3, 3),
                    Duration.ofHours(3), 1);

            Subtask subtaskId4 = new Subtask("Что то 4",
                    "Что то сделать 4",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 4, 4, 4, 4, 4, 4),
                    Duration.ofHours(4), 1);

            Task taskId5 = new Task("Что то 5",
                    "Что то сделать 5",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 4, 4, 5, 5, 5, 5),
                    Duration.ofHours(5));


            taskManager.createTask(epicId1);
            taskManager.createTask(subtaskId2);
            taskManager.createTask(subtaskId3);
            taskManager.createTask(subtaskId4);

            Exception ex = assertThrows (Exception.class, ()-> taskManager.createTask(taskId5));
            assertEquals("Данное время уже занято",ex.getMessage());
        }

        @Test
        void checkIntersectionOfTimes (){

            Epic epicId1 = new Epic("Что то 1",
                    "Что то сделать 1");

            Subtask subtaskId2 = new Subtask("Что то 2",
                    "Что то сделать 2",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 2, 2, 2, 2, 2, 2),
                    Duration.ofHours(2), 1);

            Subtask subtaskId3 = new Subtask("Что то 3",
                    "Что то сделать 3",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 3, 3, 3, 3, 3, 3),
                    Duration.ofHours(3), 1);

            Subtask subtaskId4 = new Subtask("Что то 4",
                    "Что то сделать 4",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 4, 4, 4, 4, 4, 4),
                    Duration.ofHours(4), 1);

            Task taskId5 = new Task("Что то 5",
                    "Что то сделать 5",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 5, 5, 5, 5, 5, 5),
                    Duration.ofHours(5));

            Task taskId6 = new Task("Что то 6",
                    "Что то сделать 6",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 6, 6, 6, 6, 6, 6),
                    Duration.ofHours(6));

            Task taskId7 = new Task("Что то 7",
                    "Что то сделать 7",
                    TaskStatus.DONE,
                    LocalDateTime.of(2000, 7, 7, 7, 7, 7, 7),
                    Duration.ofHours(7));

            taskManager.createTask(epicId1);
            taskManager.createTask(subtaskId2);
            taskManager.createTask(subtaskId3);
            taskManager.createTask(subtaskId4);
            taskManager.createTask(taskId5);
            taskManager.createTask(taskId6);
            taskManager.createTask(taskId7);

            assertEquals(7,taskManager.getPrioritizedTasks().size());

            assertFalse(taskManager.prioritizedTasksIsIntersectionOfTimes());
        }
        }