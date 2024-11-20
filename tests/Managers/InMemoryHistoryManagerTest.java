package Managers;

import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import Managers.InMemoryHistoryManager;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void doesAddingNewTaskIncrementsHistorySize() {
        Assertions.assertEquals(0, historyManager.getHistory().size(), "Изначальная история не пуста");

        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
        historyManager.add(task1);

        Assertions.assertEquals(1, historyManager.getHistory().size(), "Размер истории не увеличился при добавлении новой задачи");
    }

    @Test
    public void canHistoryReachMoreThanTenTasks() {
        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
        for (int i = 0; i < 11; i++) {
            historyManager.add(task1);
        }

        Assertions.assertEquals(10, historyManager.getHistory().size(), "Размер истории привысил 10 элементов");
    }

    @Test
    public void doesEleventhTaskDeletesFirstOneInHistory() {
        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Описание задачи", TaskStatus.NEW);
        Task task3 = new Task("Задача3", "Описание задачи", TaskStatus.NEW);

        historyManager.add(task1);

        for (int i = 0; i < 9; i++) {
            historyManager.add(task2);
        }

        historyManager.add(task3);

        Assertions.assertNotEquals("Задача1", historyManager.getHistory().getFirst().getName(), "Задача1 не была удалена из истории");
        Assertions.assertEquals("Задача3", historyManager.getHistory().getLast().getName(), "Задача3 не была найдена в истории");
    }
}
