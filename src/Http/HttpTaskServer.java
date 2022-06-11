package Http;

import Managers.Managers;
import Managers.TaskManager.TaskManager;
import Tasks.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Handler;

public class HttpTaskServer {

    public static void main(String[] args) throws Exception {
        manager = Managers.getFileBacked();
        Task task = new Task("Task1", "Test1", TaskStatus.NEW, TaskType.TASK, LocalDateTime.of(2022, Month.JUNE, 2, 6, 0), 15);
        manager.addTask(task);
        Epic epic1 = new Epic("Epic1", "Test1", TaskStatus.NEW, TaskType.EPIC, LocalDateTime.of(2022, Month.JUNE, 1, 12, 0), 60);
        manager.addEpic(epic1);
        Sub sub1_1 = new Sub("Sub1_1", "test1", TaskStatus.NEW, TaskType.SUBTASK, epic1.getId(), LocalDateTime.of(2022, Month.JUNE, 1, 14, 0), 30);
        manager.addSubTask(sub1_1);
        new KVServer().start();
        new HttpTaskServer().start();

    }

    public static final int PORT = 8080;
    private final HttpServer server;
    private static Gson gson;
    private static TaskManager manager;


    public HttpTaskServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        server.createContext("/tasks", HttpTaskServer::getAllTasks);
        server.createContext("/tasks/task", HttpTaskServer::getTask);
        server.createContext("/tasks/epic", HttpTaskServer::getEpic);
        server.createContext("/tasks/sub", HttpTaskServer::getSub);
        server.createContext("/tasks/history", HttpTaskServer::getHistory);
    }

    private static void getAllTasks(HttpExchange exchange) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = " ";
            if (exchange.getRequestMethod().equals("GET")) {
                String responseTask = gson.toJson(manager.getTasks());
                String responseEpic = gson.toJson(manager.getEpics());
                String responseSub = gson.toJson(manager.getSubs());
                response = responseTask + responseEpic + responseSub;
                exchange.sendResponseHeaders(200, 0);
            } else {
                response = gson.toJson("Не работает");
                exchange.sendResponseHeaders(405, 0);
            }
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static void getTask(HttpExchange exchange) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = " ";
            String method = exchange.getRequestMethod();
            Map<String, String> map = getPathQuery(exchange);
            switch (method) {
                case "GET":
                    if (!map.isEmpty()) {
                        try {
                            Task task = manager.getTask(Long.parseLong(map.get("id")));
                            if (Objects.nonNull(task)) {
                                response = gson.toJson(task);
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                response = gson.toJson("Нет задачи");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            response = gson.toJson("Нет задачи с данным ID" + e.getMessage());
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        response = gson.toJson(manager.getTasks());
                        exchange.sendResponseHeaders(400, 0);
                    }
                case "POST":
                    String strJson = new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
                    if (!map.isEmpty()) {
                        // update
                        try {
                            Task task = gson.fromJson(strJson, new TypeToken<Task>() {
                            }.getType());
                            task.setId(Long.parseLong(map.get("id")));
                            response = gson.toJson(task);
                            exchange.sendResponseHeaders(200, 0);
                        } catch (Exception e) {
                            response = gson.toJson("обновление задачи не работает" + e.getMessage());
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        //add
                        try {
                            Task task = gson.fromJson(strJson, new TypeToken<Task>() {
                            }.getType());
                            manager.addTask(task);
                            if (task != null) {
                                response = gson.toJson(task);
                                exchange.sendResponseHeaders(201, 0);
                            } else {
                                response = gson.toJson("add ERROR");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            response = gson.toJson("создание задачи не работает" + e.getMessage());
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                case "DELETE":
                    if (!map.isEmpty()) {
                        try {
                            Task task = manager.getTask(Long.parseLong(map.get("id")));
                            if (task != null) {
                                manager.deleteTaskByIdSingle(task.getId());
                                response = gson.toJson("Удалено");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                response = gson.toJson("delete ERROR");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            response = gson.toJson("удаление задачи не работает" + e.getMessage());
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                default:
                    response = gson.toJson("Запрос не работает");
                    exchange.sendResponseHeaders(400, 0);
            }
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static Map<String, String> getPathQuery(HttpExchange exchange) {
        Map<String, String> map = new HashMap<>();
        map.put(exchange.getRequestURI().getPath(), exchange.getRequestURI().getQuery());
        return map;
        // todo скорее всего не работает как надо
    }


    private static void getEpic(HttpExchange exchange) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = " ";
            if (exchange.getRequestMethod().equals("GET")) {
                response = gson.toJson(manager.getEpics());
                exchange.sendResponseHeaders(200, 0);
            } else {
                response = gson.toJson("Не работает");
                exchange.sendResponseHeaders(405, 0);
            }
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static void getSub(HttpExchange exchange) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = " ";
            if (exchange.getRequestMethod().equals("GET")) {
                response = gson.toJson(manager.getSubs());
                exchange.sendResponseHeaders(200, 0);
            } else {
                response = gson.toJson("Не работает");
                exchange.sendResponseHeaders(405, 0);
            }
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static void getHistory(HttpExchange exchange) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = " ";
            if (exchange.getRequestMethod().equals("GET")) {
                response = gson.toJson(manager.getHistory());
                exchange.sendResponseHeaders(200, 0);
            } else {
                response = gson.toJson("Не работает");
                exchange.sendResponseHeaders(405, 0);
            }
            outputStream.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void start() {
        System.out.println("Running server on port " + PORT);
        server.start();
    }
}
