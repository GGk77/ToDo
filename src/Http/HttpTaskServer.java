package Http;

import Managers.TaskManager.InMemoryTaskManager;
import Managers.TaskManager.TaskManager;
import Tasks.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;


public class HttpTaskServer {

    public static void main(String[] args) throws Exception {
        TaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Task1", "Test1", TaskStatus.NEW, TaskType.TASK, LocalDateTime.of(2022, Month.JUNE, 2, 6, 0), 15);
        manager.addTask(task);
        Epic epic1 = new Epic("Epic1", "Test1", TaskStatus.NEW, TaskType.EPIC, LocalDateTime.of(2022, Month.JUNE, 1, 12, 0), 60);
        manager.addEpic(epic1);
        Sub sub1_1 = new Sub("Sub1_1", "test1", TaskStatus.NEW, TaskType.SUBTASK, epic1.getId(), LocalDateTime.of(2022, Month.JUNE, 1, 14, 0), 30);
        manager.addSubTask(sub1_1);
        new KVServer().start();
        new HttpTaskServer(manager).start();

    }

    public static final int PORT = 8080;
    private final HttpServer server;
    private Gson gson;
    private TaskManager manager;


    public HttpTaskServer(TaskManager manager) throws Exception {
        this.manager = manager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        server.createContext("/tasks", this::getAllTasks);
        server.createContext("/tasks/task", this::getTask);
//        server.createContext("/tasks/epic", this::getEpic);
//        server.createContext("/tasks/sub", this::getSub);
//        server.createContext("/tasks/history", this::getHistory);
    }

    private void getAllTasks(HttpExchange exchange) {
        System.out.println(exchange.getRequestURI());
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = " ";
            if (exchange.getRequestMethod().equals("GET")) {
                ArrayList<Task> list = new ArrayList<>();
                list.addAll(manager.getTasks());
                list.addAll(manager.getEpics());
                list.addAll(manager.getSubs());
                response = gson.toJson(list);
                sendText(exchange, response);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void getTask(HttpExchange exchange) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String response = " ";

            String method = exchange.getRequestMethod();
            System.out.println("Обработка " + method + " запроса");

            String path = exchange.getRequestURI().getPath();
            System.out.println("Обработка " + path);

            URI requestURI = exchange.getRequestURI();
            System.out.println("Началась обработка " + requestURI);

            String query = requestURI.getQuery();
            switch (method) {
                case "GET":
                    //getById
                    if (requestURI.getQuery() != null) {
                        long id = Long.parseLong(exchange.getRequestURI().getQuery().split("=")[1]);
                        if (path.endsWith("tasks/task/")) {
                            Task task = manager.getTask(id);
                            try {
                                if (Objects.nonNull(task)) {
                                    response = gson.toJson(task);
                                    sendText(exchange, response);
                                    break;
                                } else {
                                    response = gson.toJson("Нет задачи");
                                    exchange.sendResponseHeaders(404, 0);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        ArrayList<Task> list = new ArrayList<>();
                        list.addAll(manager.getTasks());
                        response = gson.toJson(list);
                        sendText(exchange, response);
                    }
                    break;
                case "POST":
                    // update
                    long id = Long.parseLong(exchange.getRequestURI().getQuery().split("=")[1]);
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                    if (path.endsWith("tasks/task/")) {
                        try {
                            Task task = gson.fromJson(body, new TypeToken<Task>() {
                            }.getType());
                            if (manager.getTask(task.getId()) != null) {
                                manager.updateTask(task);
                                response = gson.toJson(task);
                                sendText(exchange, response);
                            } else {
                                //add
                                manager.addTask(task);
                                if (task != null) {
                                    response = gson.toJson(task);
                                    exchange.sendResponseHeaders(201, 0);
                                } else {
                                    response = gson.toJson("add ERROR");
                                    exchange.sendResponseHeaders(404, 0);
                                }
                            }
                        } catch (Exception e) {
                            response = gson.toJson("создание задачи не работает" + e.getMessage());
                            e.printStackTrace();
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        manager.deleteAll();
                        response = gson.toJson("Удалено");
                        sendText(exchange, response);
                        break;
                    } else {
                        try {
                            long idTask = Long.parseLong(query.substring(3));
                            Task task = manager.getTask(idTask);
                            if (task != null) {
                                manager.deleteTaskByIdSingle(task.getId());
                                response = gson.toJson("Удалено");
                                sendText(exchange, response);
                            } else {
                                response = gson.toJson("Не удалено");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            response = gson.toJson("удаление задачи не работает" + e.getMessage());
                            e.printStackTrace();
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;
                default:
                    outputStream.write(response.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

//    private static void getEpic(HttpExchange exchange) {
//        try (OutputStream outputStream = exchange.getResponseBody()) {
//            exchange.getResponseHeaders().set("Content-Type", "application/json");
//            String response = " ";
//            if (exchange.getRequestMethod().equals("GET")) {
//                response = gson.toJson(manager.getEpics());
//                exchange.sendResponseHeaders(200, 0);
//            } else {
//                response = gson.toJson("Не работает");
//                exchange.sendResponseHeaders(405, 0);
//            }
//            outputStream.write(response.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//    }
//
//    private static void getSub(HttpExchange exchange) {
//        try (OutputStream outputStream = exchange.getResponseBody()) {
//            exchange.getResponseHeaders().set("Content-Type", "application/json");
//            String response = " ";
//            if (exchange.getRequestMethod().equals("GET")) {
//                response = gson.toJson(manager.getSubs());
//                exchange.sendResponseHeaders(200, 0);
//            } else {
//                response = gson.toJson("Не работает");
//                exchange.sendResponseHeaders(405, 0);
//            }
//            outputStream.write(response.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//    }
//
//    private static void getHistory(HttpExchange exchange) {
//        try (OutputStream outputStream = exchange.getResponseBody()) {
//            exchange.getResponseHeaders().set("Content-Type", "application/json");
//            String response = " ";
//            if (exchange.getRequestMethod().equals("GET")) {
//                response = gson.toJson(manager.getHistory());
//                exchange.sendResponseHeaders(200, 0);
//            } else {
//                response = gson.toJson("Не работает");
//                exchange.sendResponseHeaders(405, 0);
//            }
//            outputStream.write(response.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void start() {
        System.out.println("\nЗапускаем сервер на порту " + PORT);
        System.out.println("Сделай запрос в insomnia http://localhost:8080/");
        server.start();
    }
}
