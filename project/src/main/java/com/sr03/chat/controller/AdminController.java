package com.sr03.chat.controller;

import com.sr03.chat.model.Utilisateur;
import com.sr03.chat.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/")
    public String index() {
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String afficherAccueil(Model model) {
        // On récupère tous les utilisateurs depuis la base H2
        // "model" permet de transporter cette liste vers le fichier HTML
        model.addAttribute("utilisateurs", utilisateurRepository.findAll());

        // On dit à Spring d'afficher le fichier templates/admin/accueil.html
        return "admin/accueil";
    }

    @GetMapping("/admin/users/desactiver/{id}")
    public String desactiverUtilisateur(@PathVariable Long id) {
        // On cherche l'utilisateur par son ID
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        if (utilisateur != null) {
            // On le rend inactif et on sauvegarde
            utilisateur.setActif(false);
            utilisateurRepository.save(utilisateur);
        }
        // On redirige vers l'accueil pour actualiser le tableau
        return "redirect:/admin";
    }

    @GetMapping("/admin/users/reactiver/{id}")
    public String reactiverUtilisateur(@PathVariable Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        if (utilisateur != null) {
            // On le rend actif et on sauvegarde
            utilisateur.setActif(true);
            utilisateurRepository.save(utilisateur);
        }
        // On redirige vers la page des désactivés
        return "redirect:/admin/desactives";
    }

    @GetMapping("/admin/users/supprimer/{id}")
    public String supprimerUtilisateur(@PathVariable Long id) {
        // On le supprime définitivement de la base de données
        utilisateurRepository.deleteById(id);
        return "redirect:/admin/desactives";
    }

    @GetMapping("/admin/users/add")
    public String afficherFormulaire(Model model) {
        // On crée un utilisateur vide pour que le formulaire HTML puisse s'y lier
        model.addAttribute("utilisateur", new Utilisateur());
        return "admin/add-user";
    }

    @PostMapping("/admin/users/add")
    public String sauvegarderUtilisateur(@ModelAttribute Utilisateur utilisateur) {
        // Le bouton "Submit" envoie les données ici. On sauvegarde en base de données.
        utilisateurRepository.save(utilisateur);
        // On redirige vers la page d'accueil pour voir le tableau mis à jour
        return "redirect:/admin";
    }

    @GetMapping("/admin/desactives")
    public String afficherUtilisateursDesactives(Model model) {
        // On récupère uniquement les utilisateurs dont isActif est à "false"
        model.addAttribute("utilisateurs", utilisateurRepository.findByIsActif(false));

        // On renvoie vers la vue correspondante
        return "admin/desactives";
    }
}