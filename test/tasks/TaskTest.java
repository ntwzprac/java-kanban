package tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TaskTest {
    @Test
    public void tasksAreEqualsIfIdsAreSame() {
        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
        Assertions.assertEquals(task1, task1, "Задачи не совпадают");
    }
}
