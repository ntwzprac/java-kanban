package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                getHistory(exchange);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendHasInteractions(exchange);
        }
    }

    private void getHistory(HttpExchange httpExchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        String response = gson.toJson(history);
        sendText(httpExchange, response);
    }
}