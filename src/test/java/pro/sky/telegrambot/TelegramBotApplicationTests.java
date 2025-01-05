package pro.sky.telegrambot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TelegramBotApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertNotNull(applicationContext, "Spring ApplicationContext должен быть загружен.");
	}

	@Test
	void allBeansLoaded() {
		assertNotNull(applicationContext.getBean("telegramBot"), "TelegramBot должен быть загружен.");
		assertNotNull(applicationContext.getBean("telegramBotUpdatesListener"), "TelegramBotUpdatesListener должен быть загружен.");
		assertNotNull(applicationContext.getBean("notificationTaskRepository"), "NotificationTaskRepository должен быть загружен.");
		assertNotNull(applicationContext.getBean("notificationScheduler"), "NotificationScheduler должен быть загружен.");
		assertNotNull(applicationContext.getBean("telegramBotMessageService"), "TelegramBotMessageService должен быть загружен.");
	}

	@Test
	void testTelegramBotConfiguration() {
		Object telegramBot = applicationContext.getBean("telegramBot");
		assertNotNull(telegramBot, "Бот должен быть сконфигурирован.");
	}

	@Test
	void testNotificationRepository() {
		Object repository = applicationContext.getBean("notificationTaskRepository");
		assertNotNull(repository, "Репозиторий NotificationTaskRepository должен быть доступен.");
	}

	@Test
	void testUpdatesListener() {
		Object listener = applicationContext.getBean("telegramBotUpdatesListener");
		assertNotNull(listener, "TelegramBotUpdatesListener должен быть загружен.");
	}

	@Test
	void testScheduler() {
		Object scheduler = applicationContext.getBean("notificationScheduler");
		assertNotNull(scheduler, "NotificationScheduler должен быть загружен.");
	}
}
