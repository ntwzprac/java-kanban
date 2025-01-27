package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> tasks;
    private LocalDateTime endTime;

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

        updateAttributes();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateAttributes() {
        Duration totalDuration = Duration.ZERO;
        LocalDateTime actualStartTime = null;
        LocalDateTime actualEndTime = null;

        for (Subtask subtask : tasks) {
            if (subtask.getStartTime() != null) {
                if (actualStartTime == null || subtask.getStartTime().isBefore(actualStartTime)) {
                    actualStartTime = subtask.getStartTime();
                }
            }

            if (subtask.getDuration() != null) {
                totalDuration = totalDuration.plus(subtask.getDuration());
            }

            if (subtask.getStartTime() == null || subtask.getEndTime() == null) continue;

            if (actualEndTime == null || subtask.getEndTime().isAfter(actualEndTime)) {
                actualEndTime = subtask.getEndTime();
            }
        }

        if (actualStartTime != null)
            this.setStartTime(actualStartTime);

        if (totalDuration != Duration.ZERO)
            this.setDuration(totalDuration);

        if (actualEndTime != null)
            this.setEndTime(actualEndTime);
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
