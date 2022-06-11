package Http;

import Managers.TaskManager.InMemoryTaskManager;
import Managers.TaskManager.TaskManager;
import Tasks.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
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
//        server.createContext("/tasks/task", this::getTask);
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

//    private static void getTask(HttpExchange exchange) {
//        try (OutputStream outputStream = exchange.getResponseBody()) {
//            exchange.getResponseHeaders().set("Content-Type", "application/json");
//            String response = " ";
//
//            String method = exchange.getRequestMethod();
//            System.out.println("Обработка " + method + " запроса");
//            String path = exchange.getRequestURI().getPath();
//            System.out.println("Обработка " + path);
//            URI requestURI = exchange.getRequestURI();
//            System.out.println("Началась обработка " + requestURI);
//            String query = requestURI.getQuery();
//            switch (method) {
//                case "GET":
//                    if (!query.isEmpty()) {
//                        try {
//                            Task task = manager.getTask(Long.parseLong(map.get("id")));
//                            if (Objects.nonNull(task)) {
//                                response = gson.toJson(task);
//                                exchange.sendResponseHeaders(200, 0);
//                            } else {
//                                response = gson.toJson("Нет задачи");
//                                exchange.sendResponseHeaders(404, 0);
//                            }
//                        } catch (Exception e) {
//                            response = gson.toJson("Нет задачи с данным ID" + e.getMessage());
//                            exchange.sendResponseHeaders(400, 0);
//                        }
//                    } else {
//                        response = gson.toJson(manager.getTasks());
//                        exchange.sendResponseHeaders(400, 0);
//                    }
//                case "POST":
//                    String strJson = new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
//                    if (!map.isEmpty()) {
//                        // update
//                        try {
//                            Task task = gson.fromJson(strJson, new TypeToken<Task>() {
//                            }.getType());
//                            task.setId(Long.parseLong(map.get("id")));
//                            response = gson.toJson(task);
//                            exchange.sendResponseHeaders(200, 0);
//                        } catch (Exception e) {
//                            response = gson.toJson("обновление задачи не работает" + e.getMessage());
//                            exchange.sendResponseHeaders(400, 0);
//                        }
//                    } else {
//                        //add
//                        try {
//                            Task task = gson.fromJson(strJson, new TypeToken<Task>() {
//                            }.getType());
//                            manager.addTask(task);
//                            if (task != null) {
//                                response = gson.toJson(task);
//                                exchange.sendResponseHeaders(201, 0);
//                            } else {
//                                response = gson.toJson("add ERROR");
//                                exchange.sendResponseHeaders(404, 0);
//                            }
//                        } catch (Exception e) {
//                            response = gson.toJson("создание задачи не работает" + e.getMessage());
//                            exchange.sendResponseHeaders(400, 0);
//                        }
//                    }
//                case "DELETE":
//                    if (!map.isEmpty()) {
//                        try {
//                            Task task = manager.getTask(Long.parseLong(map.get("id")));
//                            if (task != null) {
//                                manager.deleteTaskByIdSingle(task.getId());
//                                response = gson.toJson("Удалено");
//                                exchange.sendResponseHeaders(200, 0);
//                            } else {
//                                response = gson.toJson("delete ERROR");
//                                exchange.sendResponseHeaders(404, 0);
//                            }
//                        } catch (Exception e) {
//                            response = gson.toJson("удаление задачи не работает" + e.getMessage());
//                            exchange.sendResponseHeaders(400, 0);
//                        }
//                    }
//                default:
//                    response = gson.toJson("Запрос не работает");
//                    exchange.sendResponseHeaders(400, 0);
//            }
//            outputStream.write(response.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//    }
//
//    private static String getPathQuery(HttpExchange exchange) {
//        Map<String, String> map = new HashMap<>();
//        map.put(exchange.getRequestURI().getPath(), exchange.getRequestURI().getQuery());
//        String returned = map.get(exchange.getRequestURI().getQuery());
//        return returned;
//        // todo скорее всего не работает как надо
//    }


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
