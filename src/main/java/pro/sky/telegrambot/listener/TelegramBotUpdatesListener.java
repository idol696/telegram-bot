package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.TelegramBotMessageService;


import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotMessageService messageService;

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
                    String response = messageService.getCommandMessage(messageText);
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
