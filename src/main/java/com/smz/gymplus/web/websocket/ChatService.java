package com.smz.gymplus.web.websocket;

import static com.smz.gymplus.config.WebsocketConfiguration.IP_ADDRESS;

import com.smz.gymplus.security.SecurityUtils;
import com.smz.gymplus.web.websocket.dto.MessageDTO;
import java.security.Principal;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class ChatService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    public ChatService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @SubscribeMapping("/chat/public")
    public void subscribe(StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        String login = SecurityUtils.getCurrentUserLogin().orElse("anonymoususer");
        String ipAddress = stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString();
        log.debug("User {} subscribed to Chat from IP {}", login, ipAddress);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSender("System");
        messageDTO.setTime(Instant.now());
        messageDTO.setMessage(login + " joined the chat");
        messagingTemplate.convertAndSend("/chat/public", messageDTO);
    }

    @MessageMapping("/chat")
    @SendTo("/chat/public")
    public MessageDTO sendChat(@Payload MessageDTO messageDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        messageDTO.setSender(principal.getName());
        return setupMessageDTO(messageDTO, stompHeaderAccessor, principal);
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        // when the user disconnects, send a message saying that they are leaving
        log.info("{} disconnected from the chat websockets", event.getUser().getName());
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSender("System");
        messageDTO.setTime(Instant.now());
        messageDTO.setMessage(event.getUser().getName() + " left the chat");
        messagingTemplate.convertAndSend("/chat/public", messageDTO);
    }

    private MessageDTO setupMessageDTO(MessageDTO messageDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        messageDTO.setTime(Instant.now());
        log.debug("Sending user chat data {}", messageDTO);
        return messageDTO;
    }
}
