package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.User;
import com.example.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        Optional<User> existingUser = Optional.ofNullable(userService.findByUsername(user.getUsername()));
        if (existingUser.isPresent() && userService.checkPassword(user.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setEmail(user.getEmail());
            userService.updateUser(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
