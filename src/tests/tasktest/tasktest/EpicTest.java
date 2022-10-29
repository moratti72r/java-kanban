package tasktest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

    class EpicTest {
      Epic epic;

    @BeforeEach
    void newEpic (){
    epic = new Epic(1, "Что то 1", "Что то сделать 1");
    }

    @Test
    void epicWithoutSubtasksIsStatusNew () {
         TaskStatus taskStatus = epic.getStatus();
        assertEquals (TaskStatus.NEW, taskStatus);
     }

    @Test
    void epicWithTwoSubtasksIsStatusNew () {
        epic.addSubtask(new Subtask(2, "Что то 2", "Что то сделать 2", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(6), 1));
        epic.addSubtask(new Subtask(3, "Что то 3", "Что то сделать 3", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(6), 1));
        TaskStatus taskStatus = epic.getStatus();
        assertEquals (TaskStatus.NEW, taskStatus);
    }

    @Test
    void epicWithTwoSubtasksIsStatusDone () {
        epic.addSubtask(new Subtask(2, "Что то 2", "Что то сделать 2", TaskStatus.DONE, LocalDateTime.now(), Duration.ofHours(6), 1));
        epic.addSubtask(new Subtask(3, "Что то 3", "Что то сделать 3", TaskStatus.DONE, LocalDateTime.now(), Duration.ofHours(6), 1));
        TaskStatus taskStatus = epic.getStatus();
        assertEquals (TaskStatus.DONE, taskStatus);
    }

    @Test
    void epicWithTwoSubtasksIsStatusDoneAndNew () {
        epic.addSubtask(new Subtask(2, "Что то 2", "Что то сделать 2", TaskStatus.DONE, LocalDateTime.now(), Duration.ofHours(6), 1));
        epic.addSubtask(new Subtask(3, "Что то 3", "Что то сделать 3", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(6), 1));
        TaskStatus taskStatus = epic.getStatus();
        assertEquals (TaskStatus.IS_PROGRESS, taskStatus);
    }

    @Test
    void epicWithTwoSubtasksIsStatusIsProgress () {
        epic.addSubtask(new Subtask(2, "Что то 2", "Что то сделать 2", TaskStatus.IS_PROGRESS, LocalDateTime.now(), Duration.ofHours(6), 1));
        epic.addSubtask(new Subtask(3, "Что то 3", "Что то сделать 3", TaskStatus.IS_PROGRESS, LocalDateTime.now(), Duration.ofHours(6), 1));
        TaskStatus taskStatus = epic.getStatus();
        assertEquals (TaskStatus.IS_PROGRESS, taskStatus);
    }

    @Test
    void epicWithoutSubtasksEndTimeIsMaxValue (){
        LocalDateTime ldt = epic.getEndTime();
        assertEquals (LocalDateTime.MAX, ldt);
    }

        @Test
        void epicWithTwoSubtasksEndTimeIsSubtaskEndTime (){
            epic.addSubtask(new Subtask(2, "Что то 2", "Что то сделать 2", TaskStatus.NEW, LocalDateTime.of (2000,10,10,10,10,10,10), Duration.ofHours(6), 1));
            epic.addSubtask(new Subtask(3, "Что то 3", "Что то сделать 3",TaskStatus.NEW, LocalDateTime.of (2000,10,20,20,20,20,20), Duration.ofHours(6), 1));
            LocalDateTime ldt = epic.getEndTime();
            LocalDateTime ldtSubtask = epic.getSubtasks().get(3).getEndTime();
            assertEquals (ldtSubtask, ldt);
        }

        @Test
        void epicWithTwoSubtasksStartTimeIsSubtaskStartTime (){
            epic.addSubtask(new Subtask(2, "Что то 2", "Что то сделать 2", TaskStatus.NEW, LocalDateTime.of (2000,10,10,10,10,10,10), Duration.ofHours(6), 1));
            epic.addSubtask(new Subtask(3, "Что то 3", "Что то сделать 3",TaskStatus.NEW, LocalDateTime.of (2000,10,20,20,20,20,20), Duration.ofHours(6), 1));
            LocalDateTime ldt = epic.getStartTime();
            LocalDateTime ldtSubtask = epic.getSubtasks().get(2).getStartTime();
            assertEquals (ldtSubtask, ldt);
        }
}