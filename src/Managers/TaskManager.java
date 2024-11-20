package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();

    void deleteTasks();
    void deleteEpics();
    void deleteSubtasks();

    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTask(int id);
    void deleteEpic(int id);
    void deleteSubtask(int id);

    List<Subtask> getEpicSubtasks(int id);

    List<Task> getHistory();
}
