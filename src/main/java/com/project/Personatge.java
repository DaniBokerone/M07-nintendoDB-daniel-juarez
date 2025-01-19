package com.project;

public class Personatge {
    private String nom;
    private String imatge;
    private String color;
    private String nomDelVideojoc;

    // Constructor
    public Personatge(String nom, String imatge, String color, String nomDelVideojoc) {
        this.nom = nom;
        this.imatge = imatge;
        this.color = color;
        this.nomDelVideojoc = nomDelVideojoc;
    }

    // Getters y setters
    public String getNom() {
        return nom;
    }

    public String getImatge() {
        return imatge;
    }

    public String getColor() {
        return color;
    }

    public String getNomDelVideojoc() {
        return nomDelVideojoc;
    }
}
