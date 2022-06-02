package Managers;


import Tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class ManagerTest<T extends TaskManager> {
    protected T manager;
    Task task;
    Epic epic1;
    Sub sub1_1;
    Sub sub1_2;

    @BeforeEach
    void init() {
        task = new Task("Task1", "Test1", TaskStatus.NEW, TaskType.TASK,  LocalDateTime.of(2022, Month.JUNE, 2, 6, 0),15);
        manager.addTask(task);
        epic1 = new Epic("Epic1", "Test1", TaskStatus.NEW, TaskType.EPIC,  LocalDateTime.of(2022, Month.JUNE, 1, 12, 0),60);
        manager.addEpic(epic1);
        sub1_1 = new Sub("Sub1_1", "test1", TaskStatus.NEW, TaskType.SUBTASK,epic1.getId(), LocalDateTime.of(2022, Month.JUNE, 1, 14, 0),30);
        sub1_2 = new Sub("Sub1_2", "test1", TaskStatus.NEW, TaskType.SUBTASK,epic1.getId(), LocalDateTime.of(2022, Month.JUNE, 1, 15, 0),30);
        manager.addSubTask(sub1_1);
        manager.addSubTask(sub1_2);
    }

    @Test
    void getTasksTest() {
        final List<Task> tasks = manager.getTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(1, task.getId());
        assertEquals(task, tasks.get(0));
    }

    @Test
    void getEpicsTest() {
        final List<Epic> epics = manager.getEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals(2, epic1.getId());
        assertEquals(epic1, epics.get(0));
    }

    @Test
    void getSubsTest() {
        final List<Sub> subs = manager.getSubs();
        assertNotNull(subs);
        assertEquals(2, subs.size());
        assertEquals(3, sub1_1.getId());
        assertEquals(4, sub1_2.getId());
        assertEquals(sub1_1, subs.get(0));
        assertEquals(sub1_2, subs.get(1));
    }

    @Test
    void getTaskTest() {
        final Task tasks = manager.getTask(1);
        assertNotNull(tasks);
        assertEquals(tasks.getId(), task.getId());
    }

    @Test
    void getEpicTest() {
        final Epic epics = manager.getEpic(2);
        assertNotNull(epics);
        assertEquals(epics.getId(), epic1.getId());
    }

    @Test
    void getSubTest() {
        final Sub sub1 = manager.getSub(3);
        final Sub sub2 = manager.getSub(4);
        assertNotNull(sub1);
        assertNotNull(sub2);
        assertEquals(sub1.getId(), sub1_1.getId());
        assertEquals(sub2.getId(), sub1_2.getId());
    }

    @Test
    void getSubsByEpic() {
        List<Sub> subList = new ArrayList<>();
        subList.add(sub1_1);
        subList.add(sub1_2);
        assertEquals(manager.getSub(4), subList.get(1));
    }

    @Test
    void getSingleById() {
        final Task tasks = manager.getTask(1);
        assertNotNull(tasks);
        assertEquals(manager.getTask(1), tasks);
    }

    @Test
    void getEpicById() {
        final Epic epics = manager.getEpic(2);
        assertNotNull(epics);
        assertEquals(manager.getEpic(2), epics);
    }

    @Test
    void getSubById() {
        final Sub sub1 = manager.getSub(3);
        final Sub sub2 = manager.getSub(4);
        assertNotNull(sub1);
        assertNotNull(sub2);
        assertEquals(manager.getSub(3), sub1);
        assertEquals(manager.getSub(4), sub2);
    }

    @Test
    void updateTask() {
        Task task2 = new Task("task2", "test2", TaskStatus.NEW, TaskType.TASK, LocalDateTime.of(2022, Month.JUNE, 3, 15, 0), 25);
        task2.setId(1L);
        manager.updateTask(task2);
        assertEquals(manager.getTask(1),
                manager.getTasks().get(0));
    }

    @Test
    void updateEpic() {
        Epic epic2 = new Epic("Epic2", "test2", TaskStatus.NEW, TaskType.EPIC, LocalDateTime.of(2022, Month.JUNE, 4, 16, 0), 120);
        epic2.setId(2L);
        manager.updateEpic(epic2);
        assertEquals(manager.getEpic(2),
                manager.getEpics().get(0));
    }

    @Test
    void updateSub() {
        Sub sub2 = new Sub("Sub2", "test2", TaskStatus.NEW, TaskType.SUBTASK,epic1.getId(),LocalDateTime.of(2022, Month.JUNE, 4, 19, 0), 40);
        sub2.setId(3L);
        manager.updateSub(sub2);
        assertEquals(manager.getSub(3),
                manager.getSubs().get(0));
    }

    @Test
    void deleteTaskByIdSingle() {
        manager.deleteTaskByIdSingle(1);
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void deleteTaskByIdEpic() {
        manager.deleteTaskByIdEpic(2);
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void deleteTaskByIdSubtask() {
        manager.deleteTaskByIdSubtask(3);
        manager.deleteTaskByIdSubtask(4);
        assertTrue(manager.getSubs().isEmpty());
    }

    @Test
    void updateEpicStatusInProgress() {
        sub1_1.setStatus(TaskStatus.IN_PROGRESS);
        sub1_2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateEpicStatus(epic1);
        assertEquals(epic1.getStatus(),TaskStatus.IN_PROGRESS);
    }

    @Test
    void updateEpicStatusInNew() {
        sub1_1.setStatus(TaskStatus.NEW);
        sub1_2.setStatus(TaskStatus.NEW);
        manager.updateEpicStatus(epic1);
        assertEquals(epic1.getStatus(),TaskStatus.NEW);
    }

    @Test
    void updateEpicStatusInDone() {
        sub1_1.setStatus(TaskStatus.DONE);
        sub1_2.setStatus(TaskStatus.DONE);
        manager.updateEpicStatus(epic1);
        assertEquals(epic1.getStatus(),TaskStatus.DONE);
    }

    @Test
    void updateEpicStatusInNewAndDone() {
        sub1_1.setStatus(TaskStatus.NEW);
        sub1_2.setStatus(TaskStatus.DONE);
        manager.updateEpicStatus(epic1);
        assertEquals(epic1.getStatus(),TaskStatus.IN_PROGRESS);
    }


}