package taskmanager;

import historymanager.CustomLinkedList;
import historymanager.HistoryManager;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final CustomLinkedList tasks = new CustomLinkedList();
    private String historyLine;
    private final String textFile;

    private final TaskSerializer taskSerializer = new CsvTaskSerializer(this);


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
                createTask(taskSerializer.deserialize(allTasks.get(i)));
            }

            historyLine = allTasks.get(allTasks.size() - 1);

            for (Integer val : historyFromString(historyLine)) {
                getById(val);
            }
        } else {
            for (int i = 1; i < allTasks.size(); i++) {
                createTask(taskSerializer.deserialize(allTasks.get(i)));
            }
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(textFile)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : tasks.getTasks()) {
                writer.write(taskSerializer.serialize(task) + "\n");
            }
            if (historyLine != null) {
                writer.write("\n");
                writer.write(historyLine);
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
        if (value != null) {
            List<Integer> history = new ArrayList<>();
            if (!value.equals("")) {
                String[] val = value.split(",");
                for (String v : val) {
                    history.add(Integer.parseInt(v));
                }
            }
            return history;
        } else return null;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        tasks.linkLast(task);
        save();
    }


    @Override
    public Task getById(Integer id) {
        Task task = super.getById(id);
        historyLine = historyToString(super.getHistory());
        save();
        return task;
    }

    @Override
    public void removeById(Integer id) {
        super.removeById(id);
        tasks.removeNode(id);
        historyLine = historyToString(super.getHistory());
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        for (Task task : tasks.getTasks()) {
            if (task.getType() == TaskType.TASK) {
                tasks.removeNode(task.getId());
            }
        }
        historyLine = historyToString(super.getHistory());
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        for (Task task : tasks.getTasks()) {
            if (task.getType() == TaskType.SUBTASK) {
                tasks.removeNode(task.getId());
            }
        }
        historyLine = historyToString(super.getHistory());
        save();
    }

    @Override
    public void removeEpic() {
        super.removeEpic();
        for (Task task : tasks.getTasks()) {
            if (task.getType() == TaskType.EPIC) {
                tasks.removeNode(task.getId());
            }
        }
        historyLine = historyToString(super.getHistory());
        save();
    }

    @Override
    public void upDateTask(Task task) {
        super.upDateTask(task);
        tasks.removeNode(task.getId());
        tasks.linkLast(task);
        save();
    }

    public static void main(String[] args) throws IOException {

        InMemoryTaskManager manager = new FileBackedTasksManager("src\\resources\\file1.csv");
        System.out.println(manager.getHistory().getHistory());
        System.out.println(manager.getMapTasks());
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getMapEpics());

        manager.removeById(3);
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getHistory().getHistory());

        manager.removeSubtasks();
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getHistory().getHistory());

    }

}
