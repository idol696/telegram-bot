package pro.sky.telegrambot.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskRepository taskRepository;

    @Scheduled(fixedRate = 60000)
    public void sendNotifications() {
        List<NotificationTask> tasks = taskRepository.findByNotificationDatetimeBefore(LocalDateTime.now());
        tasks.forEach(task -> {
            telegramBot.execute(new SendMessage(task.getChatId(), task.getNotificationText()));
            taskRepository.delete(task);
        });
    }
}
