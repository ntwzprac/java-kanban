package Managers;

import Tasks.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
