package managers;

import tasks.Task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private TasksLinkedList<Task> history;
    private Map<Integer, Node<Task>> nodeMap;

    public InMemoryHistoryManager() {
        history = new TasksLinkedList<Task>();
        nodeMap = new LinkedHashMap<>();
    }

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            history.removeNode(nodeMap.get(task.getId()));
        }
        history.linkLast(task);
        nodeMap.put(task.getId(), history.tail);
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            history.removeNode(nodeMap.get(id));
            nodeMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }
}

