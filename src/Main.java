import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Задача1", "Сделать яичницу", TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Вынести мусор", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Уборка дома", "Провести уборку дома", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("Постирать одежду", "Не забыть повесить на вешалку", TaskStatus.NEW, epic1);
        Subtask subtask2 = new Subtask("Помыть пол", "Желательно сделать до обеда", TaskStatus.NEW, epic1);

        Epic epic2 = new Epic("Эпик2", "-Эпик2-", TaskStatus.NEW);
        Subtask subtask3 = new Subtask("Подзадача", "-Подзадача-", TaskStatus.NEW, epic2);

        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask3);

        for (Task task : taskManager.getTasks()) {
            System.out.println("> Задача №" + task.getId());
            System.out.println("  Название: " + task.name);
            System.out.println("  Описание: " + task.description);
            System.out.println("  Статус: " + task.taskStatus.toString());
        }

        System.out.println(" ---------- ");

        for (Epic epic : taskManager.getEpics()) {
            System.out.println("> Эпик №" + epic.getId());
            System.out.println("  Название: " + epic.name);
            System.out.println("  Описание: " + epic.description);
            System.out.println("  Статус: " + epic.taskStatus.toString());

            for (Subtask subtask : epic.getTasks()) {
                System.out.println("  > Подзадача №" + subtask.getId());
                System.out.println("    Название: " + subtask.name);
                System.out.println("    Описание: " + subtask.description);
                System.out.println("    Статус: " + subtask.taskStatus.toString());
            }
        }

        System.out.println("\n\n");
        System.out.println(" -- После изменений -- ");
        System.out.println("\n\n");

        task1.taskStatus = TaskStatus.DONE;
        taskManager.updateTask(task1);
        task2.taskStatus = TaskStatus.IN_PROGRESS;
        taskManager.updateTask(task2);

        subtask1.taskStatus = TaskStatus.IN_PROGRESS;
        taskManager.updateSubtask(subtask1);
        subtask2.taskStatus = TaskStatus.DONE;
        taskManager.updateSubtask(subtask2);

        subtask3.taskStatus = TaskStatus.DONE;
        taskManager.updateSubtask(subtask3);

        for (Task task : taskManager.getTasks()) {
            System.out.println("> Задача №" + task.getId());
            System.out.println("  Название: " + task.name);
            System.out.println("  Описание: " + task.description);
            System.out.println("  Статус: " + task.taskStatus.toString());
        }

        System.out.println(" ---------- ");

        for (Epic epic : taskManager.getEpics()) {
            System.out.println("> Эпик №" + epic.getId());
            System.out.println("  Название: " + epic.name);
            System.out.println("  Описание: " + epic.description);
            System.out.println("  Статус: " + epic.taskStatus.toString());

            for (Subtask subtask : epic.getTasks()) {
                System.out.println("  > Подзадача №" + subtask.getId());
                System.out.println("    Название: " + subtask.name);
                System.out.println("    Описание: " + subtask.description);
                System.out.println("    Статус: " + subtask.taskStatus.toString());
            }
        }

        System.out.println("\n\n");
        System.out.println(" -- После удаления -- ");
        System.out.println("\n\n");

        taskManager.deleteTask(1);
        taskManager.deleteEpic(6);
        taskManager.deleteSubtask(epic1, 1);

        for (Task task : taskManager.getTasks()) {
            System.out.println("> Задача №" + task.getId());
            System.out.println("  Название: " + task.name);
            System.out.println("  Описание: " + task.description);
            System.out.println("  Статус: " + task.taskStatus.toString());
        }

        System.out.println(" ---------- ");

        for (Epic epic : taskManager.getEpics()) {
            System.out.println("> Эпик №" + epic.getId());
            System.out.println("  Название: " + epic.name);
            System.out.println("  Описание: " + epic.description);
            System.out.println("  Статус: " + epic.taskStatus.toString());

            for (Subtask subtask : epic.getTasks()) {
                System.out.println("  > Подзадача №" + subtask.getId());
                System.out.println("    Название: " + subtask.name);
                System.out.println("    Описание: " + subtask.description);
                System.out.println("    Статус: " + subtask.taskStatus.toString());
            }
        }
    }
}