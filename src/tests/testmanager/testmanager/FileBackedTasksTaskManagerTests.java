package testmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;
import taskmanager.FileBackedTasksTaskManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksTaskManagerTests extends TaskManagerTest<FileBackedTasksTaskManager> {
    @BeforeEach
    public void newFileBackedTasksTaskManager() throws IOException {
        taskManager = new FileBackedTasksTaskManager("src\\resources\\file1.csv");
    }

    @AfterEach
    public void deleteFile() {
        File delete = new File("src\\resources\\file1.csv");
        boolean del = delete.delete();
    }

    @Test
    void putEpicAndReadWithoutSubtasks() throws IOException {
        Epic epic1 = new Epic("Что то 1",
                "Что то сделать 1");
        taskManager.createTask(epic1);

        FileBackedTasksTaskManager readFile = new FileBackedTasksTaskManager("src\\resources\\file1.csv");

        assertEquals(readFile.getMapEpics().get(1), epic1);
    }

    @Test
    void putTaskAndReadWithoutHistory() throws IOException {
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
        FileBackedTasksTaskManager readFile = new FileBackedTasksTaskManager("src\\resources\\file1.csv");

        assertAll(
                () -> assertTrue(readFile.getMapTasks().containsValue(task1)),
                () -> assertTrue(readFile.getMapEpics().containsValue(epic2)),
                () -> assertTrue(readFile.getMapSubtasks().containsValue(subtask3)),
                () -> assertTrue(readFile.getHistory().getHistory().isEmpty())
        );
    }

    @Test
    void putTaskAndReadWithHistory() throws IOException {
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

        FileBackedTasksTaskManager readFile = new FileBackedTasksTaskManager("src\\resources\\file1.csv");

        assertAll(
                () -> assertTrue(readFile.getMapTasks().containsValue(task1)),
                () -> assertTrue(readFile.getMapEpics().containsValue(epic2)),
                () -> assertTrue(readFile.getMapSubtasks().containsValue(subtask3)),
                () -> assertEquals(3, taskManager.getHistory().getHistory().size()),
                () -> assertTrue(taskManager.getHistory().getHistory().contains(task1)),
                () -> assertTrue(taskManager.getHistory().getHistory().contains(epic2)),
                () -> assertTrue(taskManager.getHistory().getHistory().contains(subtask3)));
    }
}