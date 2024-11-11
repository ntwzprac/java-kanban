import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int latestId = 1;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();

        for (Task task : tasks.values()) {
            allTasks.add(task);
        }

        return allTasks;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();

        for (Epic epic : epics.values()) {
            allEpics.add(epic);
        }

        return allEpics;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();

        for (Epic epic : epics.values()) {
            allSubtasks.addAll(epic.getTasks());
        }

        return allSubtasks;
    }

    public void deleteTasks() {
        tasks.clear();
        if (epics.isEmpty()) {
            latestId = 1;
        }
    }

    public void deleteEpics() {
        epics.clear();
        if (tasks.isEmpty()) {
            latestId = 1;
        }
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearTasks();
        }

        if (epics.isEmpty() && tasks.isEmpty()) {
            latestId = 1;
        }
    }

    public Task getTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача не найдена");
            return null;
        }

        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик не найден");
            return null;
        }

        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getTasks()) {
                if (subtask.getId() == id) {
                    return subtask;
                }
            }
        }
        System.out.println("Подзадача не найдена");
        return null;
    }

    public void addTask(Task task) {
        task.setId(latestId++);

        if (task.taskStatus == null) task.taskStatus = TaskStatus.NEW;

        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(latestId++);

        if (epic.taskStatus == null) epic.taskStatus = TaskStatus.NEW;

        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(latestId++);
        subtask.getEpic().addTask(subtask);

        if (subtask.taskStatus == null) subtask.taskStatus = TaskStatus.NEW;

        subtask.verifyEpicStatus();
    }

    public void updateTask(Task task) {
        for (Task t : tasks.values()) {
            if (t.getId() == task.getId()) {
                t.name = task.name;
                t.description = task.description;
                t.taskStatus = task.taskStatus;
            }
        }
    }

    public void updateEpic(Epic epic) {
        for (Epic e : epics.values()) {
            if (e.getId() == epic.getId()) {
                e.name = epic.name;
                e.description = epic.description;
                e.tasks = epic.tasks;

                // done checker
                boolean allNew = true;
                boolean allDone = true;

                if (tasks.isEmpty()) e.taskStatus = TaskStatus.NEW;

                for (Subtask subtask : e.tasks) {
                    if (subtask.taskStatus != TaskStatus.DONE) allDone = false;
                    if (subtask.taskStatus != TaskStatus.NEW) allNew = false;
                }

                if (allDone) e.taskStatus = TaskStatus.DONE;
                else if (allNew) e.taskStatus = TaskStatus.NEW;
                else e.taskStatus = TaskStatus.IN_PROGRESS;
            }
        }
    }

    public void updateSubtask(Subtask subtask) {
        for (Epic epic : epics.values()) {
            for (Subtask s : epic.getTasks()) {
                if (s.getId() == subtask.getId()) {
                    s.name = subtask.name;
                    s.description = subtask.description;
                    s.taskStatus = subtask.taskStatus;

                    s.verifyEpicStatus();
                }
            }
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }

    public void deleteSubtask(Epic epic, int id) {
        for (Subtask subtask : epic.getTasks()) {
            if (subtask.getId() == id) {
                epic.getTasks().remove(subtask);
                break;
            }
        }
        epic.verifyStatus();
    }

    public void getEpicSubtasks(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик не найден");
            return;
        }

        Epic epic = epics.get(id);
        for (Subtask subtask : epic.getTasks()) {
            System.out.println("> Подзадача №" + subtask.getId());
            System.out.println("Название: " + subtask.name);
            System.out.println("  Описание: " + subtask.description);
            System.out.println("  Статус: " + subtask.taskStatus);
        }
    }
}
