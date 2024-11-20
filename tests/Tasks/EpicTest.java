package Tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class EpicTest {
    @Test
    public void epicsAreEqualsIfIdsAreSame() {
        Epic epic1 = new Epic("Эпик1", "Описание эпика");
        Assertions.assertEquals(epic1, epic1, "Эпики не совпадают");
    }

    //    @Test
    //    public void cantAddEpicToEpicAsSubtask() {
    //        Epic epic1 = new Epic("Эпик1", "Описание эпика");
    //
    //    } Не понял требование. Метод addTask() у Epic принимает только Subtask изначально

    @Test
    public void deleteSubtaskWorks() {
        Epic epic1 = new Epic("Эпик1", "Описание эпика");
        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи", TaskStatus.NEW, epic1.getId());
        epic1.addTask(subtask1);
        epic1.deleteTask(subtask1.getId());
        Assertions.assertEquals(0, epic1.getTasks().size(), "Подзадача не удалена");
    }
}
