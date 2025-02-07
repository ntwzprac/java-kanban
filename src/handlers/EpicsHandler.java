package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    getEpics(exchange);
                    break;
                case "POST":
                    addEpic(exchange);
                    break;
                case "DELETE":
                    removeEpic(exchange);
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

    private void getEpics(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");

        if (pathParts.length == 2) {
            List<Epic> epics = taskManager.getEpics();
            String jsonResponse = gson.toJson(epics);
            sendText(httpExchange, jsonResponse);
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            Epic epic = taskManager.getEpic(id);
            if (epic != null) {
                sendText(httpExchange, gson.toJson(epic));
            } else {
                sendNotFound(httpExchange);
            }
        } else if (pathParts.length == 4 && "subtasks".equals(pathParts[3])) {
            int id = Integer.parseInt(pathParts[2]);
            Epic epic = taskManager.getEpic(id);
            if (epic != null) {
                List<Subtask> subtasks = epic.getTasks();
                sendText(httpExchange, gson.toJson(subtasks));
            } else {
                sendNotFound(httpExchange);
            }
        } else {
            sendNotFound(httpExchange);
        }
    }

    private void addEpic(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes());
        JsonElement jsonElement = JsonParser.parseString(requestBody);
        Type epicType = new TypeToken<Epic>() {
        }.getType();
        Epic epic = gson.fromJson(jsonElement, epicType);

        if (epic.getName() == null || epic.getName().isEmpty()) {
            sendHasInteractions(httpExchange);
            return;
        }

        if (epic.getTaskStatus() == null) {
            epic.setTaskStatus(TaskStatus.NEW);
        }

        taskManager.addEpic(epic);
        sendCreated(httpExchange);
    }

    private void removeEpic(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");

        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteEpic(id);
            sendText(httpExchange, "Эпик удален");
        } else {
            sendNotFound(httpExchange);
        }
    }
}