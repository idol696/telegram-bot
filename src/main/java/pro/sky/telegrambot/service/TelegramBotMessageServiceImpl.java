package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.TelegramBotCommand;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.repository.TelegramBotCommandRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotMessageServiceImpl implements TelegramBotMessageService {

    @Autowired
    private NotificationTaskRepository taskRepository;

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotMessageService.class);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");

    @Autowired
    private TelegramBotCommandRepository commandRepository;

    @Override
    public String getCommandMessage(String command) {
        logger.info("Retrieving message for command: {}", command);
        try {
            Optional<TelegramBotCommand> foundCommand = commandRepository.findByCommand(command);
            if (foundCommand.isPresent()) {
                logger.debug("Command found: {}", command);
                return foundCommand.get().getText();
            } else {
                logger.warn("Command not found: {}", command);
                return "Неизвестная команда. Используйте /help для списка доступных команд.";
            }
        } catch (Exception e) {
            logger.error("Error retrieving command: {}", command, e);
            return "Ошибка загрузки команды. Попробуйте позже.";
        }
    }

    @Override
    public String processMessage(String chatId, String messageText) {
        Matcher matcher = pattern.matcher(messageText);
        if (matcher.matches()) {
            String dateTimeString = matcher.group(1);
            String taskText = matcher.group(3);
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
                LocalDateTime now = LocalDateTime.now();

                if (dateTime.isBefore(now)) {
                    logger.error("Invalid date entered: {}. Date must not be in the past.", dateTimeString);
                    return "Ошибка: Нельзя указать дату и время в прошлом. Пожалуйста, введите корректные данные.";
                }

                NotificationTask notificationTask = new NotificationTask(
                        Long.parseLong(chatId),
                        taskText,
                        dateTime,
                        now
                );

                taskRepository.save(notificationTask);
                return String.format("Задача успешно сохранена:\nДата: %s\nСобытие: %s", dateTimeString, taskText);

            } catch (Exception e) {
                logger.error("Error parsing date: {}. Message text: {}", dateTimeString, messageText, e);
                return "Ошибка обработки даты и времени. Проверьте формат.";
            }
        }
        return "Ошибка обработки даты и времени";
    }
}

