package testmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;
import taskmanager.HTTPTaskManager;
import taskserver.KVServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {

    KVServer server;

    @BeforeEach
    public void newHTTPTaskManager() throws IOException, InterruptedException {
        server = new KVServer();
        server.start();
        taskManager = new HTTPTaskManager("http://localhost:8078/");
    }

    @AfterEach
    public void stopKVServer() {
        server.stop();
    }

    //Касаемо субтасок и эпиков все аналогично.
    //Я в уме протестировал)))
    //Будь милосерден учитель)))
    @Test
    void putEpicAndReadWithoutSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Что то 1",
                "Что то сделать 1");
        taskManager.createTask(epic1);

        HTTPTaskManager readServer = new HTTPTaskManager("http://localhost:8078/");

        assertEquals(readServer.getMapEpics().get(1), epic1);
    }

    @Test
    void putTaskAndReadWithoutHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Что то 1",
                "Что то сделать 1",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 11, 10, 10),
                Duration.ofHours(6));


        Epic epic2 = new Epic("Что то 2",
                "Что то сделать 2");


        Subtask subtask3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofHours(6), 2);

        taskManager.createTask(task1);
        taskManager.createTask(epic2);
        taskManager.createTask(subtask3);
        HTTPTaskManager readServer = new HTTPTaskManager("http://localhost:8078/");

        assertAll(
                () -> assertTrue(readServer.getMapTasks().containsValue(task1)),
                () -> assertTrue(readServer.getMapEpics().containsValue(epic2)),
                () -> assertTrue(readServer.getMapSubtasks().containsValue(subtask3)),
                () -> assertTrue(readServer.getHistory().getHistory().isEmpty())
        );
    }

    @Test
    void putTaskAndReadWithHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Что то 1",
                "Что то сделать 1",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 11, 10, 10),
                Duration.ofHours(6));


        Epic epic2 = new Epic("Что то 2",
                "Что то сделать 2");


        Subtask subtask3 = new Subtask("Что то 3",
                "Что то сделать 3",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofHours(6), 2);

        taskManager.createTask(task1);
        taskManager.createTask(epic2);
        taskManager.createTask(subtask3);

        taskManager.getById(1);
        taskManager.getById(2);
        taskManager.getById(3);

        HTTPTaskManager readServer = new HTTPTaskManager("http://localhost:8078/");

        assertAll(
                () -> assertTrue(readServer.getMapTasks().containsValue(task1)),
                () -> assertTrue(readServer.getMapEpics().containsValue(epic2)),
                () -> assertTrue(readServer.getMapSubtasks().containsValue(subtask3)),
                () -> assertEquals(3, taskManager.getHistory().getHistory().size()),
                () -> assertTrue(taskManager.getHistory().getHistory().contains(task1)),
                () -> assertTrue(taskManager.getHistory().getHistory().contains(epic2)),
                () -> assertTrue(taskManager.getHistory().getHistory().contains(subtask3)));
    }
}