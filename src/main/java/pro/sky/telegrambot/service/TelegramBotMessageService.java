package pro.sky.telegrambot.service;

public interface TelegramBotMessageService {

    /**
     * Получение стартового сообщения.
     * @return Стартовое сообщение для пользователя.
     */
    String getStartMessage();

    /**
     * Получение списка доступных команд.
     * @return Список доступных команд.
     */
    String getHelpMessage();

    /**
     * Получение сообщения для команды /pray.
     * @return Сообщение "Молитва механикус".
     */
    String getMechanicMessage();

    /**
     * Обработка пользовательского сообщения.
     * @param chatId ID чата, откуда пришло сообщение.
     * @param messageText Текст сообщения.
     * @return Ответ для пользователя.
     */
    String processMessage(String chatId, String messageText);
}