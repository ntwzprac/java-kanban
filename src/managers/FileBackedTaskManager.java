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
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String savePath;

    public FileBackedTaskManager(String savePath) {
        super();

        this.savePath = savePath;
        try {
            List<String> lines = stringFromFile(savePath);
            if (!lines.isEmpty()) {
                List<Subtask> importedSubtasks = new ArrayList<>();
                List<Task> importedTasks = new ArrayList<>();
                int highestId = 0;

                for (int i = 1; i < lines.size(); i++) {
                    Task task = fromString(lines.get(i));
                    if (task instanceof Epic) {
                        importEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        importedSubtasks.add((Subtask) task);
                    } else {
                        importedTasks.add(task);
                    }
                    if (task.getId() > highestId) {
                        highestId = task.getId();
                    }
                }
                for (Subtask subtask : importedSubtasks) {
                    importSubtask(subtask);
                }
                for (Task task : importedTasks) {
                    importTask(task);
                }
                latestId = highestId;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла: " + e.getMessage());
        }
    }

    private void save() {
        List<Task> allTasks = getTasks();
        allTasks.addAll(getEpics());
        allTasks.addAll(getSubtasks());

        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic\n");
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
        int epicId = 0;
        if (parts.length == 6) {
            epicId = Integer.parseInt(parts[5]);
        }
        Task task = null;

        switch (type) {
            case "TASK":
                task = new Task(name, description, TaskStatus.valueOf(status));
                task.setId(id);
                break;
            case "EPIC":
                task = new Epic(name, description);
                task.setId(id);
                task.setTaskStatus(TaskStatus.valueOf(status));
                break;
            case "SUBTASK":
                task = new Subtask(name, description, TaskStatus.valueOf(status), epicId);
                task.setId(id);
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
        List<Subtask> importedSubtasks = new ArrayList<>();
        List<Task> importedTasks = new ArrayList<>();
        int highestId = 0;

        for (int i = 1; i < lines.size(); i++) {
            Task task = manager.fromString(lines.get(i));
            if (task instanceof Epic) {
                manager.importEpic((Epic) task);
            } else if (task instanceof Subtask) {
                importedSubtasks.add((Subtask) task);
            } else {
                importedTasks.add(task);
            }
            if (task.getId() > highestId) {
                highestId = task.getId();
            }
        }
        for (Subtask subtask : importedSubtasks) {
            manager.importSubtask(subtask);
        }
        for (Task task : importedTasks) {
            manager.importTask(task);
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
    }

    public void importEpic(Epic epic) {
        getEpicsMap().put(epic.getId(), epic);
    }

    public void importSubtask(Subtask subtask) {
        if (!getEpicsMap().containsKey(subtask.getEpicId())) {
            System.out.println("Эпик не найден");
            return;
        }
        getSubtasksMap().put(subtask.getId(), subtask);
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
