package server;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import managers.TaskManager;
import managers.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final int PORT = 8080;
    protected Gson gson;
    private TaskManager taskManager;
    HttpServer server;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TasksHandler(taskManager));
            server.createContext("/epics", new EpicsHandler(taskManager));
            server.createContext("/subtasks", new SubtasksHandler(taskManager));
            server.createContext("/history", new HistoryHandler(taskManager));
            server.createContext("/prioritized", new PrioritizedHandler(taskManager));

        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера: " + e.getMessage());
        }
    }

    public void start() {
        System.out.println("Сервер запущен на порту: " + PORT);

        server.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен");

        server.stop(0);
    }

    public Gson getGson() {
        return gson;
    }
}