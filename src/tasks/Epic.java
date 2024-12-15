package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> tasks;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        tasks = new ArrayList<>();
    }

    public void addTask(Subtask subtask) {
        tasks.add(subtask);
    }

    public ArrayList<Subtask> getTasks() {
        return tasks;
    }

    public Subtask getTask(int id) {
        for (Subtask subtask : tasks) {
            if (subtask.getId() == id) {
                return subtask;
            }
        }
        return null;
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void verifyStatus() {
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : tasks) {
            if (subtask.getTaskStatus() != TaskStatus.DONE) {
                allDone = false;
            }
            if (subtask.getTaskStatus() != TaskStatus.NEW) {
                allNew = false;
            }
        }

        if (allNew) {
            this.setTaskStatus(TaskStatus.NEW);
        } else if (allDone) {
            this.setTaskStatus(TaskStatus.DONE);
        } else {
            this.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void deleteTask(int id) {
        for (Subtask subtask : tasks) {
            if (subtask.getId() == id) {
                tasks.remove(subtask);
                break;
            }
        }
    }
}
