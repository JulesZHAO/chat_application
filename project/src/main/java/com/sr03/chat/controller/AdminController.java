package com.sr03.chat.controller;

import com.sr03.chat.model.Utilisateur;
import com.sr03.chat.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Vérifie si un admin est connecté en cherchant son objet dans la session
    private boolean estConnecte(HttpSession session) {
        return session.getAttribute("utilisateurConnecte") != null;
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String afficherAccueil(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page, // Page 0 par défaut (première page)
            @RequestParam(defaultValue = "5") int size, // 5 utilisateurs par page
            Model model,
            HttpSession session) {

        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

        Page<Utilisateur> pageUtilisateurs;

        // Si le champ de recherche est vide, on cherche tout le monde
        if (keyword.isEmpty()) {
            pageUtilisateurs = utilisateurRepository.findAll(PageRequest.of(page, size));
        }
        // Sinon, on utilise la méthode de recherche
        else {
            pageUtilisateurs = utilisateurRepository
                    .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            keyword, keyword, keyword, PageRequest.of(page, size));
        }

        // On envoie la liste des utilisateurs de la page actuelle
        model.addAttribute("utilisateurs", pageUtilisateurs.getContent());

        // On envoie les infos de pagination pour construire les boutons HTML
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageUtilisateurs.getTotalPages());

        // On renvoie le mot-clé à la page HTML pour le garder affiché dans la barre de
        // recherche
        model.addAttribute("keyword", keyword);

        // On dit à Spring d'afficher le fichier templates/admin/accueil.html
        return "admin/accueil";
    }

    @GetMapping("/admin/users/editer/{id}")
    public String afficherFormulaireEdition(@PathVariable Long id, Model model, HttpSession session) {

        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

        // On cherche l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);

        if (utilisateur == null) {
            return "redirect:/admin"; // Sécurité : si l'ID n'existe pas, on retourne à l'accueil
        }

        // On envoie l'utilisateur trouvé au formulaire
        model.addAttribute("utilisateur", utilisateur);
        return "admin/edit-user";
    }

    @PostMapping("/admin/users/editer/{id}")
    public String mettreAJourUtilisateur(@PathVariable Long id,
                                         @Valid @ModelAttribute Utilisateur utilisateur,
                                         BindingResult result,
                                         HttpSession session) {

        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

        if (result.hasErrors()) {
            return "admin/edit-user";
        }

        utilisateur.setId(id);
        utilisateurRepository.save(utilisateur);
        return "redirect:/admin";
    }

    @GetMapping("/admin/users/desactiver/{id}")
    public String desactiverUtilisateur(@PathVariable Long id, HttpSession session) {
        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

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
    public String reactiverUtilisateur(@PathVariable Long id, HttpSession session) {

        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

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
    public String supprimerUtilisateur(@PathVariable Long id, HttpSession session) {

        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

        // On le supprime définitivement de la base de données
        utilisateurRepository.deleteById(id);
        return "redirect:/admin/desactives";
    }

    @GetMapping("/admin/users/add")
    public String afficherFormulaire(Model model, HttpSession session) {

        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

        // On crée un utilisateur vide pour que le formulaire HTML puisse s'y lier
        model.addAttribute("utilisateur", new Utilisateur());
        return "admin/add-user";
    }

    @PostMapping("/admin/users/add")
    public String sauvegarderUtilisateur(@Valid @ModelAttribute Utilisateur utilisateur,
                                         BindingResult result,
                                         HttpSession session) {

        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

        // S'il y a des erreurs de validation, on retourne au formulaire
        if (result.hasErrors()) {
            return "admin/add-user";
        }

        // Le bouton "Submit" envoie les données ici. On sauvegarde en base de données.
        utilisateurRepository.save(utilisateur);
        // On redirige vers la page d'accueil pour voir le tableau mis à jour
        return "redirect:/admin";
    }

    @GetMapping("/admin/desactives")
    public String afficherUtilisateursDesactives(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpSession session) {

        // Redirige vers la connexion si pas de session active
        if (!estConnecte(session)) return "redirect:/login";

        Page<Utilisateur> pageUtilisateurs;

        // Si pas de recherche : on prend tous les désactivés
        if (keyword.isEmpty()) {
            pageUtilisateurs = utilisateurRepository.findByIsActifFalse(PageRequest.of(page, size));
        }
        // Sinon : on cherche le mot-clé parmi les désactivés
        else {
            pageUtilisateurs = utilisateurRepository.searchDesactives(keyword, PageRequest.of(page, size));
        }

        model.addAttribute("utilisateurs", pageUtilisateurs.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageUtilisateurs.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/desactives";
    }
}