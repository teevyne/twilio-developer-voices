package com.twilio.earthday.earth.pieceone.web;

import com.twilio.earthday.earth.pieceone.model.EcoTask;
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
    public ResponseEntity<EcoTask> createTask(@RequestBody EcoTask task, @RequestBody Long userId) {
        return new ResponseEntity<>(userService.addEcoTask(task, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public EcoTask completeEcoTask(@PathVariable("id") Long taskId) {
        return userService.completeEcoTask(taskId);
    }

    @PostMapping("/{taskId}/send-updates")
    public ResponseEntity<String> sendTaskUpdates(@PathVariable Long taskId) {
        userService.sendTaskUpdates(taskId);
        return ResponseEntity.ok("Task updates sent successfully");
    }

    @PostMapping("/user/{userId}/follower")
    public ResponseEntity<User> followUser(@PathVariable Long userId, @RequestParam String follower) {
        User user = userService.followUser(userId, follower);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/{userId}/reminders")
    public ResponseEntity<String> sendReminders(@PathVariable Long userId) {
        userService.sendReminders(userId);
        return ResponseEntity.ok("Reminders sent successfully");
    }

    @PostMapping("/user/{userId}/milestone")
    public ResponseEntity<String> sendMilestoneNotification(@PathVariable Long userId, @RequestBody EcoTask ecoTask) {
        userService.sendMilestoneNotification(userId, ecoTask);
        return ResponseEntity.ok("Milestone notification sent successfully");
    }
}