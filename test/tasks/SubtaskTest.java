package tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class SubtaskTest {
    @Test
    public void subTasksAreEqualsIfIdsAreSame() {
        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи", TaskStatus.NEW, 0);
        Assertions.assertEquals(subtask1, subtask1, "Подзадачи не совпадают");
    }

//    @Test
//    public void cantMakeSubtaskAsSubtasksEpic() {
//        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи", TaskStatus.NEW, 0);
//    } Subtask требует айди эпика, а не сам объект, не понимаю реализацию
}
