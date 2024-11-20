package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>(10);
    }

    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.remove(history.getFirst());
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
