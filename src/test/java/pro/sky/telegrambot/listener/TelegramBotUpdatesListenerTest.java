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

    static final String ID_VERIFY = "123456";
    static final String CORRECT_COMMAND_VERIFY = "/start";


    /** ТЕСТИРУЕМ КОРРЕКТНОСТЬ КОММАНД, ИХ ОТПРАВКУ ЧЕРЕЗ МОКИ И РЕАКЦИЮ ЛИСТЕНЕРА **/

    @Test
    void process_ShouldHandleCommand() {
        Update update = mockUpdate(CORRECT_COMMAND_VERIFY);
        String expectedMessage = "Приветственное сообщение";

        when(messageService.getCommandMessage(CORRECT_COMMAND_VERIFY)).thenReturn(expectedMessage);

        listener.process(Collections.singletonList(update));

        verifySendMessage(ID_VERIFY, expectedMessage);
    }


    @Test
    void process_ShouldHandleMessageProcessing() {
        Update update = mockUpdate("01.01.2025 20:00 Сделать домашнюю работу");
        String expectedMessage = "Задача успешно сохранена";

        when(messageService.processMessage(ID_VERIFY, "01.01.2025 20:00 Сделать домашнюю работу")).thenReturn(expectedMessage);

        listener.process(Collections.singletonList(update));

        verifySendMessage(ID_VERIFY, expectedMessage);
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
    private Update mockUpdate(String text) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(text);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456L);

        return update;
    }
}
