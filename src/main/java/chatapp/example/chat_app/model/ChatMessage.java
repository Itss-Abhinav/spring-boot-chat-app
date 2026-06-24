package chatapp.example.chat_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String sender;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    // Define the types of messages that can be sent
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}