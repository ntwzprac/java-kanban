package server;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpPrioritizedHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    static Gson gson;

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @BeforeAll
    public static void setUpAll() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test Task", "Testing task", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(1));
        manager.addTask(task);

        Task task2 = new Task("Test Task 2", "Testing task 2", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.now().plusHours(1));
        task2.setDuration(Duration.ofHours(1));
        manager.addTask(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за получение приоритетных задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что приоритетные задачи содержат одну задачу с корректным именем
        List<Task> prioritizedTasksFromManager = manager.getPrioritizedTasks();

        assertNotNull(prioritizedTasksFromManager, "Приоритетные задачи не возвращаются");
        assertEquals(2, prioritizedTasksFromManager.size(), "Некорректное количество приоритетных задач");
        assertEquals("Test Task", prioritizedTasksFromManager.get(0).getName(), "Некорректное имя приоритетной задачи");
    }

    @Test
    public void testGetEmptyPrioritizedTasks() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за получение приоритетных задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что приоритетные задачи пустые
        List<Task> prioritizedTasksFromManager = manager.getPrioritizedTasks();

        assertNotNull(prioritizedTasksFromManager, "Приоритетные задачи не возвращаются");
        assertEquals(0, prioritizedTasksFromManager.size(), "Приоритетные задачи не пустые");
    }
}