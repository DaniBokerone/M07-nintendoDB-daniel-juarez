package com.project;

public class Consola {
    private String nom;
    private String data;
    private String procesador;
    private String color;
    private int venudes;
    private String imatge;

    // Constructor
    public Consola(String nom, String data, String procesador, String color, int venudes, String imatge) {
        this.nom = nom;
        this.data = data;
        this.procesador = procesador;
        this.color = color;
        this.venudes = venudes;
        this.imatge = imatge;
    }

    // Getters y setters
    public String getNom() {
        return nom;
    }

    public String getData() {
        return data;
    }

    public String getProcesador() {
        return procesador;
    }

    public String getColor() {
        return color;
    }

    public int getVenudes() {
        return venudes;
    }

    public String getImatge() {
        return imatge;
    }
}
