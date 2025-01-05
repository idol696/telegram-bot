package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.TelegramBotMessageServiceImpl;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final Map<LocalDateTime, String> tasks = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");


    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskRepository taskRepository;

    @Autowired
    private TelegramBotMessageServiceImpl messageService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.debug("Processing update: {}", update);
            if (update.message() != null && update.message().text() != null) {
                String chatId = String.valueOf(update.message().chat().id());
                String messageText = update.message().text();
                logger.info("Processing message: {}", messageText);

                if (messageText.startsWith("/")) {
                    String response;
                    switch (messageText) {
                        case "/start":
                            response = messageService.getStartMessage();
                            break;
                        case "/pray":
                            response = messageService.getMechanicMessage();
                            break;
                        case "/help":
                            response = messageService.getHelpMessage();
                            break;
                        default:
                            response = "Неизвестная команда. Используйте /help для списка доступных команд.";
                            logger.warn("Unknown command received: {}", messageText);
                            break;
                    }
                    telegramBot.execute(new SendMessage(chatId, response));
                } else {
                    String response = messageService.processMessage(chatId, messageText);
                    telegramBot.execute(new SendMessage(chatId, response));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
