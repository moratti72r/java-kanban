package taskmanager;

import historymanager.HistoryManager;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksTaskManager extends InMemoryTaskTaskManager {

    private String textFile;

    private final TaskSerializer taskSerializer = new CsvTaskSerializer();

    protected FileBackedTasksTaskManager(){}
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


    private void separate(ArrayList<String> allTasks) {
        Integer idMax = 0;
        if (allTasks.get(allTasks.size() - 2).isBlank()) {
            for (int i = 1; i < allTasks.size() - 2; i++) {
                Task task = taskSerializer.deserialize(allTasks.get(i));
                idMax = idMax < task.getId() ? task.getId() : idMax;
                setIdTaskGenerator(task.getId());
                super.createTask(task);
            }
            setIdTaskGenerator(idMax);

            String historyLine = allTasks.get(allTasks.size() - 1);

            for (Integer val : historyFromString(historyLine)) {
                getById(val);
            }
        } else {
            for (int i = 1; i < allTasks.size(); i++) {
                Task task = taskSerializer.deserialize(allTasks.get(i));
                idMax = idMax < task.getId() ? task.getId() : idMax;
                setIdTaskGenerator(task.getId());
                super.createTask(task);
            }
            setIdTaskGenerator(idMax);
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
            if (!super.getHistory().getHistory().isEmpty()) {
                writer.write("\n");
                writer.write(historyToString(super.getHistory()));
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private String historyToString(HistoryManager history) {

        return history.getHistory().stream().map(Task::getId).map(String::valueOf).collect(Collectors.joining(","));
    }

    private List<Integer> historyFromString(String value) {
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

    @Override
    public Integer createTask(Task task) {
        super.createTask(task);
        save();
        return idTaskGenerator;
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