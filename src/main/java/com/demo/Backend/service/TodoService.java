package com.demo.Backend.service;
import com.demo.Backend.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final List<Task> tasks = new ArrayList<>();
    private int currentId = 1;

    // Hardcoded tasks for demonstration
    public TodoService() {
        tasks.add(new Task(currentId++, "Learn Spring Boot", false));
        tasks.add(new Task(currentId++, "Build a To-Do App", false));
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Optional<Task> getTaskById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst();
    }

    public Task addTask(String title) {
        Task newTask = new Task(currentId++, title, false);
        tasks.add(newTask);
        return newTask;
    }

    public Optional<Task> updateTask(int id, String title) {
        Optional<Task> taskOptional = getTaskById(id);
        taskOptional.ifPresent(task -> task.setTitle(title));
        return taskOptional;
    }

    public boolean deleteTask(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    public Optional<Task> toggleTaskCompletion(int id) {
        Optional<Task> taskOptional = getTaskById(id);
        taskOptional.ifPresent(task -> task.setCompleted(!task.isCompleted()));
        return taskOptional;
    }
}

