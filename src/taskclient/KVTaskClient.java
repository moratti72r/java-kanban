package taskclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String uri;
    private String API;
    private HttpClient client;

    public KVTaskClient(String uri) {
        client = HttpClient.newHttpClient();
        this.uri = uri;
        URI url = URI.create(uri + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            API = "API_TOKEN=" + response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public void put(String key, String json) {
        URI url = URI.create(uri + "save/" + key + "?" + API);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status == 200) {
                System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);
            } else if (status == 403) {
                System.out.println("Неверное значение API ключа. Код состояния: " + status);
            } else if (status == 400) {
                System.out.println("Путь указан не верно. Код состояния: " + status);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        URI url;
        if (key.contains("?")) {
            url = URI.create(uri + "load/" + key + "&" + API);
        } else {
            url = URI.create(uri + "load/" + key + "?" + API);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        String taskJson = "";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status == 200) {
                taskJson = response.body();
                System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);
            } else if (status == 403) {
                System.out.println("Неверное значение API ключа. Код состояния: " + status);
            } else if (status == 400) {
                System.out.println("Путь указан не верно. Код состояния: " + status);
            } else if (status == 404) {
                System.out.println("По данному пути данные отсутствуют. Код состояния: " + status);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return taskJson;
    }
}
