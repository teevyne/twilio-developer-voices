package com.twilio.earthday.earth.pieceone.service;

import com.twilio.earthday.earth.pieceone.model.EcoTask;
import com.twilio.earthday.earth.pieceone.model.User;
import com.twilio.earthday.earth.pieceone.repo.EcoTaskRepository;
import com.twilio.earthday.earth.pieceone.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public EcoTask addEcoTask(EcoTask ecoTask, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        ecoTask.setUser(user);
        return ecoTaskRepository.save(ecoTask);
    }

    public EcoTask completeEcoTask(Long taskId) {
        EcoTask ecoTask = ecoTaskRepository.findById(taskId).orElse(null);
        assert ecoTask != null;
        ecoTask.setCompleted(true);
        return ecoTaskRepository.save(ecoTask);
    }

    public void sendTaskUpdates(Long taskId) {
        EcoTask ecoTask = ecoTaskRepository.findById(taskId).orElse(null);
        assert ecoTask != null;
        User user = ecoTask.getUser();
        List<String> followers = user.getFollowers();
        for (String follower : followers) {
            twilioService.sendMessage(follower, "Your friend has completed a task: " + ecoTask.getTaskName());
        }
    }

    public User followUser(Long userId, String follower) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        user.getFollowers().add(follower);
        userRepository.save(user);

        return user;
    }

    public void sendReminders(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        for (String follower : user.getFollowers()) {
            twilioService.sendMessage(follower, "Don't forget to complete your eco-challenges today!");
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
