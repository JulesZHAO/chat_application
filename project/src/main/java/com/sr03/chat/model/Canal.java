package com.sr03.chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "canaux")
public class Canal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private LocalDateTime dateHoraire;
    private int dureeValidite;

    // Relation n..1 : un canal appartient à un seul propriétaire
    @ManyToOne
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private Utilisateur proprietaire;

    // Relation n..n : un canal possède une liste d'invités
    @ManyToMany
    @JoinTable(name = "canal_invitations", joinColumns = @JoinColumn(name = "canal_id"), inverseJoinColumns = @JoinColumn(name = "utilisateur_id"))
    private List<Utilisateur> invites = new ArrayList<>();

    public Canal() {
    }

    public Canal(String titre, String description, LocalDateTime dateHoraire, int dureeValidite,
            Utilisateur proprietaire) {
        this.titre = titre;
        this.description = description;
        this.dateHoraire = dateHoraire;
        this.dureeValidite = dureeValidite;
        this.proprietaire = proprietaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }

    public List<Utilisateur> getInvites() {
        return invites;
    }

    public void setInvites(List<Utilisateur> invites) {
        this.invites = invites;
    }
}