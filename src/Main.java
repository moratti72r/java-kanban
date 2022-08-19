public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();

        Epic epic1 = new Epic(0, "Автомобиль",
                "Починить автомобиль", manager.NEW);
        Subtask subtask1 = new Subtask(0, "Аккумулятор",
                "Зарядить или заменить аккумулятор на автомобиле",
                manager.NEW, epic1);
        Subtask subtask2 = new Subtask(0, "Колеса",
                "Проверить давление в шинах",
                manager.NEW, epic1);

        Epic epic2 = new Epic(0, "Здоровье", "Заняться здоровьем", manager.NEW);
        Subtask subtask3 = new Subtask(0, "Нервы",
                "Выпить пиво, чтобы не нервничать при решении данной задачи" +
                        "И отправить задачу на проверку для получение подсказок",
                manager.NEW, epic2);

        manager.createTask(epic1);
        manager.createTask(subtask1);
        manager.createTask(subtask2);
        manager.createTask(epic2);
        manager.createTask(subtask3);

        System.out.println(manager.getTasks(manager.mapTasks));
        System.out.println(manager.getTasks(manager.mapSubtasks));
        System.out.println(manager.getTasks(manager.mapEpics));


    }
}
