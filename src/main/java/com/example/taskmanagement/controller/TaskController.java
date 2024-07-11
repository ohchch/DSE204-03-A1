package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.TaskDTO;
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
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO, @RequestParam Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCategory(taskDTO.getCategory());
        task.setPriority(taskDTO.getPriority());
        task.setUser(user);
        return taskService.createTask(task, user);
    }
    

    @GetMapping
    public List<TaskDTO> getTasks(@RequestParam(required = false) String title,
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
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Optional<TaskDTO> task = taskService.findById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDetails) {
        Optional<TaskDTO> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            TaskDTO taskDTO = taskOptional.get();
            taskDTO.setTitle(taskDetails.getTitle());
            taskDTO.setDescription(taskDetails.getDescription());
            taskDTO.setCategory(taskDetails.getCategory());
            taskDTO.setPriority(taskDetails.getPriority());
            TaskDTO updatedTask = taskService.updateTask(taskDTO);
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
    public ResponseEntity<TaskDTO> updateTaskPriority(@PathVariable Long id, @RequestBody String priority) {
        Optional<TaskDTO> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            TaskDTO taskDTO = taskOptional.get();
            taskDTO.setPriority(priority);
            TaskDTO updatedTask = taskService.updateTask(taskDTO);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
