package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.service.TelegramBotMessageServiceImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TelegramBotUpdatesListenerTest {

    @InjectMocks
    private TelegramBotUpdatesListener listener;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private TelegramBotMessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** ТЕСТИРУЕМ КОРРЕКТНОСТЬ КОММАНД, ИХ ОТПРАВКУ ЧЕРЕЗ МОКИ И РЕАКЦИЮ ЛИСТЕНЕРА **/

    @Test
    void process_ShouldHandleStartCommand() {
        Update update = mockUpdate("/start", 123456L);
        String expectedMessage = "Приветственное сообщение";

        when(messageService.getStartMessage()).thenReturn(expectedMessage);

        listener.process(Collections.singletonList(update)); // Проба синглтона)

        verifySendMessage("123456", expectedMessage);
    }

    @Test
    void process_ShouldHandlePrayCommand() {
        Update update = mockUpdate("/pray", 123456L);
        String expectedMessage = "Молитва механикус";

        when(messageService.getMechanicMessage()).thenReturn(expectedMessage);

        listener.process(Collections.singletonList(update));

        verifySendMessage("123456", expectedMessage);
    }

    @Test
    void process_ShouldHandleHelpCommand() {
        Update update = mockUpdate("/help", 123456L);
        String expectedMessage = "Хелп";

        when(messageService.getHelpMessage()).thenReturn(expectedMessage);

        listener.process(Collections.singletonList(update));

        verifySendMessage("123456", expectedMessage);
    }

    @Test
    void process_ShouldHandleUnknownCommand() {
        Update update = mockUpdate("/unknown", 123456L);
        String expectedMessage = "Неизвестная команда. Используйте /help для списка доступных команд.";

        listener.process(Collections.singletonList(update));

        verifySendMessage("123456", expectedMessage);
    }

    @Test
    void process_ShouldHandleMessageProcessing() {
        Update update = mockUpdate("01.01.2025 20:00 Сделать домашнюю работу", 123456L);
        String expectedMessage = "Задача успешно сохранена";

        when(messageService.processMessage("123456", "01.01.2025 20:00 Сделать домашнюю работу")).thenReturn(expectedMessage);

        listener.process(Collections.singletonList(update));

        verifySendMessage("123456", expectedMessage);
    }

    // Общий метод для проверки отправленных сообщений. Применяем ArgumentCaptor - ловим аргументы для мока
    private void verifySendMessage(String expectedChatId, String expectedText) {
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());

        SendMessage actualMessage = captor.getValue();
        assertEquals(expectedChatId, actualMessage.getParameters().get("chat_id").toString());
        assertEquals(expectedText, actualMessage.getParameters().get("text"));
    }

    // Мокируем Update
    private Update mockUpdate(String text, long chatId) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(text);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);

        return update;
    }
}
