package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    getSubtasks(exchange);
                    break;
                case "POST":
                    addSubtask(exchange);
                    break;
                case "DELETE":
                    removeSubtask(exchange);
                    break;
                default:
                    sendNotFound(exchange);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendHasInteractions(exchange);
        }
    }

    private void getSubtasks(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");

        if (pathParts.length == 2) {
            List<Subtask> subtasks = taskManager.getSubtasks();
            String jsonResponse = gson.toJson(subtasks);
            sendText(httpExchange, jsonResponse);
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            Subtask subtask = taskManager.getSubtask(id);
            if (subtask != null) {
                sendText(httpExchange, gson.toJson(subtask));
            } else {
                sendNotFound(httpExchange);
            }
        } else {
            sendNotFound(httpExchange);
        }
    }

    private void addSubtask(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes());
        JsonElement jsonElement = JsonParser.parseString(requestBody);
        Type subtaskType = new TypeToken<Subtask>() {
        }.getType();
        Subtask subtask = gson.fromJson(jsonElement, subtaskType);

        if (subtask.getName() == null || subtask.getName().isEmpty()) {
            sendHasInteractions(httpExchange);
            return;
        }

        if (subtask.getTaskStatus() == null) {
            subtask.setTaskStatus(TaskStatus.NEW);
        }

        if (taskManager.getSubtask(subtask.getId()) != null) {
            taskManager.updateSubtask(subtask);
            sendCreated(httpExchange);
        } else {
            taskManager.addSubtask(subtask);
            sendText(httpExchange, gson.toJson(subtask));
        }
    }

    private void removeSubtask(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");

        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteSubtask(id);
            sendText(httpExchange, "Подзадача удалена");
        } else {
            sendNotFound(httpExchange);
        }
    }
}