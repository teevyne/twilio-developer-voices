package com.twilio.earthday.earth.pieceone.web;

import com.twilio.earthday.earth.pieceone.model.EcoTask;
import com.twilio.earthday.earth.pieceone.model.EcoTaskRequest;
import com.twilio.earthday.earth.pieceone.model.FollowUserRequest;
import com.twilio.earthday.earth.pieceone.model.User;
import com.twilio.earthday.earth.pieceone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/eco-challenge")
public class EcoChallengeController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/task")
    public ResponseEntity<EcoTask> createTask(@RequestBody EcoTaskRequest ecoTaskRequest) {
        return new ResponseEntity<>(userService.addEcoTask(ecoTaskRequest), HttpStatus.CREATED);
    }

    @PutMapping("/task/{id}")
    public EcoTask completeEcoTask(@PathVariable("id") Long taskId) {
        return userService.completeEcoTask(taskId);
    }

    @PostMapping("/{taskId}/send-updates")
    public ResponseEntity<String> sendTaskUpdates(@PathVariable Long taskId) {
        userService.sendTaskUpdates(taskId);
        return ResponseEntity.ok("Task updates sent successfully");
    }

    @PostMapping("/user/follower/add")
    public ResponseEntity<User> followUser(@RequestBody FollowUserRequest followUserRequest) {
        User user = userService.followUser(followUserRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/{userId}/notification")
    public ResponseEntity<?> sendReminders(@PathVariable Long userId) {
        userService.sendNotificationAndReminders(userId);
        return ResponseEntity.ok("Notification sent successfully to users' followers");
    }

    @PostMapping("/user/{userId}/milestone")
    public ResponseEntity<String> sendMilestoneNotification(@PathVariable Long userId, @RequestBody EcoTask ecoTask) {
        userService.sendMilestoneNotification(userId, ecoTask);
        return ResponseEntity.ok("Milestone notification sent successfully");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {

        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/users/")
    public ResponseEntity<?> getUsers() {

        return ResponseEntity.ok(userService.getUsers());
    }
}