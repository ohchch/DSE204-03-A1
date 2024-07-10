package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task, User user) {
        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<Task> findTasksByTitle(String title) {
        return taskRepository.findByTitleContaining(title);
    }

    public List<Task> findTasksByCategory(String category) {
        return taskRepository.findByCategory(category);
    }

    public List<Task> findTasksByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
