package com.sr03.chat.model;

import java.time.LocalTime;

public class Message {

    private String contenu;
    private LocalTime heure;
    private String expediteur;

    public Message() {
    }

    public Message(String contenu, LocalTime heure, String expediteur) {
        this.contenu = contenu;
        this.heure = heure;
        this.expediteur = expediteur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }
}
