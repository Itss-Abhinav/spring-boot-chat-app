package chatapp.example.chat_app.controller;


import chatapp.example.chat_app.model.ChatMessage;
import chatapp.example.chat_app.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository messageRepository;

    // When a message is sent to /app/chat.sendMessage, this method handles it
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public") // Broadcast the return value to everyone subscribed to /topic/public
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // Save to MySQL
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    // Handles a new user joining the chat
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session so the server remembers who this connection belongs to
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        chatMessage.setContent(chatMessage.getSender() + " joined the chat!");
        messageRepository.save(chatMessage);
        return chatMessage;
    }
}