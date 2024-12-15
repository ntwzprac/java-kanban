package managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ManagersTest {
    @Test
    public void doesManagersGivesAlreadyInitializedInMemoryTaskManager() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test
    public void doesManagersGivesAlreadyInitializedInMemoryHistoryManager() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}
