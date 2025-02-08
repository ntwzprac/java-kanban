package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    getTasks(exchange);
                    break;
                case "POST":
                    addTask(exchange);
                    break;
                case "DELETE":
                    removeTask(exchange);
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

    private void getTasks(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");

        if (pathParts.length == 2) {
            List<Task> tasks = taskManager.getTasks();
            String jsonResponse = gson.toJson(tasks);
            sendText(httpExchange, jsonResponse);
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTask(id);
            if (task != null) {
                sendText(httpExchange, gson.toJson(task));
            } else {
                sendNotFound(httpExchange);
            }
        } else {
            sendNotFound(httpExchange);
        }
    }

    private void addTask(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes());
        JsonElement jsonElement = JsonParser.parseString(requestBody);
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task task = gson.fromJson(jsonElement, taskType);

        if (task.getName() == null || task.getName().isEmpty()) {
            sendHasInteractions(httpExchange);
            return;
        }

        if (task.getTaskStatus() == null) {
            task.setTaskStatus(TaskStatus.NEW);
        }

        if (task.getId() != 0) {
            taskManager.updateTask(task);
            sendCreated(httpExchange);
        } else {
            taskManager.addTask(task);
            sendText(httpExchange, gson.toJson(task));
        }
    }

    private void removeTask(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");

        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteTask(id);
            sendText(httpExchange, "Задача удалена");
        } else {
            sendNotFound(httpExchange);
        }
    }
}