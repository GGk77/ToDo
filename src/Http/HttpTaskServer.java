package Http;

import Managers.Managers;
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
import java.util.ArrayList;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;


public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private Gson gson;
    private TaskManager manager;

    public HttpTaskServer() throws Exception {
        manager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        System.out.println(server);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        init();

    }

    public HttpTaskServer(TaskManager manager) throws Exception {
        this.manager = manager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        System.out.println(server);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    init();
    }

    public void init() {
        server.createContext("/tasks", this::getAllTasks);
        server.createContext("/tasks/task", this::getTask);
        server.createContext("/tasks/epic", this::getEpic);
        server.createContext("/tasks/sub", this::getSub);
        server.createContext("/tasks/history", this::getHistory);
    }

    private void getHistory(HttpExchange exchange) {
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
                        try {
                            Task task = manager.getTask(id);
                            if (Objects.nonNull(task)) {
                                response = gson.toJson(task);
                                sendText(exchange, response);
                            } else {
                                response = gson.toJson("Нет задачи");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            response = gson.toJson("Нет получения" + e.getMessage());
                            exchange.sendResponseHeaders(400, 0);
                        }

                    } else {
                        response = gson.toJson("Укажите ID" );
                        exchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case "POST":
                    // update
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
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
                            response = gson.toJson(task);
                            sendText(exchange, response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        response = gson.toJson("создание/обновление задачи не работает" + e.getMessage());
                        exchange.sendResponseHeaders(400, 0);
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
                            e.printStackTrace();
                            response = gson.toJson("удаление задачи не работает" + e.getMessage());
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

    private void getEpic(HttpExchange exchange) {
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
                        try {
                            Epic epic = manager.getEpic(id);
                            if (Objects.nonNull(epic)) {
                                response = gson.toJson(epic);
                                sendText(exchange, response);
                            } else {
                                response = gson.toJson("Нет задачи");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            response = gson.toJson("Нет получения" + e.getMessage());
                            exchange.sendResponseHeaders(400, 0);
                        }

                    } else {
                        ArrayList<Epic> list = new ArrayList<>(manager.getEpics());
                        response = gson.toJson(list);
                        exchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case "POST":
                    // update
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                    try {
                        Epic epic = gson.fromJson(body, new TypeToken<Epic>() {
                        }.getType());
                        if (manager.getEpic(epic.getId()) != null) {
                            manager.updateTask(epic);
                            response = gson.toJson(epic);
                            sendText(exchange, response);
                        } else {
                            //add
                            manager.addEpic(epic);
                            response = gson.toJson(epic);
                            sendText(exchange, response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        response = gson.toJson("создание/обновление задачи не работает" + e.getMessage());
                        exchange.sendResponseHeaders(400, 0);
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
                            long idEpic = Long.parseLong(query.substring(3));
                            Epic epic = manager.getEpic(idEpic);
                            if (epic != null) {
                                manager.deleteTaskByIdEpic(epic.getId());
                                response = gson.toJson("Удалено");
                                sendText(exchange, response);
                            } else {
                                response = gson.toJson("Не удалено");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            response = gson.toJson("удаление задачи не работает" + e.getMessage());
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

    private void getSub(HttpExchange exchange) {
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
                        try {
                            Sub sub = manager.getSub(id);
                            if (Objects.nonNull(sub)) {
                                response = gson.toJson(sub);
                                sendText(exchange, response);
                            } else {
                                response = gson.toJson("Нет задачи");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            response = gson.toJson("Нет получения" + e.getMessage());
                            exchange.sendResponseHeaders(400, 0);
                        }

                    } else {
                        ArrayList<Sub> list = new ArrayList<>(manager.getSubs());
                        response = gson.toJson(list);
                        exchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case "POST":
                    // update
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                    try {
                        Sub sub = gson.fromJson(body, new TypeToken<Sub>() {
                        }.getType());
                        if (manager.getSub(sub.getId()) != null) {
                            manager.updateSub(sub);
                            response = gson.toJson(sub);
                            sendText(exchange, response);
                        } else {
                            //add
                            manager.addSubTask(sub);
                            response = gson.toJson(sub);
                            sendText(exchange, response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        response = gson.toJson("создание/обновление задачи не работает" + e.getMessage());
                        exchange.sendResponseHeaders(400, 0);
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
                            long idSub = Long.parseLong(query.substring(3));
                            Sub sub = manager.getSub(idSub);
                            if (sub != null) {
                                manager.deleteTaskByIdSubtask(sub.getId());
                                response = gson.toJson("Удалено");
                                sendText(exchange, response);
                            } else {
                                response = gson.toJson("Не удалено");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            response = gson.toJson("удаление задачи не работает" + e.getMessage());
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

    public void stop() {
            server.stop(0);
    }
}
