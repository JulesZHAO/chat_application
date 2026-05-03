package com.sr03.chat.controller;

import com.sr03.chat.model.Utilisateur;
import com.sr03.chat.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
}