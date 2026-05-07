package model;

import java.util.ArrayList;

public class Ranking {
    private int idranking;
    private int victorias;
    private  int derrotas;
    private int empates;
    private int puntuaciones;
    private ArrayList<Usuario> id_usuarios;

    public enum modo_patida {
        facil,
        medio,
        dificil,
        loco,
        unoversusuno;
    }
    public Ranking(int idranking, int victorias, int derrotas, int empates, int puntuaciones ) {
        this.idranking = idranking;
        this.victorias = victorias;
        this.derrotas = derrotas;
        this.empates = empates;
        this.puntuaciones = puntuaciones;
    }




}
