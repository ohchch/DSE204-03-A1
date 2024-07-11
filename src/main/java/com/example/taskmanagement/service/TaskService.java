package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public TaskDTO createTask(Task task, User user) {
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return toDTO(savedTask);
    }

    public List<TaskDTO> findTasksByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitleContaining(title);
        return toDTOs(tasks);
    }

    public List<TaskDTO> findTasksByCategory(String category) {
        List<Task> tasks = taskRepository.findByCategory(category);
        return toDTOs(tasks);
    }

    public List<TaskDTO> findTasksByPriority(String priority) {
        List<Task> tasks = taskRepository.findByPriority(priority);
        return toDTOs(tasks);
    }

    public Optional<TaskDTO> findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(this::toDTO);
    }

    public TaskDTO updateTask(TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.getId())
                                  .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCategory(taskDTO.getCategory());
        task.setPriority(taskDTO.getPriority());
        Task updatedTask = taskRepository.save(task);
        return toDTO(updatedTask);
    }
    

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO toDTO(Task task) {
        return task.toDTO();
    }

    private List<TaskDTO> toDTOs(List<Task> tasks) {
        return tasks.stream().map(Task::toDTO).collect(Collectors.toList());
    }
}
