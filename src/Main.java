import task.Epic;
import task.Subtask;
import task.TaskStatus;
import taskmanager.Manager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();

        Epic epic1 = new Epic(0,"Автомобиль","Подготовить к поездке", TaskStatus.getNEW());
        manager.createEpic(epic1);
        manager.createSubtask(new Subtask(0,"Топливо","Заправить полный бак бензина",TaskStatus.getNEW(),epic1));
        manager.createSubtask(new Subtask(0,"Шины","Проверить давление в шинах",TaskStatus.getDONE(),epic1));

        Epic epic2 = new Epic(0,"Техническое задание","Проверить техническое задание в классе Main", TaskStatus.getNEW());
        manager.createEpic(epic2);
        manager.createSubtask(new Subtask(0,"Коммит","Правильно написать коммит при отправке",TaskStatus.getNEW(),epic2));

        System.out.println(manager.getMapEpics());
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getMapTasks());

        manager.upDateSubtask(new Subtask(2,"Топливо","Заправить полный бак бензина",TaskStatus.getDONE(),epic1));
        System.out.println(manager.getMapEpics());
        System.out.println(manager.getMapSubtasks());

        manager.removeById(2);
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getMapEpics());

        manager.removeById(1);
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getMapEpics());
    }
}
