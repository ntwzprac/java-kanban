package managers;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager loadFromFile(String path) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(path);
        try {
            taskManager = FileBackedTaskManager.loadFromFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return taskManager;
    }
}