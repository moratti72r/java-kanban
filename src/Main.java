import historymanager.HistoryManager;
import task.Epic;
import task.Subtask;
import task.TaskStatus;
import taskmanager.Manager;
import taskmanager.Managers;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = Managers.getDefault();
        HistoryManager history = Managers.getDefaultHistory();

        Epic epic1 = new Epic(0,"Автомобиль","Подготовить к поездке", TaskStatus.NEW);
        manager.createEpic(epic1);
        manager.createSubtask(new Subtask(0,"Топливо","Заправить полный бак бензина",TaskStatus.NEW,epic1));
        manager.createSubtask(new Subtask(0,"Шины","Проверить давление в шинах",TaskStatus.DONE,epic1));

        Epic epic2 = new Epic(0,"Техническое задание","Проверить техническое задание в классе Main", TaskStatus.NEW);
        manager.createEpic(epic2);
        manager.createSubtask(new Subtask(0,"Коммит","Правильно написать коммит при отправке",TaskStatus.DONE,epic2));

        System.out.println(manager.getMapEpics());
        System.out.println(manager.getMapSubtasks());
        System.out.println(manager.getMapTasks());

        manager.getById(2); //1
        System.out.println(history.getHistory());

        manager.getById(3); //2
        manager.getById(2); //3
        manager.getById(3); //4
        manager.getById(2); //5
        manager.getById(3); //6
        manager.getById(2); //7
        manager.getById(3); //8
        manager.getById(2); //9
        manager.getById(3); //10
        manager.getById(5); //11

        System.out.println(history.getHistory());
    }
}
