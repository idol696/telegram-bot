package pro.sky.telegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TelegramBotMessageServiceTest {

    @InjectMocks
    private TelegramBotMessageServiceImpl messageService;

    @Mock
    private NotificationTaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStartMessage_ShouldReturnCorrectMessage() {
        String startMessage = messageService.getStartMessage();
        assertTrue(startMessage.contains("Приветствуем в нашем боте расписаний"));
    }

    @Test
    void getHelpMessage_ShouldReturnCorrectMessage() {
        String startMessage = messageService.getHelpMessage();
        assertTrue(startMessage.contains("Доступные команды:"));
    }

    @Test
    void getPrayMessage_ShouldReturnCorrectMessage() {
        String startMessage = messageService.getMechanicMessage();
        assertTrue(startMessage.contains("Пришло время славить Бога Машины"));
    }

    @Test
    void processMessage_ValidInput_ShouldSaveTask() {
        String chatId = "123456";
        String messageText = "01.01.9999 20:00 Сделать домашнюю работу";

        String response = messageService.processMessage(chatId, messageText);

        assertTrue(response.contains("Задача успешно сохранена"));

        verify(taskRepository, times(1)).save(any(NotificationTask.class));
    }


    @Test
    void processMessage_InvalidDate_ShouldReturnError() {
        String chatId = "123456";
        String messageText = "01.01.2020 20:00 Сделать домашнюю работу";

        String response = messageService.processMessage(chatId, messageText);

        assertTrue(response.contains("Нельзя указать дату и время в прошлом"));
        verify(taskRepository, never()).save(any(NotificationTask.class));
    }

    @Test
    void processMessage_InvalidFormat_ShouldReturnError() {
        String chatId = "123456";
        String messageText = "Неверный формат";

        String response = messageService.processMessage(chatId, messageText);

        assertTrue(response.contains("Неверный формат"));
        verify(taskRepository, never()).save(any(NotificationTask.class));
    }
}
