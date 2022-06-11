package Managers;

import static org.junit.jupiter.api.Assertions.*;

import Managers.FileBacked.FileBackedTasksManager;
import Managers.History.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
class FileBackedTasksManagerTest extends ManagerTest<FileBackedTasksManager> {
    HistoryManager historyManager;
    @BeforeEach
    void init() {
        historyManager = Managers.getHistoryDefault();
        manager = new FileBackedTasksManager(new File("taskPrinter.csv"));
        super.init();
    }

    @Test
    void saveFileTaskAndHistoryTest() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter("taskPrinter.csv"))) {
            String head = "id,type,name,status,description,epicId";
            writer.append(head);
            writer.newLine();
            writer.write(String.valueOf(task));
            writer.newLine();
            writer.write(String.valueOf(epic1));
            writer.newLine();
            writer.write(String.valueOf(sub1_1));
            writer.newLine();
            writer.write(String.valueOf(sub1_2));
            writer.newLine();
            historyManager.add(task);
            historyManager.add(epic1);
            historyManager.add(sub1_1);
            historyManager.add(sub1_2);
            var history = manager.historyToString(historyManager);
            writer.write(history);
            writer.flush();
            BufferedReader reader = new BufferedReader(new FileReader("taskPrinter.csv"));
            assertEquals(head, reader.readLine());
            assertEquals("Task{name='Task12', description='Test12', status=NEW, id=1, startTime=2022-06-02T06:00, duration15}", reader.readLine());
            assertEquals("Epic{name='Epic1', description='Test1', status=NEW, id=2, startTime=2022-06-01T14:00, duration90}", reader.readLine());
            assertEquals("Sub{name='Sub1_1', description='test1', status=NEW, id=3, startTime=2022-06-01T14:00, duration30}", reader.readLine());
            assertEquals("Sub{name='Sub1_2', description='test1', status=NEW, id=4, startTime=2022-06-01T15:00, duration30}", reader.readLine());
            assertEquals("1,2,3,4",reader.readLine());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    @Test
    void saveFileEmptyTest() {
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("taskPrinter.csv"));
            write.write("");
            write.close();
            BufferedReader reader = new BufferedReader(new FileReader("taskPrinter.csv"));
            reader.readLine();
            assertNull(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void loadFromFile() {
        try {
            FileBackedTasksManager.loadFromFile(new File("taskPrinter.csv"));
            BufferedReader reader = new BufferedReader(new FileReader("taskPrinter.csv"));
            reader.readLine();
            assertEquals("1,TASK,Task1,NEW,Test1", reader.readLine());
            assertEquals("2,EPIC,Epic1,NEW,Test1", reader.readLine());
            assertEquals("3,SUBTASK,Sub1_1,NEW,test1,2", reader.readLine());
            assertEquals("4,SUBTASK,Sub1_2,NEW,test1,2", reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}