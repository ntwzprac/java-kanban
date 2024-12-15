package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int latestId = 1;
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;

    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();

        for (Task task : tasks.values()) {
            allTasks.add(task);
        }

        return allTasks;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();

        for (Epic epic : epics.values()) {
            allEpics.add(epic);
        }

        return allEpics;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();

        for (Subtask subtask : subtasks.values()) {
            allSubtasks.add(subtask);
        }

        return allSubtasks;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearTasks();
            epic.verifyStatus();
        }
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача не найдена");
            return null;
        }

        historyManager.add(tasks.get(id));

        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик не найден");
            return null;
        }

        historyManager.add(epics.get(id));

        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадача не найдена");
            return null;
        }

        historyManager.add(subtasks.get(id));

        return subtasks.get(id);
    }

    @Override
    public void addTask(Task task) {
        task.setId(latestId++);

        if (task.getTaskStatus() == null) task.setTaskStatus(TaskStatus.NEW);

        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(latestId++);

        if (epic.getTaskStatus() == null) epic.setTaskStatus(TaskStatus.NEW);

        epics.put(epic.getId(), epic);
    }

    @Override
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

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            oldEpic.setName(epic.getName());
            oldEpic.setDescription(epic.getDescription());
        }
    }

    @Override
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

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик не найден");
            return;
        }
        Epic epic = epics.get(id);
        for (Subtask subtask : epic.getTasks()) {
            subtasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадача не найдена");
            return;
        }
        int epicId = subtasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);

        epic.deleteTask(id);
        subtasks.remove(id);
        epic.verifyStatus();
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик не найден");
            return null;
        }
        return epics.get(id).getTasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
