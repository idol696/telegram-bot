package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "notification_text", nullable = false, length = 255)
    private String notificationText;

    @Column(name = "notification_datetime", nullable = false)
    private LocalDateTime notificationDatetime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "changed", nullable = false)
    private boolean changed;

    public NotificationTask() {}

    public NotificationTask(Long chatId, String notificationText, LocalDateTime notificationDatetime, LocalDateTime createdAt) {
        this.chatId = chatId;
        this.notificationText = notificationText;
        this.notificationDatetime = notificationDatetime;
        this.createdAt = createdAt;
        this.changed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
    }

     public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public LocalDateTime getNotificationDatetime() {
        return notificationDatetime;
    }

    public void setNotificationDatetime(LocalDateTime notificationDatetime) {
        this.notificationDatetime = notificationDatetime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return isChanged() == that.isChanged() && Objects.equals(getId(), that.getId()) && Objects.equals(getChatId(),
                that.getChatId()) && Objects.equals(getNotificationText(), that.getNotificationText())
                && Objects.equals(getNotificationDatetime(), that.getNotificationDatetime())
                && Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getChatId(), getNotificationText(), getNotificationDatetime(), getCreatedAt(), isChanged());
    }

    @Override
    public String toString() {
        return "NotificationTask{" + "id=" + id +
                ", chatId=" + chatId +
                ", notificationText='" + notificationText + '\'' +
                ", notificationDatetime=" + notificationDatetime +
                ", createdAt=" + createdAt +
                ", changed=" + changed +
                '}';
    }
}
