package com.sr03.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Le mot de passe doit contenir 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial"
    )
    private String motDePasse;

    private boolean isAdmin;
    private boolean isActif = true; // Actif par défaut lors de la création
    private int loginTentatives = 0;
    private java.time.LocalDateTime compteBloqueJusqua;


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

    public int getLoginTentative() {return loginTentatives;}

    public void setLoginTentative(int loginTentatives) {
        this.loginTentatives = loginTentatives;
    }

    public java.time.LocalDateTime getCompteBloqueJusqua() {
        return compteBloqueJusqua;
    }

    public void setCompteBloqueJusqua(java.time.LocalDateTime compteBloqueJusqua) {
        this.compteBloqueJusqua = compteBloqueJusqua;
    }


}