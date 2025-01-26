package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import util.ManagerSaveException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String savePath;

    public FileBackedTaskManager(String savePath) {
        super();

        this.savePath = savePath;
    }

    private void save() {
        List<Task> allTasks = getTasks();
        allTasks.addAll(getEpics());
        allTasks.addAll(getSubtasks());

        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic,startTime,duration\n");
        for (Task task : allTasks) {
            sb.append(task.toString());
            sb.append("\n");
        }

        try {
            Files.writeString(Path.of(savePath), sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    private Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];
        String epic = parts[5];
        String startTime = parts[6];
        String duration = parts[7];
        int epicId = 0;
        if (!epic.isBlank()) {
            epicId = Integer.parseInt(epic);
        }
        Task task = null;

        switch (type) {
            case "TASK":
                task = new Task(name, description, TaskStatus.valueOf(status));
                task.setId(id);
                if (!startTime.equals("null")) task.setStartTime(LocalDateTime.parse(startTime));
                else task.setStartTime(null);

                if (!duration.equals("null")) task.setDuration(Duration.parse(duration));
                else task.setDuration(null);

                break;
            case "EPIC":
                task = new Epic(name, description);
                task.setId(id);
                task.setTaskStatus(TaskStatus.valueOf(status));
                break;
            case "SUBTASK":
                task = new Subtask(name, description, TaskStatus.valueOf(status), epicId);
                task.setId(id);
                if (!startTime.equals("null")) task.setStartTime(LocalDateTime.parse(startTime));
                else task.setStartTime(null);

                if (!duration.equals("null")) task.setDuration(Duration.parse(duration));
                else task.setDuration(null);
                break;
        }

        return task;
    }

    private List<String> stringFromFile(String pathString) throws IOException {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(pathString);
        if (Files.exists(path)) {
            lines = Files.readAllLines(path);
        }

        return lines;
    }

    public static FileBackedTaskManager loadFromFile(String path) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(path);
        List<String> lines = manager.stringFromFile(path);
        int highestId = 0;

        for (int i = 1; i < lines.size(); i++) {
            Task task = manager.fromString(lines.get(i));
            if (task instanceof Epic) {
                manager.importEpic((Epic) task);
            } else if (task instanceof Subtask) {
                manager.importSubtask((Subtask) task);
                Epic epic = manager.getEpicsMap().get(((Subtask) task).getEpicId());
                epic.updateDuration();
            } else {
                manager.importTask(task);
            }
            if (task.getId() > highestId) {
                highestId = task.getId();
            }
        }


        manager.latestId = highestId;
        return manager;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    public void importTask(Task task) {
        getTasksMap().put(task.getId(), task);
        getPrioritizedTasksSet().add(task);
    }

    public void importEpic(Epic epic) {
        getEpicsMap().put(epic.getId(), epic);
        getPrioritizedTasksSet().add(epic);
    }

    public void importSubtask(Subtask subtask) {
        if (!getEpicsMap().containsKey(subtask.getEpicId())) {
            System.out.println("Эпик не найден");
            return;
        }
        getSubtasksMap().put(subtask.getId(), subtask);
        getPrioritizedTasksSet().add(subtask);
        getEpicsMap().get(subtask.getEpicId()).addTask(subtask);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }
}
