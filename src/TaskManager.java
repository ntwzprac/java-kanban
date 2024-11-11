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
    private HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
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
    }

    public void deleteEpics() {
        epics.clear();
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearTasks();
            epic.verifyStatus();
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

        if (task.getTaskStatus() == null) task.setTaskStatus(TaskStatus.NEW);

        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(latestId++);

        if (epic.getTaskStatus() == null) epic.setTaskStatus(TaskStatus.NEW);

        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            System.out.println("Эпик не найден");
            return;
        }
        subtask.setId(latestId++);
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addTask(subtask);
        epics.get(subtask.getEpicId()).verifyStatus();
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(int id, String name, String description) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epic.setName(name);
            epic.setDescription(description);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            int epicId = subtasks.get(subtask.getId()).getEpicId();
            Epic epic = epics.get(epicId);
            if (epicId == subtask.getEpicId()) {
                subtasks.put(subtask.getId(), subtask);
                ArrayList<Subtask> tasks = epic.getTasks();
                if (tasks.contains(subtask)) {
                    tasks.set(tasks.indexOf(subtask), subtask);
                }
                epic.verifyStatus();
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

    public ArrayList<Subtask> getEpicSubtasks(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик не найден");
            return null;
        }
        return epics.get(id).getTasks();
    }
}
