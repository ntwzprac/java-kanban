package tasks;

public enum TaskStatus {
    NEW,
    IN_PROGRESS,
    DONE;

    @Override
    public String toString() {
        if (this == TaskStatus.NEW) return "Новое";
        else if (this == TaskStatus.IN_PROGRESS) return "В процессе";
        else if (this == TaskStatus.DONE) return "Завершено";
        else return "Статус не найден";
    }
}
