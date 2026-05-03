package com.sr03.chat.controller;

import com.sr03.chat.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}