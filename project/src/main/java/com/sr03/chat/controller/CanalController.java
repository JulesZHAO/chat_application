package com.sr03.chat.controller;

import com.sr03.chat.model.Canal;
import com.sr03.chat.model.Utilisateur;
import com.sr03.chat.repository.CanalRepository;
import com.sr03.chat.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/canaux")
public class CanalController {

    @Autowired
    private CanalRepository canalRepository;

    @PostMapping
    public Canal planifierCanal(@RequestBody Canal canal) {
        return canalRepository.save(canal);
    }

    @GetMapping("/proprietaire/{id}")
    public List<Canal> getCanauxParPropietaire(@PathVariable Long id) {
        return canalRepository.findByProprietaireId(id);
    }

    @GetMapping("/invite/{id}")
    public List<Canal> getCanauxParInvite(@PathVariable Long id) {
        return canalRepository.findByInvitesId(id);
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
}
