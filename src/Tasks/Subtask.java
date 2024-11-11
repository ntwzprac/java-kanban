package Tasks;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, TaskStatus taskStatus, Epic epic) {
        super(name, description, taskStatus);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public void verifyEpicStatus() {
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : epic.getTasks()) {
            if (subtask.taskStatus != TaskStatus.DONE) allDone = false;
            if (subtask.taskStatus != TaskStatus.NEW) allNew = false;
        }

        if (allDone) epic.taskStatus = TaskStatus.DONE;
        else if (allNew) epic.taskStatus = TaskStatus.NEW;
        else epic.taskStatus = TaskStatus.IN_PROGRESS;
    }
}
