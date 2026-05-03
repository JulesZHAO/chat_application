package com.sr03.chat.repository;

import com.sr03.chat.model.Utilisateur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // - save(utilisateur) -> pour créer ou modifier un utilisateur
    // - findAll() -> pour récupérer la liste de tous les utilisateurs
    // - findById(id) -> pour trouver un utilisateur précis
    // - deleteById(id) -> pour supprimer un utilisateur
    Utilisateur findByEmail(String email);

    List<Utilisateur> findByIsActif(Boolean actif);

}