package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.TelegramBotCommand;

import java.util.Optional;

@Repository
public interface TelegramBotCommandRepository extends JpaRepository<TelegramBotCommand, Long> {
    Optional<TelegramBotCommand> findByCommand(String command);
}
