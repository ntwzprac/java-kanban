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
}