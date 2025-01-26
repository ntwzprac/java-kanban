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

    public void updateAttributes() {
        LocalDateTime startTime = null;
        for (Subtask subtask : tasks) {
            if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
        }

        Duration duration = Duration.ZERO;
        for (Subtask subtask : tasks) {
            if (subtask.getDuration() != null)
                duration = duration.plus(subtask.getDuration());
        }

        if (!tasks.isEmpty())
            this.setStartTime(tasks.getFirst().getStartTime());

        if (duration != Duration.ZERO)
            this.setDuration(duration);
        else
            this.setDuration(null);

        LocalDateTime endTime = null;
        for (Subtask subtask : tasks) {
            if (subtask.getStartTime() == null || subtask.getEndTime() == null) continue;

            if (endTime == null || subtask.getEndTime().isAfter(endTime)) {
                if (subtask.getEndTime() != null) endTime = subtask.getEndTime();
            }
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

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", getId(), "EPIC", getName(), getTaskStatus().toDetailedString(), getDescription(), "", getStartTime(), getDuration());
    }
}
