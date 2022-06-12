package Http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;
import Managers.TaskManager.InMemoryTaskManager;
import Managers.TaskManager.TaskManager;
import Tasks.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;


class HttpTaskServerTest {
    private TaskManager manager;

    private HttpTaskServer httpTaskServer;
    private Task task;
    private Epic epic1;
    private Sub sub1_1;
    private Sub sub1_2;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    @BeforeEach
    void init() {
        try {
            manager = new InMemoryTaskManager();
            httpTaskServer =  new HttpTaskServer(manager);
            httpTaskServer.start();
        task = new Task("Task1", "Test1", TaskStatus.NEW, TaskType.TASK, LocalDateTime.of(2022, Month.JUNE, 2, 6, 0), 15);
        manager.addTask(task);
        epic1 = new Epic("Epic1", "Test1", TaskStatus.NEW, TaskType.EPIC, LocalDateTime.of(2022, Month.JUNE, 1, 12, 0), 60);
        manager.addEpic(epic1);
        long epicId = epic1.getId();
        sub1_1 = new Sub("Sub1_1", "test1", TaskStatus.NEW, TaskType.SUBTASK, epicId, LocalDateTime.of(2022, Month.JUNE, 1, 14, 0), 30);
        sub1_2 = new Sub("Sub1_2", "test1", TaskStatus.NEW, TaskType.SUBTASK, epicId, LocalDateTime.of(2022, Month.JUNE, 1, 15, 0), 30);
        manager.addSubTask(sub1_1);
        manager.addSubTask(sub1_2);
        manager.getTask(task.getId());
        manager.getTask(epic1.getId());
        manager.getTask(sub1_1.getId());
        manager.getTask(sub1_2.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @AfterEach
    void stop() {
        httpTaskServer.stop();
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getAllTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeTaskTestById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
    }
    @Test
    void removeTaskTestWithoutId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
    }
    @Test
    void removeEpicTestById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
    }
    @Test
    void removeEpicWithoutById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeSubTestById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
        assertEquals(1,manager.getSubs().size());
    }
    @Test
    void removeSubWithoutById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
        assertEquals(0,manager.getSubs().size());
    }


    @Test
    void getTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080");
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/task/?id=1")).GET().build();
        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        String body = response.body();
        System.out.println(body);
    }
    @Test
    void getTaskWithoutIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080");
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/task/")).GET().build();
        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
    @Test
    void getEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080");
        String json = gson.toJson(epic1);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic/?id=2")).GET().build();
        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        String body = response.body();
        System.out.println(body);
    }
    @Test
    void getSubTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080");
        String json = gson.toJson(sub1_1);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask/?id=3")).GET().build();
        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        String body = response.body();
        System.out.println(body);
    }

    @Test
    void createTaskTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task task1 = new Task("Task1", "Test1", TaskStatus.NEW, TaskType.TASK, LocalDateTime.of(2022, Month.JUNE, 12, 18, 0), 45);
        String json = gson.toJson(task1);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200,response.statusCode());
    }

    @Test
    void createEpicTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Epic epic = new Epic("ep", "ep2", TaskStatus.NEW, TaskType.TASK, LocalDateTime.of(2022, Month.JUNE, 17, 18, 0), 45);
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200,response.statusCode());
    }

    @Test
    void createSubTest() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
       Sub sub1_3 = new Sub("Sub1_3", "test1", TaskStatus.NEW, TaskType.SUBTASK, epic1.getId(), LocalDateTime.of(2022, Month.JUNE, 4, 15, 0), 30);
        String json = gson.toJson(sub1_3);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200,response.statusCode());
    }
}
