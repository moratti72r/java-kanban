package taskmanager;

import historymanager.HistoryManager;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksTaskManager extends InMemoryTaskTaskManager {

    private final String textFile;

    private final TaskSerializer taskSerializer = new CsvTaskSerializer();


    public FileBackedTasksTaskManager(String textFile) throws IOException {
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
            Integer idMax = 0;
            for (int i = 1; i < allTasks.size() - 2; i++) {
                Task task = taskSerializer.deserialize(allTasks.get(i));
                idMax = idMax < task.getId() ? task.getId() : idMax;
                setIdTaskGenerator(task.getId() - 1);
                super.createTask(task);
            }
            setIdTaskGenerator(idMax);

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

    public void setIdTaskGenerator(Integer id) {
        idTaskGenerator = id;
    }

    public Integer getIdTaskGenerator() {
        return idTaskGenerator;
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
}