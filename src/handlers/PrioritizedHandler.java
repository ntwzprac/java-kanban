package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                getPrioritized(exchange);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendHasInteractions(exchange);
        }
    }

    private void getPrioritized(HttpExchange httpExchange) throws IOException {
        List<Task> history = taskManager.getPrioritizedTasks();
        String response = gson.toJson(history);
        sendText(httpExchange, response);
    }
}