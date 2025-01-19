package com.project;

public class Joc {
    private String nom;
    private int any;
    private String tipus;
    private String descripcio;
    private String imatge;

    // Constructor
    public Joc(String nom, int any, String tipus, String descripcio, String imatge) {
        this.nom = nom;
        this.any = any;
        this.tipus = tipus;
        this.descripcio = descripcio;
        this.imatge = imatge;
    }

    // Getters y setters
    public String getNom() {
        return nom;
    }

    public int getAny() {
        return any;
    }

    public String getTipus() {
        return tipus;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public String getImatge() {
        return imatge;
    }
}
