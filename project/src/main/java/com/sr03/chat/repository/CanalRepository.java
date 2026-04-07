package com.sr03.chat.repository;

import com.sr03.chat.model.Canal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CanalRepository extends JpaRepository<Canal, Long> {

    // Retourne la liste de tous les chats créés par un utilisateur
    List<Canal> findByProprietaireId(Long proprietaireId);

    // Retourne la liste de tous les chats d'un utilisateur dont il est invité
    List<Canal> findByInvitesId(Long utilisateurId);
}
