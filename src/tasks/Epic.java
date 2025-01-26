package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
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

        updateDuration();
    }

    public void updateDuration() {
        Duration duration = Duration.ZERO;
        for (Subtask subtask : tasks) {
            if (subtask.getDuration() != null)
                duration = duration.plus(subtask.getDuration());
        }

        if (!tasks.isEmpty())
            this.setStartTime(tasks.getFirst().getStartTime());

        this.setDuration(duration);
    }

    @Override
    public LocalDateTime getStartTime() {
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.getFirst().getStartTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.getLast().getEndTime();
    }

    public void deleteTask(int id) {
        for (Subtask subtask : tasks) {
            if (subtask.getId() == id) {
                tasks.remove(subtask);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", getId(), "EPIC", getName(), getTaskStatus().toDetailedString(), getDescription(), "", getStartTime(), getDuration());
    }
}
