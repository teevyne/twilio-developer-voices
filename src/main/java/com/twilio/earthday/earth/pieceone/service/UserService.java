package com.twilio.earthday.earth.pieceone.service;

import com.twilio.earthday.earth.pieceone.model.EcoTask;
import com.twilio.earthday.earth.pieceone.model.EcoTaskRequest;
import com.twilio.earthday.earth.pieceone.model.FollowUserRequest;
import com.twilio.earthday.earth.pieceone.model.User;
import com.twilio.earthday.earth.pieceone.repository.EcoTaskRepository;
import com.twilio.earthday.earth.pieceone.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EcoTaskRepository ecoTaskRepository;

    @Autowired
    private TwilioService twilioService;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUser(Long phoneNumber) {
        System.out.println(userRepository.findById(phoneNumber));
        return userRepository.findById(phoneNumber);
    }

    public List<User> getUsers() {
//        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }

    public EcoTask addEcoTask(EcoTaskRequest ecoTaskRequest) {
        EcoTask ecoTask = new EcoTask();
        User user = userRepository.findByPhoneNumber(ecoTaskRequest.getUserPhoneNumber());
        log.info(String.valueOf(user));
        ecoTask.setTaskName(ecoTaskRequest.getTaskName());
        ecoTask.setCompleted(false);
        ecoTask.setPhoneNumber(ecoTaskRequest.getUserPhoneNumber());
        return ecoTaskRepository.save(ecoTask);
    }

    public EcoTask completeEcoTask(Long taskId) {
        EcoTask ecoTask = ecoTaskRepository.findById(taskId).orElse(null);
        assert ecoTask != null;
        ecoTask.setCompleted(true);
        log.info(String.valueOf(ecoTask.isCompleted()));
        return ecoTaskRepository.save(ecoTask);
    }

    public void sendTaskUpdates(Long taskId) {
        EcoTask ecoTask = ecoTaskRepository.findById(taskId).orElse(null);
        assert ecoTask != null;
        User user = userRepository.findByPhoneNumber(ecoTask.getPhoneNumber());

        List<String> followers = user.getFollowers();
        for (String follower : followers) {
            twilioService.sendMessage(follower, "Your friend has completed a task: " + ecoTask.getTaskName());
        }
    }

    public User followUser(FollowUserRequest followUserRequest) {
        User user = userRepository.findById(followUserRequest.getUserId()).orElse(null);
        assert user != null;
        user.getFollowers().add(followUserRequest.getFollowerPhoneNumber());
        userRepository.save(user);

        return user;
    }

    public void sendNotificationAndReminders(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        String message = "Hello there. Your friend, " + user.getFirstName() + " has completed an " +
                "EcoTask Challenge. Don't to be a part today!";

        for (String follower : user.getFollowers()) {
            twilioService.sendMessage("+234" + follower.substring(1), message);

        }
    }

    public void sendMilestoneNotification(Long userId, EcoTask ecoTask) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        twilioService
                .sendMessage(user.getPhoneNumber(), "Congratulations! You have reached a milestone in your journey" +
                        " to become more environmentally conscious: " + ecoTask.getTaskName());
    }
}
