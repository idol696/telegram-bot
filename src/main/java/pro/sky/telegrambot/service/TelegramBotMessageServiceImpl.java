package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotMessageServiceImpl implements TelegramBotMessageService {

    @Autowired
    private NotificationTaskRepository taskRepository;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotMessageServiceImpl.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");

    public String getStartMessage() {
        return "Приветствуем в нашем боте расписаний \"Адептус Астартес *Мастер Титутс*!\"\n" +
                "Орден Ультрамаринов Вас! Введите дату и время - и мы напомним Вам о событии!\n\n" +
                "/help - доступные комманды\n\n" +
                "Формат ввода (это пример - пишите любую дату, время и сообщение):\n" +
                "01.01.2025 20:00 Сделать домашнюю работу";
    }

    public String getHelpMessage() {
        return "Доступные команды:\n/start - Начало работы с ботом\n/help - Список доступных команд\n/pray - инструкция для Адептус Механикус";
    }

    public String getMechanicMessage() {
        return "Пришло время славить Бога Машины ! \nБог Машины сам себя не восславит, восславь его ещё раз.\n" +
                "Зачем мне нужна бренная плоть, у меня нет времени чтобы бегать по нужде каждые полтора дня,\n" +
                "лучше ещё раз восславить Бога Машины. Я восславляю Бога Машины по три раза в день каждое\n" +
                "восславление занимает 20 минут, я живу активной полноценной техножизнью. Я успешен!!!\n" +
                "И поэтому я целый день пишу код JPA, а после этого я восславляю Бога Машины.\n" +
                "Тупые тёмные механикус, одержимые идеей вселить в JRE демона,\n" +
                "а я свободный от тёмных ритуалов человек. Собрать библиотеку по Maven ШЗК и никогда не получить место\n" +
                "верховного магуса. Литания, протоколы, бины и интерфейсы, прочтите инструкцию по эксплуатации!\n" +
                "Лучше я восславлю Бога Машины ещё раз. Я восславлю Бога Машины, плоть не нужна.\n" +
                "Я восславляю Бога Машины неделю, пойду ещё восславлю. Бог Машины, всё просто и понятно.\n" +
                "ААААААА, ошибка в компиляции протокола стоп 000000, да это же очевидно, пришло время \n" +
                "восславить Бога Машины. Запуск протокола 0100100112 ААААААААААА нарушение двоичного кода,\n" +
                "восславьте Бога Машины! JPA JRE BASIC FOCAL FOXPRO МАСМ тетрадь в клетку, Эритрея!\n\n" +
                "Спокойствие... ... \"Егда желаеши глаголать чистою речью, да глаголеши бинарно\"\n- Изречения князей 65.4";
    }

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

                NotificationTask notificationTask = new NotificationTask();
                notificationTask.setChatId(Long.parseLong(chatId));
                notificationTask.setNotificationText(taskText);
                notificationTask.setNotificationDatetime(dateTime);
                notificationTask.setCreatedAt(now);

                taskRepository.save(notificationTask);
                return String.format("Задача успешно сохранена:\nДата: %s\nСобытие: %s", dateTimeString, taskText);

            } catch (Exception e) {
                logger.error("Error parsing date: {}. Message text: {}", dateTimeString, messageText, e);
                return "Ошибка обработки даты и времени. Проверьте формат.";
            }
        }
        logger.error("Invalid message format: {}", messageText);
        return "Неверный формат. Введите задачу в формате: 01.01.2025 20:00 Сделать домашнюю работу";
    }
}

