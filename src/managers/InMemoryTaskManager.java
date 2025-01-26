package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int latestId = 1;
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;
    private final Set<Task> prioritizedTasks;

    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();

        this.prioritizedTasks = new TreeSet<>((task1, task2) -> {
            if (task1.getStartTime() == null || task2.getStartTime() == null) {
                return 0;
            }
            int compare = task1.getStartTime().compareTo(task2.getStartTime());
            if (compare == 0) {
                return task1.hashCode() - task2.hashCode(); // Для уникальности элементов
            }
            return compare;
        });
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public void deleteTasks() {
        tasks.values().stream()
                .map(Task::getId)
                .forEach(historyManager::remove);

        tasks.clear();
    }


    @Override
    public void deleteEpics() {
        epics.values().forEach(epic -> {
            epic.getTasks().stream()
                    .map(Subtask::getId)
                    .forEach(historyManager::remove);
            historyManager.remove(epic.getId());
        });

        epics.clear();
        subtasks.clear();
    }


    @Override
    public void deleteSubtasks() {
        epics.values().forEach(epic -> {
            epic.clearTasks();
            epic.verifyStatus();
        });

        subtasks.values().stream()
                .map(Subtask::getId)
                .forEach(historyManager::remove);

        subtasks.clear();
    }


    protected boolean isOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
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
        if (task.getTaskStatus() == null) task.setTaskStatus(TaskStatus.NEW);

        if (task.getStartTime() != null && task.getDuration() != null) {
            if (prioritizedTasks.stream()
                    .anyMatch(existingTask -> isOverlapping(existingTask, task))) {
                System.out.println("Пересечение времени задач");
                return;
            }
        }

        task.setId(latestId++);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic.getTaskStatus() == null) epic.setTaskStatus(TaskStatus.NEW);

        if (epic.getStartTime() != null && epic.getDuration() != null) {
            if (prioritizedTasks.stream()
                    .anyMatch(existingTask -> isOverlapping(existingTask, epic))) {
                System.out.println("Пересечение времени задач");
                return;
            }
        }

        epic.setId(latestId++);
        epics.put(epic.getId(), epic);
        prioritizedTasks.add(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            System.out.println("Эпик не найден");
            return;
        }

        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            if (prioritizedTasks.stream()
                    .filter(existingTask -> !(existingTask instanceof Epic))
                    .anyMatch(existingTask -> isOverlapping(existingTask, subtask))) {
                System.out.println("Пересечение времени задач");
                return;
            }
        }

        subtask.setId(latestId++);
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
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

        epics.get(id).getTasks().stream()
                .map(Subtask::getId)
                .forEach(subtaskId -> {
                    subtasks.remove(subtaskId);
                    historyManager.remove(subtaskId);
                });

        epics.remove(id);
        historyManager.remove(id);
    }


    @Override
    public void deleteSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадача не найдена");
            return;
        }

        subtasks.computeIfPresent(id, (subtaskId, subtask) -> {
            epics.computeIfPresent(subtask.getEpicId(), (epicId, epic) -> {
                epic.deleteTask(subtaskId);
                epic.verifyStatus();
                return epic;
            });
            historyManager.remove(subtaskId);
            return null;
        });
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

    protected Map<Integer, Task> getTasksMap() {
        return tasks;
    }

    protected Map<Integer, Epic> getEpicsMap() {
        return epics;
    }

    protected Map<Integer, Subtask> getSubtasksMap() {
        return subtasks;
    }

    protected Set<Task> getPrioritizedTasksSet() {
        return prioritizedTasks;
    }
}
