package com.sr03.chat.controller;

import com.sr03.chat.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/canal/{canalId}/send")
    @SendTo("/topic/canal/{canalId}")
    public Message envoyerMessage(Message message) {
        message.setHeure(LocalTime.now());
        return message;
    }
}
