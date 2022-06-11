package Managers;

import Managers.TaskManager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends ManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void init() {
        manager = new InMemoryTaskManager();
        super.init();
    }
}