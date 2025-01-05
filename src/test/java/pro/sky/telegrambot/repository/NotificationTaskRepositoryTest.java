package pro.sky.telegrambot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class NotificationTaskRepositoryTest {

    @Autowired
    private NotificationTaskRepository repository;

    @Test
    void findByNotificationDatetimeBefore_ShouldReturnMatchingTasks() {

        NotificationTask task = new NotificationTask();
        task.setChatId(123L);
        task.setNotificationText("Test Task");
        task.setNotificationDatetime(LocalDateTime.now().minusMinutes(1));
        task.setCreatedAt(LocalDateTime.now());

        repository.save(task);

        List<NotificationTask> tasks = repository.findByNotificationDatetimeBefore(LocalDateTime.now());

        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getNotificationText());
        assertEquals(123L, tasks.get(0).getChatId());
    }

    @Test
    void findByNotificationDatetimeBefore_ShouldReturnEmptyList_WhenNoTasksMatch() {

        NotificationTask task = new NotificationTask();
        task.setChatId(123L);
        task.setNotificationText("Future Task");
        task.setNotificationDatetime(LocalDateTime.now().plusMinutes(1));
        task.setCreatedAt(LocalDateTime.now());

        repository.save(task);

        List<NotificationTask> tasks = repository.findByNotificationDatetimeBefore(LocalDateTime.now());

        assertTrue(tasks.isEmpty());
    }
}
