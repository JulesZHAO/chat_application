package com.sr03.chat.controller;

import com.sr03.chat.model.Utilisateur;
import com.sr03.chat.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping
    public Utilisateur ajouterUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @GetMapping
    public List<Utilisateur> getTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        return utilisateurRepository.findById(id);
    }

    @PutMapping("/{id}")
    public Utilisateur modifierUtilisateur(@PathVariable Long id, @RequestBody Utilisateur nouvelUtilisateur) {
        nouvelUtilisateur.setId(id);
        return utilisateurRepository.save(nouvelUtilisateur);
    }

    @DeleteMapping("/{id}")
    public void supprimerUtilisateur(@PathVariable Long id) {
        utilisateurRepository.deleteById(id);
    }
}
