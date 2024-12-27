import managers.TaskManager;
import managers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {
    public static void oldTest(TaskManager taskManager) {
        Task task1 = new Task("Задача1", "Сделать яичницу", TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Вынести мусор", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Уборка дома", "Провести уборку дома");
        Subtask subtask1 = new Subtask("Постирать одежду", "Не забыть повесить на вешалку", TaskStatus.NEW, 3);
        Subtask subtask2 = new Subtask("Помыть пол", "Желательно сделать до обеда", TaskStatus.NEW, 3);

        Epic epic2 = new Epic("Эпик2", "-Эпик2-");
        Subtask subtask3 = new Subtask("Подзадача", "-Подзадача-", TaskStatus.NEW, 6);

        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask3);

        for (Task task : taskManager.getTasks()) {
            System.out.println("> Задача №" + task.getId());
            System.out.println("  Название: " + task.getName());
            System.out.println("  Описание: " + task.getDescription());
            System.out.println("  Статус: " + task.getTaskStatus().toString());
        }

        System.out.println(" ---------- ");

        for (Epic epic : taskManager.getEpics()) {
            System.out.println("> Эпик №" + epic.getId());
            System.out.println("  Название: " + epic.getName());
            System.out.println("  Описание: " + epic.getDescription());
            System.out.println("  Статус: " + epic.getTaskStatus().toString());

            for (Subtask subtask : epic.getTasks()) {
                System.out.println("  > Подзадача №" + subtask.getId());
                System.out.println("    Название: " + subtask.getName());
                System.out.println("    Описание: " + subtask.getDescription());
                System.out.println("    Статус: " + subtask.getTaskStatus().toString());
            }
        }

        System.out.println("\n\n");
        System.out.println(" -- После изменений -- ");
        System.out.println("\n\n");

        task1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);
        task2.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task2);

        subtask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        subtask2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);

        subtask3.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask3);

        for (Task task : taskManager.getTasks()) {
            System.out.println("> Задача №" + task.getId());
            System.out.println("  Название: " + task.getName());
            System.out.println("  Описание: " + task.getDescription());
            System.out.println("  Статус: " + task.getTaskStatus().toString());
        }

        System.out.println(" ---------- ");

        for (Epic epic : taskManager.getEpics()) {
            System.out.println("> Эпик №" + epic.getId());
            System.out.println("  Название: " + epic.getName());
            System.out.println("  Описание: " + epic.getDescription());
            System.out.println("  Статус: " + epic.getTaskStatus().toString());

            for (Subtask subtask : epic.getTasks()) {
                System.out.println("  > Подзадача №" + subtask.getId());
                System.out.println("    Название: " + subtask.getName());
                System.out.println("    Описание: " + subtask.getDescription());
                System.out.println("    Статус: " + subtask.getTaskStatus().toString());
            }
        }

        System.out.println("\n\n");
        System.out.println(" -- После удаления -- ");
        System.out.println("\n\n");

        taskManager.deleteTask(1);
        taskManager.deleteEpic(6);
        taskManager.deleteSubtask(4);

        for (Task task : taskManager.getTasks()) {
            System.out.println("> Задача №" + task.getId());
            System.out.println("  Название: " + task.getName());
            System.out.println("  Описание: " + task.getDescription());
            System.out.println("  Статус: " + task.getTaskStatus().toString());
        }

        System.out.println(" ---------- ");

        for (Epic epic : taskManager.getEpics()) {
            System.out.println("> Эпик №" + epic.getId());
            System.out.println("  Название: " + epic.getName());
            System.out.println("  Описание: " + epic.getDescription());
            System.out.println("  Статус: " + epic.getTaskStatus().toString());

            for (Subtask subtask : epic.getTasks()) {
                System.out.println("  > Подзадача №" + subtask.getId());
                System.out.println("    Название: " + subtask.getName());
                System.out.println("    Описание: " + subtask.getDescription());
                System.out.println("    Статус: " + subtask.getTaskStatus().toString());
            }
        }
    }

    public static void oldTest2(TaskManager taskManager) {
        Task task1 = new Task("Задача1", "Описание задачи1", TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Описание задачи2", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи1", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача2", "Описание подзадачи2", TaskStatus.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача3", "Описание подзадачи3", TaskStatus.NEW, epic1.getId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        taskManager.addEpic(epic2);

        // Проверка списка задач
        System.out.println("\nСписок задач:\n");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task.getName());
        }

        System.out.println("\nСписок эпиков:\n");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic.getName());
            for (Subtask subtask : epic.getTasks()) {
                System.out.println("  " + subtask.getName());
            }
        }

        // Проверка истории
        taskManager.getTask(1);
        taskManager.getSubtask(4);
        taskManager.getSubtask(5);
        taskManager.getEpic(3);
        taskManager.getEpic(7);
        taskManager.getSubtask(4);

        System.out.println("\nИстория:\n");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task.getName());
        }
        /*
        Правильный порядок истории должен быть:
        - Задача1
        - Подзадача2
        - Эпик1
        - Подзадача1
         */

        // Проверка удаления
        taskManager.deleteTask(1);

        System.out.println("\nЗадачи:\n");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task.getName());
        }

        // Проверка истории после удаления задачи
        System.out.println("\nИстория:\n");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task.getName());
        }

        // Проверка истории после удаления эпика
        taskManager.deleteEpic(3);
        System.out.println("\nИстория:\n");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task.getName());
        }
    }

    public static void main(String[] args) {
//        TaskManager taskManager = Managers.getDefault();
//        oldTest(taskManager);
//        oldTest2(taskManager);

        TaskManager taskManager = Managers.loadFromFile("./save.csv");

        /* Раскоментировать, если необходимо восстановить данные для теста ( к примеру, если файл save.csv был очищен )
        Task task1 = new Task("Задача1", "Описание задачи1", TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Описание задачи2", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи1", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача2", "Описание подзадачи2", TaskStatus.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача3", "Описание подзадачи3", TaskStatus.NEW, epic1.getId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        taskManager.addEpic(epic2);
         */

        System.out.println("\nСписок задач:\n");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task.getId() + " - " + task.getName());
        }

        System.out.println("\nСписок эпиков:\n");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic.getId() + " - " + epic.getName());
            for (Subtask subtask : epic.getTasks()) {
                System.out.println("  " + subtask.getId() + " - " + subtask.getName());
            }
        }
//        oldTest(taskManager);
    }
}