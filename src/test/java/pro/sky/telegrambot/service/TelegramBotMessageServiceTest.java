package pro.sky.telegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.model.TelegramBotCommand;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.repository.TelegramBotCommandRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TelegramBotMessageServiceTest {

    @InjectMocks
    private TelegramBotMessageServiceImpl messageService;

    @Mock
    private NotificationTaskRepository taskRepository;

    @Mock
    private TelegramBotCommandRepository commandRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCommandMessage_ExistingCommand_ShouldReturnMessage() {
        String command = "/start";
        String expectedMessage = "Приветственное сообщение";

        when(commandRepository.findByCommand(command))
                .thenReturn(Optional.of(new TelegramBotCommand(command, expectedMessage)));

        String result = messageService.getCommandMessage(command);

        assertEquals(expectedMessage, result);
        verify(commandRepository, times(1)).findByCommand(command);
    }

    @Test
    void getCommandMessage_UnknownCommand_ShouldReturnError() {
        String command = "/unknown";

        when(commandRepository.findByCommand(command)).thenReturn(Optional.empty());

        String result = messageService.getCommandMessage(command);

        assertEquals("Неизвестная команда. Используйте /help для списка доступных команд.", result);
        verify(commandRepository, times(1)).findByCommand(command);
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

        assertTrue(response.contains("Ошибка обработки даты и времени"));
        verify(taskRepository, never()).save(any(NotificationTask.class));
    }
}
