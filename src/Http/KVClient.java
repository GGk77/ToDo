package Http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    public HttpClient client;
    private String token;
    private String url;

    public KVClient(int port) {
        this.client = HttpClient.newHttpClient();
        this.url = "http://localhost:" + port;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                this.token = response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        try {
            client = HttpClient.newHttpClient();
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(body)
                    .uri(URI.create("http://localhost:8078/save/"
                            + key + "?API_TOKEN- " + token))
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request,handler);
            System.out.println(response);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в методе put - KVClient");
        }
    }
    // возвращает гет /load/<key>?API_TOKEN
    public String load(String key) {
        String loader = null;
        try {
            client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8078/load/"
                    + key + "?API_TOKEN- " + token))
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request,handler);
            loader = response.body();
            System.out.println(response);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в методе load - KVClient");
        }
        return loader;
    }
}
