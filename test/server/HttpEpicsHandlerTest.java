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
import tasks.Epic;
import tasks.Subtask;
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

public class HttpEpicsHandlerTest {
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
    public void testAddEpic() throws IOException, InterruptedException {
        // создаём эпик
        Epic epic = new Epic("Test Epic", "Testing epic");

        // конвертируем его в JSON
        String epicJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание эпиков
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создался один эпик с корректным именем
        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Test Epic", epicsFromManager.get(0).getName(), "Некорректное имя эпика");
    }

    @Test
    public void testRemoveEpic() throws IOException, InterruptedException {
        // создаём эпик
        Epic epic = new Epic("Test Epic", "Testing epic");

        // добавляем эпик в менеджер
        manager.addEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за удаление эпиков
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что эпик удалился
        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(0, epicsFromManager.size(), "Некорректное количество эпиков");
    }

    @Test
    public void testGettingUnknownEpic() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за получение эпиков
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testGettingSubtasks() throws IOException, InterruptedException {
        // создаём эпик
        Epic epic = new Epic("Test Epic", "Testing epic");

        // создаём подзадачу
        Subtask subtask = new Subtask("Test Subtask", "Testing subtask", TaskStatus.NEW, epic.getId());

        // добавляем подзадачу в менеджер
        manager.addSubtask(subtask);

        // добавляем эпик в менеджер
        manager.addEpic(epic);

        // добавляем подзадачу в эпик
        epic.addTask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за получение подзадач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили одну подзадачу с корректным именем
        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals(1, epicsFromManager.get(0).getTasks().size(), "Некорректное количество подзадач");
        assertEquals("Test Subtask", epicsFromManager.get(0).getTasks().get(0).getName(), "Некорректное имя подзадачи");
    }
}