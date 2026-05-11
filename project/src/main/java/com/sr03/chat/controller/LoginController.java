package com.sr03.chat.controller;

import com.sr03.chat.model.Utilisateur;
import com.sr03.chat.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Controller
public class LoginController {

    // Nombre maximum de tentatives avant blocage du compte
    private static final int MAX_TENTATIVES = 5;

    // Durée du blocage en minutes après trop de tentatives échouées
    private static final int MINUTES_BLOGAGE = 15;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Affiche la page de connexion
    @GetMapping("/login")
    public String afficherLogin() {
        return "login";
    }

    // Traite le formulaire de connexion soumis par l'utilisateur
    @PostMapping("/login")
    public String traiterLogin(
            @RequestParam String email,
            @RequestParam String motDePasse,
            HttpSession session,
            Model model) {

        // 1. Cherche l'utilisateur par son email dans la base de données
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);

        // 2. Si aucun compte ne correspond à cet email
        if (utilisateur == null) {
            model.addAttribute("erreur", "Email ou mot de passe incorrect.");
            return "login";
        }

        // 3. Vérifie si le compte est temporairement bloqué
        if (utilisateur.getCompteBloqueJusqua() != null &&
                LocalDateTime.now().isBefore(utilisateur.getCompteBloqueJusqua())) {
            model.addAttribute("erreur",
                    "Compte bloqué temporairement. Réessayez après " +
                    utilisateur.getCompteBloqueJusqua().toLocalTime().withSecond(0).withNano(0));
            return "login";
        }

        // 4. Vérifie si le mot de passe est incorrect
        if (!utilisateur.getMotDePasse().equals(motDePasse)) {
            int tentatives = utilisateur.getLoginTentative() + 1;
            utilisateur.setLoginTentative(tentatives);

            // 5. Si le nombre maximum de tentatives est atteint → bloquer le compte
            if (tentatives >= MAX_TENTATIVES) {
                utilisateur.setLoginTentative(0);
                utilisateur.setCompteBloqueJusqua(LocalDateTime.now().plusMinutes(MINUTES_BLOGAGE));
                utilisateurRepository.save(utilisateur);
                model.addAttribute("erreur",
                        "Trop de tentatives. Compte bloqué pendant " + MINUTES_BLOGAGE + " minutes.");
                return "login";
            }

            // Sauvegarde le compteur de tentatives et affiche le nombre restant
            utilisateurRepository.save(utilisateur);
            model.addAttribute("erreur",
                    "Email ou mot de passe incorrect. (" + tentatives + "/" + MAX_TENTATIVES + " tentatives)");
            return "login";
        }

        // 6. Connexion réussie : réinitialise les tentatives et crée la session
        utilisateur.setLoginTentative(0);
        utilisateur.setCompteBloqueJusqua(null);
        utilisateurRepository.save(utilisateur);

        // Stocke l'utilisateur dans la session pour les pages protégées
        session.setAttribute("utilisateurConnecte", utilisateur);
        return "redirect:/admin";
    }

    // Détruit la session et redirige vers la page de connexion
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
