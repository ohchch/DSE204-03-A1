package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.service.TaskService;
import com.example.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Task createTask(@RequestBody Task task, @RequestParam String username) {
        User user = userService.findByUsername(username);
        return taskService.createTask(task, user);
    }

    @GetMapping
    public List<Task> getTasks(@RequestParam(required = false) String title,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) String priority) {
        if (title != null) {
            return taskService.findTasksByTitle(title);
        } else if (category != null) {
            return taskService.findTasksByCategory(category);
        } else if (priority != null) {
            return taskService.findTasksByPriority(priority);
        } else {
            return taskService.findTasksByTitle("");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.findById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            Task updatedTask = task.get();
            updatedTask.setTitle(taskDetails.getTitle());
            updatedTask.setDescription(taskDetails.getDescription());
            updatedTask.setCategory(taskDetails.getCategory());
            updatedTask.setPriority(taskDetails.getPriority());
            taskService.updateTask(updatedTask);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.findById(id).isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<Task> updateTaskPriority(@PathVariable Long id, @RequestBody String priority) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            Task updatedTask = task.get();
            updatedTask.setPriority(priority);
            taskService.updateTask(updatedTask);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
