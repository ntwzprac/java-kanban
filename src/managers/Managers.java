package managers;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager loadFromFile(String path) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(path);
        return taskManager;
    }
}