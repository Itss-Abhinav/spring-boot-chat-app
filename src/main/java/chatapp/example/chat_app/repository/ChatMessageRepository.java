package chatapp.example.chat_app.repository;

import chatapp.example.chat_app.model.ChatMessage;
import chatapp.example.chat_app.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // JpaRepository gives us built-in methods like save(), findAll(), etc.
}