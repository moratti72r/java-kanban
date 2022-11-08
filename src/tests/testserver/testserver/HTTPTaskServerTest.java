package testserver;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;
import taskserver.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HTTPTaskServerTest {
    HttpTaskServer srv;
    Gson gson;
    HttpClient client;

    @BeforeEach
    public void newHTTPTaskServer() throws IOException {
        srv = new HttpTaskServer();
        gson = new Gson();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void closeHTTPTaskServer() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        srv.stop();
    }

    @Test
    public void getStatus404WhenGetRequestAskNonExistTask() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .GET()
                .build();

        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }

        assertEquals(404, status);
    }

    @Test
    public void getStatus403WhenNotCorrectQueryString() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?sda2"))
                .header("Accept", "application/json")
                .GET()
                .build();

        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }

        assertEquals(403, status);
    }

    @Test
    public void getStatus400WhenPostRequestBodyIsEmpty() {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .header("Accept", "application/json")
                .POST(body)
                .build();

        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }

        assertEquals(400, status);
    }

    @Test
    public void getStatus400WhenPostRequestBodyNotCorrect() {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("{\"ustal\"=\"voobshe\"}");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .header("Accept", "application/json")
                .POST(body)
                .build();

        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }

        assertEquals(400, status);
    }

    @Test
    public void getStatus200WhenPostRequestBodyIsCorrect() {

        Task task1 = new Task(1, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));

        String jsonTask1 = gson.toJson(task1);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(body)
                .build();
        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }
        assertEquals(200, status);
    }


    @Test
    public void getStatus200WhenGetRequest() {
        Task task1 = new Task(1, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));

        getRequstPostMethodWithTaskAtURL("http://localhost:8080/tasks/task", task1);


        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .GET()
                .build();
        int status1 = 0;
        String taskJson = null;
        try {
            HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
            status1 = response.statusCode();
            if (status1 == 200) {
                taskJson = response.body();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }
        String t1 = JsonParser.parseString(taskJson)
                .getAsJsonArray()
                .get(0)
                .toString();

        assertEquals(200, status1);
        assertEquals(task1, gson.fromJson(t1, Task.class));
    }

    @Test
    public void getStatus200WhenCorrectQueryString() {
        Task task1 = new Task(1, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));

        getRequstPostMethodWithTaskAtURL("http://localhost:8080/tasks/task", task1);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .header("Accept", "application/json")
                .GET()
                .build();

        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }

        assertEquals(200, status);
    }

    @Test
    public void getStatus200WhenGetRequestForHistory() {
        Task task1 = new Task(1, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));

        getRequstPostMethodWithTaskAtURL("http://localhost:8080/tasks/task", task1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .GET()
                .build();
        int status1 = 0;
        String taskJson = null;
        try {
            HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
            status1 = response.statusCode();
            if (status1 == 200) {
                taskJson = response.body();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }
        String t1 = JsonParser.parseString(taskJson)
                .getAsJsonArray()
                .get(0)
                .toString();

        assertEquals(200, status1);
        assertEquals(task1, gson.fromJson(t1, Task.class));
    }

    @Test
    public void getStatus405WhenMethodRequestNotCorrect() {
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .DELETE()
                .build();
        int status1 = 0;
        try {
            HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
            status1 = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }

        assertEquals(405, status1);
    }

    @Test
    public void getStatus400WhenAddTaskWithIntersectionTime() {
        Task task1 = new Task(1, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));

        getRequstPostMethodWithTaskAtURL("http://localhost:8080/tasks/task", task1);

        Task task2 = new Task(2, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));

        String jsonTask2 = gson.toJson(task2);

        URI url = URI.create("http://localhost:8080/tasks/task");

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        String taskJson = null;
        try {
            HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
            int status1 = response.statusCode();
            if (status1 == 200) {
                taskJson = response.body();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }
        String t2 = JsonParser.parseString(taskJson)
                .getAsJsonArray()
                .get(0)
                .toString();

        int size = JsonParser.parseString(taskJson)
                .getAsJsonArray().size();

        assertEquals(400, status);
        assertFalse(task2.equals(gson.fromJson(t2, Task.class)));
        assertEquals(1, size);
    }

    @Test
    public void getStatus403WhenDeleteRequestNotCorrectQueryString() {
        Task task1 = new Task(1, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));
        getRequstPostMethodWithTaskAtURL("http://localhost:8080/tasks/task", task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=2"))
                .DELETE()
                .build();

        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }
        assertEquals(403, status);
    }

    @Test
    public void getStatus200WhenDeleteRequestCorrectQueryString() {
        Task task1 = new Task(1, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));
        getRequstPostMethodWithTaskAtURL("http://localhost:8080/tasks/task", task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .DELETE()
                .build();

        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }
        assertEquals(200, status);

    }


    @Test
    public void getStatus200WhenDeleteRequestWithoutQueryString() {
        Task task1 = new Task(1, "Имя", "спецификация",
                TaskStatus.DONE,
                LocalDateTime.of(2000, 10, 10, 10, 10),
                Duration.ofMinutes(10));
        getRequstPostMethodWithTaskAtURL("http://localhost:8080/tasks/task", task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .DELETE()
                .build();

        int status = 0;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }
        assertEquals(200, status);
    }

    private void getRequstPostMethodWithTaskAtURL(String uri, Task task) {
        String jsonTask = gson.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(body)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка");
        }
    }
}

