package com.sr03.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private boolean isAdmin;
    private boolean isActif = true; // Actif par défaut lors de la création

    // Relation 1..n : un utilisateur peut créer plusieurs canaux
    @JsonIgnore
    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL)
    private List<Canal> canauxCrees = new ArrayList<>();

    // Relation n..n : un utilisateur peut être invité à plusieurs canaux
    @JsonIgnore
    @ManyToMany(mappedBy = "invites")
    private List<Canal> canauxInvites = new ArrayList<>();

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String email, String motDePasse, boolean isAdmin) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.isAdmin = isAdmin;
        this.isActif = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isActif() {
        return isActif;
    }

    public void setActif(boolean actif) {
        isActif = actif;
    }

    public List<Canal> getCanauxCrees() {
        return canauxCrees;
    }

    public void setCanauxCrees(List<Canal> canauxCrees) {
        this.canauxCrees = canauxCrees;
    }

    public List<Canal> getCanauxInvites() {
        return canauxInvites;
    }

    public void setCanauxInvites(List<Canal> canauxInvites) {
        this.canauxInvites = canauxInvites;
    }
}