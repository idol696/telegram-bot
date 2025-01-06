package pro.sky.telegrambot.service;

public interface TelegramBotMessageService {
    String processMessage(String chatId, String messageText);
    String getCommandMessage(String command);
}