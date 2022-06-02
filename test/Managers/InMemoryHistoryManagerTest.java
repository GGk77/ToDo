package Managers;

import Tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryTaskManager manager = new InMemoryTaskManager();
    HistoryManager historyManager;
    Task task1;
    Epic epic1;
    Sub sub1_1;
    Sub sub1_2;

    @BeforeEach
    void init() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task1", "Test1", TaskStatus.NEW, TaskType.TASK,  LocalDateTime.of(2022, Month.JUNE, 2, 6, 0),15);
        manager.addTask(task1);
        epic1 = new Epic("Epic1", "Test1", TaskStatus.NEW, TaskType.EPIC,  LocalDateTime.of(2022, Month.JUNE, 1, 12, 0),60);
        manager.addEpic(epic1);
        sub1_1 = new Sub("Sub1_1", "test1", TaskStatus.NEW, TaskType.SUBTASK,epic1.getId(), LocalDateTime.of(2022, Month.JUNE, 1, 14, 0),30);
        sub1_2 = new Sub("Sub1_2", "test1", TaskStatus.NEW, TaskType.SUBTASK,epic1.getId(), LocalDateTime.of(2022, Month.JUNE, 1, 15, 0),30);
        manager.addSubTask(sub1_1);
        manager.addSubTask(sub1_2);

    }

    @Test
    void getHistoryTest() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1_1);

        assertEquals(3, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
    }

    @Test
    void historyIsEmptyTest() {
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void removeTest() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1_1);
        historyManager.add(sub1_2);
        historyManager.remove(1);
        historyManager.remove(3);
        assertFalse(historyManager.getHistory().isEmpty());
        assertEquals(2, historyManager.getHistory().size());
        historyManager.remove(2);
        historyManager.remove(4);
        assertEquals(0,historyManager.getHistory().size());
    }

    @Test
    void duplicateTest() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1_1);
        historyManager.add(sub1_2);
        manager.getTask(1);
        manager.getTask(1);
        manager.getSub(3);
        manager.getSub(3);
        manager.getSub(3);
        manager.getEpic(2);
        manager.getEpic(2);
        assertEquals(4, historyManager.getHistory().size());
    }
}