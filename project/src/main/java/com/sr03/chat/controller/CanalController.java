package com.sr03.chat.controller;

import com.sr03.chat.model.Canal;
import com.sr03.chat.model.Utilisateur;
import com.sr03.chat.repository.CanalRepository;
import com.sr03.chat.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/canaux")
public class CanalController {

    @Autowired
    private CanalRepository canalRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping
    public ResponseEntity<?> planifierCanal(@RequestBody CanalCreationRequest request) {
        Optional<Utilisateur> proprietaireOpt = utilisateurRepository.findById(request.getProprietaireId());
        if (proprietaireOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur introuvable.");
        }

        Canal canal = new Canal();
        canal.setTitre(request.getTitre());
        canal.setDescription(request.getDescription());
        canal.setDateHoraire(request.getDateHoraire());
        canal.setDureeValidite(request.getDureeValidite());
        canal.setProprietaire(proprietaireOpt.get());

        return ResponseEntity.ok(canalRepository.save(canal));
    }

    @GetMapping("/{id}")
    public Optional<Canal> getCanalById(@PathVariable Long id) {
        return canalRepository.findById(id);
    }

    @GetMapping("/proprietaire/{id}")
    public List<Canal> getCanauxParPropietaire(@PathVariable Long id) {
        return canalRepository.findByProprietaireId(id);
    }

    @GetMapping("/invite/{id}")
    public List<Canal> getCanauxParInvite(@PathVariable Long id) {
        return canalRepository.findByInvitesId(id);
    }

    @PostMapping("/{canalId}/inviter")
    public ResponseEntity<String> inviter(@PathVariable Long canalId, @RequestParam String email) {
        Optional<Canal> canalOpt = canalRepository.findById(canalId);
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        if (canalOpt.isEmpty() || utilisateur == null) {
            return ResponseEntity.badRequest().body("Canal ou utilisateur introuvable.");
        }
        Canal canal = canalOpt.get();
        if (!canal.getInvites().contains(utilisateur)) {
            canal.getInvites().add(utilisateur);
            canalRepository.save(canal);
        }
        return ResponseEntity.ok("Invitation envoyée.");
    }

    @PutMapping("/{id}")
    public Canal modifierCanal(@PathVariable Long id, @RequestBody Canal canal) {
        canal.setId(id);
        return canalRepository.save(canal);
    }

    @DeleteMapping("/{id}")
    public void supprimerCanal(@PathVariable Long id) {
        canalRepository.deleteById(id);
    }

    public static class CanalCreationRequest {
        private String titre;
        private String description;
        private LocalDateTime dateHoraire;
        private int dureeValidite;
        private Long proprietaireId;

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getDateHoraire() {
            return dateHoraire;
        }

        public void setDateHoraire(LocalDateTime dateHoraire) {
            this.dateHoraire = dateHoraire;
        }

        public int getDureeValidite() {
            return dureeValidite;
        }

        public void setDureeValidite(int dureeValidite) {
            this.dureeValidite = dureeValidite;
        }

        public Long getProprietaireId() {
            return proprietaireId;
        }

        public void setProprietaireId(Long proprietaireId) {
            this.proprietaireId = proprietaireId;
        }
    }
}
