package com.sr03.chat.repository;

import com.sr03.chat.model.Utilisateur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // - save(utilisateur) -> pour créer ou modifier un utilisateur
    // - findAll() -> pour récupérer la liste de tous les utilisateurs
    // - findById(id) -> pour trouver un utilisateur précis
    // - deleteById(id) -> pour supprimer un utilisateur
    Utilisateur findByEmail(String email);

    Page<Utilisateur> findByIsActifFalse(Pageable pageable);

    // méthode avec @Query pour chercher UNIQUEMENT parmi les utilisateurs
    // désactivés
    @Query("SELECT u FROM Utilisateur u WHERE u.isActif = false AND " +
            "(LOWER(u.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Utilisateur> searchDesactives(@Param("keyword") String keyword, Pageable pageable);

    // méthode de recherche de Spring
    Page<Utilisateur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrEmailContainingIgnoreCase(String nom,
            String prenom, String email, Pageable pageable);

}