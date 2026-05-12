package model;

import java.util.ArrayList;

public class Modo_juego {

    private int id_resultado;
    private String ganador;
    private ArrayList<Partidas> id_partida;

    public enum tipo{
        VICTORIA,
        EMPATE,
        DERROTA
    }

    public Modo_juego(int id_resultado, String ganador) {
        this.id_resultado = id_resultado;
        this.ganador = ganador;
        this.id_partida = new ArrayList<>();
    }




}
