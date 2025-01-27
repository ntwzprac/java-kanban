package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = null;
        this.duration = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) return startTime.plus(duration);
        else return null;
    }

    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", getId(), "TASK", getName(), getTaskStatus().toDetailedString(), getDescription(), "", getStartTime(), getDuration());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
