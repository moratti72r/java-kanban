import taskmanager.HTTPTaskManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        HTTPTaskManager manager = new HTTPTaskManager("http://localhost:8078/");

//        Task task1 = new Task("Что то 1",
//                "Что то сделать 1",
//                TaskStatus.DONE,
//                LocalDateTime.of(2000, 10, 10, 3, 10),
//                Duration.ofHours(6));
//
//
//        Epic epic2 = new Epic("Что то 2",
//                "Что то сделать 2");
//
//
//        Subtask subtask3 = new Subtask("Что то 3",
//                "Что то сделать 3",
//                TaskStatus.DONE,
//                LocalDateTime.of(2000, 10, 10, 10, 10),
//                Duration.ofHours(6), 2);
//
//        Subtask subtask4 = new Subtask("Что то 4",
//                "Что то сделать 4",
//                TaskStatus.DONE,
//                LocalDateTime.of(2000, 10, 9, 10, 10),
//                Duration.ofHours(6), 2);
//
//        manager.createTask(task1);
//        manager.createTask(epic2);
//        manager.createTask(subtask3);
//        manager.createTask(subtask4);

        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getMapEpics());
    }
}
