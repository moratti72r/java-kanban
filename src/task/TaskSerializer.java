package task;


public interface TaskSerializer {
    Task deserialize (String line);
    String serialize (Task task);
    String getHeadLine();
}
