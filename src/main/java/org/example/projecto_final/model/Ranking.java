package org.example.projecto_final.model;

public class Ranking {
    private int idranking;
    private Usuario usuario;
    private String nombreUsuario; // 👈 Añadimos esta variable para guardar el nombre en texto
    private int victorias;
    private int derrotas;
    private int empates;
    private int puntuaciones;

    // ... (tus enums y código anterior)

    // 🆕 NUEVO CONSTRUCTOR: Recibe el nombre directamente como String
    public Ranking(String nombreUsuario, int victorias, int derrotas, int empates) {
        this.nombreUsuario = nombreUsuario;
        this.victorias = victorias;
        this.derrotas = derrotas;
        this.empates = empates;
    }

    // 🔑 GETTER PARA JAVAFX: Devolverá el nombre de la variable de texto
    public String getNombreUsuario() {
        return this.nombreUsuario;
    }

    // El resto de tus getters normales...
    public int getVictorias() { return victorias; }
    public int getDerrotas() { return derrotas; }
    public int getEmpates() { return empates; }
}