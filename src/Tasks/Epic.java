package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private int latestSubtaskId = 1;
    public ArrayList<Subtask> tasks =  new ArrayList<>();

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public void addTask(Subtask subtask) {
        subtask.setId(latestSubtaskId++);
        subtask.setEpic(this);
        if (subtask.taskStatus == null) subtask.taskStatus  = TaskStatus.NEW;

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
        latestSubtaskId = 1;
    }

    public void verifyStatus() {
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : tasks) {
            if (subtask.taskStatus != TaskStatus.DONE) {
                allDone = false;
            }
            if (subtask.taskStatus != TaskStatus.NEW) {
                allNew = false;
            }
        }

        if (allDone) {
            this.taskStatus = TaskStatus.DONE;
        } else if (allNew) {
            this.taskStatus = TaskStatus.NEW;
        } else {
            this.taskStatus = TaskStatus.IN_PROGRESS;
        }
    }
}
