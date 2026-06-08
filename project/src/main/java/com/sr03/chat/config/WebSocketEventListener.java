package com.sr03.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final Map<String, UserSessionInfo> sessionParId = new ConcurrentHashMap<>();
    private final Map<Long, Set<String>> utilisateursParCanal = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String sessionId = headerAccessor.getSessionId();
        String utilisateur = headerAccessor.getFirstNativeHeader("utilisateur");
        String canalIdStr = headerAccessor.getFirstNativeHeader("canalId");

        if (utilisateur != null && canalIdStr != null) {
            try {
                Long canalId = Long.parseLong(canalIdStr);
                sessionParId.put(sessionId, new UserSessionInfo(utilisateur, canalId));
                
                utilisateursParCanal
                    .computeIfAbsent(canalId, k -> Collections.synchronizedSet(new LinkedHashSet<>()))
                    .add(utilisateur);
                    
                logger.info("Utilisateur '{}' connecté au salon {}", utilisateur, canalId);
                diffuserUtilisateurs(canalId);

                // Envoyer un message système
                com.sr03.chat.model.Message systemMessage = new com.sr03.chat.model.Message(
                        utilisateur + " a rejoint le chat.",
                        java.time.LocalTime.now(),
                        "Système"
                );
                messagingTemplate.convertAndSend("/topic/canal/" + canalId, systemMessage);

            } catch (NumberFormatException e) {
                logger.error("CanalId invalide lors de la connexion websocket: {}", canalIdStr);
            }
        } else {
            logger.info("Un utilisateur s'est connecté sans préciser son nom ou le canal. Session ID: {}", sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        UserSessionInfo userInfo = sessionParId.remove(sessionId);
        if (userInfo != null) {
            Set<String> users = utilisateursParCanal.get(userInfo.getCanalId());
            if (users != null) {
                users.remove(userInfo.getUtilisateur());
            }
            logger.info("Utilisateur '{}' déconnecté du salon {}", userInfo.getUtilisateur(), userInfo.getCanalId());
            diffuserUtilisateurs(userInfo.getCanalId());

            // Envoyer un message système
            com.sr03.chat.model.Message systemMessage = new com.sr03.chat.model.Message(
                    userInfo.getUtilisateur() + " a quitté le chat.",
                    java.time.LocalTime.now(),
                    "Système"
            );
            messagingTemplate.convertAndSend("/topic/canal/" + userInfo.getCanalId(), systemMessage);
        }
    }

    @EventListener
    public void handleWebSocketSubscribeListener(org.springframework.web.socket.messaging.SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        
        if (destination != null && destination.startsWith("/topic/canal/") && destination.endsWith("/utilisateurs")) {
            // Extraire le canalId de la destination "/topic/canal/{canalId}/utilisateurs"
            try {
                String[] parts = destination.split("/");
                if (parts.length >= 5) {
                    Long canalId = Long.parseLong(parts[3]);
                    diffuserUtilisateurs(canalId);
                }
            } catch (NumberFormatException e) {
                logger.warn("Impossible d'extraire le canalId de la destination: {}", destination);
            }
        }
    }

    private void diffuserUtilisateurs(Long canalId) {
        Set<String> users = utilisateursParCanal.getOrDefault(canalId, Collections.emptySet());
        messagingTemplate.convertAndSend(
                "/topic/canal/" + canalId + "/utilisateurs",
                new ArrayList<>(users)
        );
    }
    
    public static class UserSessionInfo {
        private String utilisateur;
        private Long canalId;
        
        public UserSessionInfo(String utilisateur, Long canalId) {
            this.utilisateur = utilisateur;
            this.canalId = canalId;
        }
        
        public String getUtilisateur() { return utilisateur; }
        public Long getCanalId() { return canalId; }
    }
}
