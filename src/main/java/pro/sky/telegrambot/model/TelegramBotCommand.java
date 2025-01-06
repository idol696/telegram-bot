package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "telegram_bot_command")
public class TelegramBotCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "command", nullable = false)
    private String command;

    @Column(name = "description", nullable = false, length = 255)
    private String text;

    public TelegramBotCommand() {}

    public TelegramBotCommand(String command, String text) {
        this.command = command;
        this.text = text;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramBotCommand that = (TelegramBotCommand) o;
        return Objects.equals(getCommand(), that.getCommand()) && Objects.equals(getText(), that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommand(), getText());
    }

    @Override
    public String toString() {
        return "TelegramBotCommand{" + "command='" + command + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
