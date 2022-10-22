package taskmanager;

import historymanager.HistoryManager;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String textFile;

    private final TaskSerializer taskSerializer = new CsvTaskSerializer();


    public FileBackedTasksManager(String textFile) throws IOException {
        this.textFile = textFile;

        ArrayList<String> allTasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            while (br.ready()) {
                String line = br.readLine();
                allTasks.add(line);
            }
            if (allTasks.size() > 1) {
                separate(allTasks);
            }
        } catch (IOException e) {
            Files.createFile(Paths.get(textFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void separate(ArrayList<String> allTasks) {
        if (allTasks.get(allTasks.size() - 2).isBlank()) {
            for (int i = 1; i < allTasks.size() - 2; i++) {
                Task task = taskSerializer.deserialize(allTasks.get(i));
                setIdTask(task.getId());
                super.createTask(task);
            }
            setIdTask(taskSerializer.getMaxId());

            String historyLine = allTasks.get(allTasks.size() - 1);

            for (Integer val : historyFromString(historyLine)) {
                getById(val);
            }
        } else {
            for (int i = 1; i < allTasks.size(); i++) {
                super.createTask(taskSerializer.deserialize(allTasks.get(i)));
            }
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(textFile)) {
            writer.write(taskSerializer.getHeadLine());
            for (Task task : getMapTasks().values()) {
                writer.write(taskSerializer.serialize(task) + "\n");
            }
            for (Epic epic : getMapEpics().values()) {
                writer.write(taskSerializer.serialize(epic) + "\n");
            }
            for (Subtask subtask : getMapSubtasks().values()) {
                writer.write(taskSerializer.serialize(subtask) + "\n");
            }
            if (historyToString(super.getHistory()) != null) {
                writer.write("\n");
                writer.write(historyToString(super.getHistory()));
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public String historyToString(HistoryManager history) {
        String historyLine = "";
        if (!history.getHistory().isEmpty()) {
            for (Task task : history.getHistory()) {
                historyLine = historyLine + "," + (task.getId().toString());
            }
            historyLine = historyLine.substring(1);

        }
        return historyLine;
    }

    public List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (value != null) {
            if (!value.equals("")) {
                String[] val = value.split(",");
                for (String v : val) {
                    history.add(Integer.parseInt(v));
                }
            }
        }
        return history;
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }


    @Override
    public Task getById(Integer id) {
        Task task = super.getById(id);
        save();
        return task;
    }

    @Override
    public void removeById(Integer id) {
        super.removeById(id);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeEpic() {
        super.removeEpic();
        save();
    }

    @Override
    public void upDateTask(Task task) {
        super.upDateTask(task);
        save();
    }

    public static void main(String[] args) throws IOException {

        InMemoryTaskManager manager = new FileBackedTasksManager("src\\resources\\file1.csv");

//        Epic epic1 = new Epic(0, "Что то 1", "Что то сделать 1", TaskStatus.NEW); //id1
//        manager.createTask(epic1);
//        manager.createTask(new Subtask(0, "Что то 2", "Что то сделать 2", TaskStatus.NEW, 1));
//        manager.createTask(new Subtask(0, "Что то 3", "Что то сделать 3", TaskStatus.DONE, 1));
//
//        Epic epic2 = new Epic(0, "Что то 4", "Что то сделать 4", TaskStatus.NEW);
//        manager.createTask(epic2);
//        manager.createTask(new Subtask(0, "Что то 5", "Что то сделать 5", TaskStatus.DONE, 4));
//        manager.createTask(new Task(0, "Что то 6", "Что то сделать 6", TaskStatus.DONE));
//        manager.createTask(new Task(0, "Что то 7", "Что то сделать 7", TaskStatus.DONE));
//        manager.createTask(new Task(0, "Что то 8", "Что то сделать 8", TaskStatus.DONE));
//        manager.createTask(new Task(0, "Что то 9", "Что то сделать 9", TaskStatus.DONE));
//        manager.createTask(new Subtask(0, "Что то 10", "Что то сделать 10", TaskStatus.NEW, 4));
//        manager.createTask(new Subtask(0, "Что то 11", "Что то сделать 11", TaskStatus.DONE, 4));
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

        System.out.println(manager.getHistory().getHistory());
        System.out.println(manager.getMapTasks());
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getMapEpics());

        manager.createTask(new Task(0, "Что то 9", "Что то сделать 9", TaskStatus.DONE));
        System.out.println(manager.getMapTasks());
        manager.createTask(new Task(0, "Что то 10", "Что то сделать 9", TaskStatus.DONE));

        manager.removeById(3);
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getHistory().getHistory());

        manager.removeSubtasks();
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getHistory().getHistory());

    }

}
