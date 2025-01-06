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
        // Извлекаем только те задачи, которые имеют время меньше текущего и не были изменены
        List<NotificationTask> tasks = taskRepository.findByNotificationDatetimeBeforeAndChangedFalse(LocalDateTime.now());

        tasks.forEach(task -> {
            // Отправляем уведомление через Telegram
            telegramBot.execute(new SendMessage(task.getChatId(), task.getNotificationText()));

            // Обновляем поле "changed", чтобы задача больше не была отправлена
            task.setChanged(true);
            taskRepository.save(task);
        });
    }
}
