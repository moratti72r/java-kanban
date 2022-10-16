package taskmanager;

import historymanager.HistoryManager;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private ArrayList<String> all = new ArrayList<>();
    private ArrayList<String> taskLines = new ArrayList<>();
    private String historyLine;
    private final String file;

    public FileBackedTasksManager(String textFile) throws IOException {
        String fileHistory = "src\\filehistory\\";
        file = fileHistory + textFile;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                all.add(line);
            }
            if (all.size() > 1) {
                separate();
            }
        } catch (IOException e) {
            Files.createFile(Paths.get(file));
        }
    }

    public void separate() {

        if (all.get(all.size() - 2).isBlank()) {
            for (int i = 1; i < all.size() - 2; i++) {
                taskLines.add(all.get(i));
                fromString(all.get(i));
            }
            for (Integer val : historyFromString(all.get(all.size() - 1))) {
                super.getById(val);
            }
        }
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        taskLines.add(task.toString());
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        taskLines.add(subtask.toString());
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        taskLines.add(epic.toString());
        save();
    }

    @Override
    public Task getById(Integer id) {
        Task task = super.getById(id);
        historyLine = historyToString(super.getHistory());
        save();
        return task;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (String task : taskLines) {
                writer.write(task + "\n");
            }
            writer.write("\n");
            if (historyLine != null) {
                writer.write(historyLine);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public Task fromString(String value) {
        String[] split = value.split(",");
        String type = split[1];
        String name = split[2];
        String status = split[3];
        String specification = split[4];
        Integer idEpic = split.length > 5 ? Integer.parseInt(split[5]) : null;
        Task task = null;

        TaskStatus taskStatus = null;
        if (status.equals(TaskStatus.DONE.toString())) {
            taskStatus = TaskStatus.DONE;
        } else if (status.equals(TaskStatus.IS_PROGRESS.toString())) {
            taskStatus = TaskStatus.IS_PROGRESS;
        } else if (status.equals(TaskStatus.NEW.toString())) {
            taskStatus = TaskStatus.NEW;
        }


        if (type.equals(TaskType.EPIC.toString())) {
            task = new Epic(0, name, specification, taskStatus);
            super.createEpic((Epic) task);
        } else if (type.equals(TaskType.SUBTASK.toString())) {
            task = new Subtask(0, name, specification, taskStatus, getMapEpics().get(idEpic));
            super.createSubtask((Subtask) task);
        } else if (type.equals(TaskType.TASK.toString())) {
            task = new Task(0, name, specification, taskStatus);
            super.createTask(task);
        }
        return task;
    }

    public String historyToString(HistoryManager history) {
        String id = "";
        if (!history.getHistory().isEmpty()) {
            for (Task task : history.getHistory()) {
                id = id + "," + (task.getId().toString());
            }
            id = id.substring(1);
        }
        return id;
    }

    public List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (!value.equals("")) {
            String[] val = value.split(",");
            for (String v : val) {
                history.add(Integer.parseInt(v));
            }
        }
        return history;
    }

    public static void main(String[] args) throws IOException {
//        FileBackedTasksManager manager = new FileBackedTasksManager("file1.csv");
//        Epic epic1 = new Epic(0, "Что то 1", "Что то сделать 1", TaskStatus.NEW); //id1
//        manager.createEpic(epic1);
//        manager.createSubtask(new Subtask(0, "Что то 2", "Что то сделать 2", TaskStatus.NEW, epic1));
//        manager.createSubtask(new Subtask(0, "Что то 3", "Что то сделать 3", TaskStatus.DONE, epic1));
//
//        Epic epic2 = new Epic(0, "Что то 4", "Что то сделать 4", TaskStatus.NEW);
//
//        manager.createEpic(epic2);
//        manager.createSubtask(new Subtask(0, "Что то 5", "Что то сделать 5", TaskStatus.DONE, epic2));
//
//
//        manager.createTask(new Task(0, "Что то 6", "Что то сделать 6", TaskStatus.DONE));
//        manager.createTask(new Task(0, "Что то 7", "Что то сделать 7", TaskStatus.DONE));
//        manager.createTask(new Task(0, "Что то 8", "Что то сделать 8", TaskStatus.DONE));
//        manager.createTask(new Task(0, "Что то 9", "Что то сделать 9", TaskStatus.DONE));
//
//        manager.createSubtask(new Subtask(0, "Что то 10", "Что то сделать 10", TaskStatus.NEW, epic2));
//        manager.createSubtask(new Subtask(0, "Что то 11", "Что то сделать 11", TaskStatus.DONE, epic2));
//
//        System.out.println(manager.getMapEpics());
//        System.out.println(manager.getMapSubtasks());
//        System.out.println(manager.getMapTasks());
//
//        manager.getById(1);
//        System.out.println(manager.getHistory().getHistory());
//
//        manager.getById(2);
//        manager.getById(3);
//        System.out.println(manager.getHistory().getHistory());
//        manager.getById(4);
//        manager.getById(5);
//        manager.getById(6);
//        manager.getById(7);
//        manager.getById(8);
//        manager.getById(9);
//        manager.getById(10);
//        manager.getById(5); //11 = 5
//        manager.getById(11);
//
//        System.out.println(manager.getHistory().getHistory());
        FileBackedTasksManager manager = new FileBackedTasksManager("file1.csv");
        System.out.println(manager.getHistory().getHistory());
        System.out.println(manager.getMapTasks());
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getMapEpics());
    }

}
