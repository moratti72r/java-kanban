//import historymanager.HistoryManager;
//import task.Epic;
//import task.Subtask;
//import task.Task;
//import task.TaskStatus;
//import taskmanager.Manager;
//import taskmanager.Managers;
//
//public class Main {
//
//    public static void main(String[] args) {
//        System.out.println("Поехали!");
//        Manager manager = Managers.getDefault();
//        HistoryManager history = Managers.getDefaultHistory();
//
//        Epic epic1 = new Epic(0, "Автомобиль", "Подготовить к поездке", TaskStatus.NEW); //id1
//        manager.createEpic(epic1);
//        manager.createSubtask(new Subtask(0, "Топливо", "Заправить полный бак бензина", TaskStatus.NEW, epic1)); //id2
//        manager.createSubtask(new Subtask(0, "Шины", "Проверить давление в шинах", TaskStatus.DONE, epic1));//id3
//
//        Epic epic2 = new Epic(0, "Техническое задание", "Проверить техническое задание в классе Main", TaskStatus.NEW);
//        //id4
//        manager.createEpic(epic2);
//        manager.createSubtask(new Subtask(0, "Коммит", "Правильно написать коммит при отправке", TaskStatus.DONE, epic2));
//        //id5
//
//        manager.createTask(new Task(0, "Что то там 6", "Что там сделать 6", TaskStatus.DONE));//id6
//        manager.createTask(new Task(0, "Что то там 7", "Что там сделать 7", TaskStatus.DONE));//id7
//        manager.createTask(new Task(0, "Что то там 8", "Что там сделать 8", TaskStatus.DONE));//id8
//        manager.createTask(new Task(0, "Что то там 9", "Что там сделать 9", TaskStatus.DONE));//id9
//
//        manager.createSubtask(new Subtask(0, "Что там 10", "Что там сделать 10", TaskStatus.NEW, epic2));//10
//        manager.createSubtask(new Subtask(0, "Что то 11", "Что то 11", TaskStatus.DONE, epic2));//11
//
//        System.out.println(manager.getMapEpics());
//        System.out.println(manager.getMapSubtasks());
//        System.out.println(manager.getMapTasks());
//        history.remove(2);
//        manager.getById(1); //1
//        System.out.println(history.getHistory());
//
//        manager.getById(2); //2
//        manager.getById(3); //3
//        System.out.println(history.getHistory());
//        manager.getById(4); //4
//        manager.getById(5); //5
//        manager.getById(6); //6
//        manager.getById(7); //7
//        manager.getById(8); //8
//        manager.getById(9); //9
//        manager.getById(10); //10
//        manager.getById(5); //11 = 5
//        manager.getById(11);//12
//
//        System.out.println(history.getHistory());
//        history.remove(4);
//        history.remove(2);
//
//        System.out.println(history.getHistory());
//    }
//}
