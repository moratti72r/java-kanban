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

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskTaskManagerTest extends TaskManagerTest <InMemoryTaskTaskManager>
{

        @BeforeEach
    public void newInMemoryTaskManager(){
        taskManager = new InMemoryTaskTaskManager();
    }

    @Test
    void getTaskFromEmptyMap(){
        Exception ex = assertThrows (Exception.class, ()-> taskManager.getById(2));
        assertEquals("Задача под введенным id отсутствует",ex.getMessage());
    }

    @Test
    void removeTaskFromEmptyMap() {
        Exception ex = assertThrows(RuntimeException.class, () -> taskManager.removeById(2));
        assertEquals("Задача под введенным id отсутствует", ex.getMessage());
    }

    @Test
    void removeTasksFromEmptyMap() {
        Exception exTasks = assertThrows(Exception.class, () -> taskManager.removeTasks());
        Exception exSubtasks = assertThrows(Exception.class, () -> taskManager.removeSubtasks());
        Exception exEpics = assertThrows(Exception.class, () -> taskManager.removeEpic());
        assertAll(
                () -> assertEquals("Список задач уже пуст", exTasks.getMessage()),
                () -> assertEquals("Список задач уже пуст", exSubtasks.getMessage()),
                () -> assertEquals("Список задач уже пуст", exEpics.getMessage()));
    }

    @Test
    void upDateTaskFromEmptyMap() {
        Exception exTask = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Task(6, "Что то 6", "Что то сделать 6",
                        TaskStatus.DONE, LocalDateTime.now().minusMinutes(6), Duration.ofHours(6))));

        Exception exSubtask = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Subtask(10, "Что то 10", "Что то сделать 10",
                        TaskStatus.NEW, LocalDateTime.now().minusMinutes(10), Duration.ofHours(6), 4)));

        Exception exEpic = assertThrows(RuntimeException.class,
                () -> taskManager.upDateTask(new Epic(4, "Что то 4", "Что то сделать 4")));

        assertAll(
                () -> assertEquals("Задача с таким id отсутствует", exTask.getMessage()),
                () -> assertEquals("Задача с таким id отсутствует", exSubtask.getMessage()),
                () -> assertEquals("Задача с таким id отсутствует", exEpic.getMessage()));

    }

    @Test
    void putTaskWithoutIdToEmptyMap() {
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


        Exception exSubtasks = assertThrows(Exception.class, () -> taskManager.createTask(subtask3)); // throw Exception
        assertEquals("Эпик с таким id отсутствует", exSubtasks.getMessage());                  //бросить ошибку т.к. нет соответ.эпика
        assertNull(subtask3.getId());                                                                 //отсутствие id т.к. возникла ошибка выше


        taskManager.createTask(task1); //id = 1

        taskManager.createTask(epic2); //id = 2
        assertEquals(TaskStatus.NEW, epic2.getStatus());                                              //проверка статуса Эпика без сабтасок

        taskManager.createTask(subtask3); //id = 3 , epic id = 2

        assertAll(
                () -> assertTrue(taskManager.getMapTasks().containsValue(task1)),                    //добавить в мапуТасок
                () -> assertEquals(1, task1.getId()),                                         //соответствие id
                () -> assertTrue(taskManager.getMapEpics().containsValue(epic2)),                    //дабавить в мапуЭпик
                () -> assertEquals(2, epic2.getId()),                                         //соответствие id
                () -> assertTrue(taskManager.getMapSubtasks().containsValue(subtask3)),              //добавить в мапуСабтасок т.к. есть соотв.эпик
                () -> assertEquals(3, subtask3.getId()),                                      //соответствие id
                () -> assertEquals(subtask3.getStatus(), epic2.getStatus()),                          //соответствие статуса при един.сабтаске
                () -> assertEquals(subtask3.getStartTime(), epic2.getStartTime()),                    //соответствие старта при един.сабтаске
                () -> assertEquals(subtask3.getEndTime(), epic2.getEndTime()));                       //соответствие конца при един.сабтаске
    }

    @Test
    void upDateStatusAndTimeWith3Subtasks() {
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

        assertEquals(TaskStatus.DONE, epicId1.getStatus());                   //3 сабтаски со статусом DONE
        assertEquals(subtaskId2.getStartTime(), epicId1.getStartTime());     //соответствие время старта
        assertEquals(subtaskId4.getEndTime(), epicId1.getEndTime());         //соответствие времени завершения

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
        assertEquals(subtaskId4.getStartTime(), epicId1.getStartTime());     //соответствие время старта
        assertEquals(subtaskId2.getEndTime(), epicId1.getEndTime());         //соответствие времени завершения
    }

    @Test
    void AddAndRemoveTaskFromMapAndHistory() {
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

        Task task6 = taskManager.getById(6);
        Task task7 = taskManager.getById(7);
        Subtask subtask4 = (Subtask) taskManager.getById(4);
        Subtask subtask5 = (Subtask) taskManager.getById(3);
        Epic epic1 = (Epic) taskManager.getById(1);

        assertAll(
                () -> assertEquals(taskManager.getMapTasks().get(6), task6),                   //сравнение полученной таски
                () -> assertEquals(taskManager.getMapSubtasks().get(4), subtask4),             //сравнение полученной сабтаски
                () -> assertEquals(taskManager.getMapEpics().get(1), epic1),                   //сравнение полученного эпика
                () -> assertEquals(5, taskManager.getHistory().getHistory().size()),  //проверка размера истории
                () -> assertTrue(taskManager.getHistory().getHistory().contains(task6)),      //проверка на наличие в истории
                () -> assertTrue(taskManager.getHistory().getHistory().contains(subtask4)),   //проверка на наличие в истории
                () -> assertTrue(taskManager.getHistory().getHistory().contains(epic1)));     //проверка на наличие в истории

        taskManager.removeById(7);
        taskManager.removeById(6);
        assertEquals(3, taskManager.getHistory().getHistory().size());
        assertFalse(taskManager.getHistory().getHistory().contains(task6));
        assertEquals(1, taskManager.getMapTasks().size());
        assertTrue(taskManager.getMapTasks().containsValue(taskId5));

        taskManager.removeSubtasks();
        assertEquals(1, taskManager.getHistory().getHistory().size());                 // 1 эпик
    }
    }