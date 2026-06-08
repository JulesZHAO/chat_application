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

    private final Map<Long, Set<String>> utilisateursParCanal = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/canal/{canalId}/rejoindre")
    public void rejoindre(@DestinationVariable Long canalId, Message message) {
        utilisateursParCanal
                .computeIfAbsent(canalId, k -> Collections.synchronizedSet(new LinkedHashSet<>()))
                .add(message.getExpediteur());
        diffuserUtilisateurs(canalId);
    }

    private void diffuserUtilisateurs(Long canalId) {
        Set<String> users = utilisateursParCanal.getOrDefault(canalId, Collections.emptySet());
        messagingTemplate.convertAndSend(
                "/topic/canal/" + canalId + "/utilisateurs",
                new ArrayList<>(users)
        );
    }

    @MessageMapping("/canal/{canalId}/quitter")
    public void quitter(@DestinationVariable Long canalId, Message message) {
        Set<String> users = utilisateursParCanal.get(canalId);
        if (users != null) {
            users.remove(message.getExpediteur());
        }
        diffuserUtilisateurs(canalId);
    }

    @MessageMapping("/canal/{canalId}/send")
    @SendTo("/topic/canal/{canalId}")
    public Message envoyerMessage(Message message) {
        message.setHeure(LocalTime.now());
        return message;
    }
}
