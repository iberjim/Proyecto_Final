package org.example.projecto_final.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InteligenciaArtificial {

    private Random random = new Random();

    // Matriz con todas las combinaciones posibles para ganar (filas, columnas y diagonales)
    private final int[][] LINEAS_GANADORAS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontales
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Verticales
            {0, 4, 8}, {2, 4, 6}             // Diagonales
    };

    /**
     * Método principal para decidir el movimiento
     */
    public int decidirMovimiento(int[] tablero, String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "fácil":
            case "facil":
                return moverAleatorio(tablero); // 100% Azar

            case "medio":
                // 50% de probabilidad de jugar inteligente, 50% al azar
                if (random.nextBoolean()) {
                    return moverInteligente(tablero, false); // Bloquea pero no prioriza ganar
                } else {
                    return moverAleatorio(tablero);
                }

            case "difícil":
            case "dificil":
                return moverInteligente(tablero, true); // IA Completa (Ataca, defiende y domina el centro)

            default:
                return moverAleatorio(tablero);
        }
    }

    // 1. NIVEL FÁCIL: Elige cualquier casilla vacía sin pensar
    private int moverAleatorio(int[] tablero) {
        List<Integer> libres = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (tablero[i] == 0) {
                libres.add(i);
            }
        }
        if (libres.isEmpty()) return -1;
        return libres.get(random.nextInt(libres.size()));
    }

    // 2 y 3. NIVEL MEDIO/DIFÍCIL: Aplica las reglas lógicas
    private int moverInteligente(int[] tablero, boolean modoDificil) {

        // --- REGLA 1: ¿PUEDO GANAR? (Solo en modo difícil) ---
        if (modoDificil) {
            int casillaGanadora = buscarCasillaClave(tablero, 2); // 2 es la IA (O)
            if (casillaGanadora != -1) {
                return casillaGanadora; // Si encuentra línea de 2 fichas suyas, pone la tercera y gana
            }
        }

        // --- REGLA 2: ¿DEBO BLOQUEAR AL HUMANO? ---
        int casillaBloqueo = buscarCasillaClave(tablero, 1); // 1 es el Humano (X)
        if (casillaBloqueo != -1) {
            return casillaBloqueo; // Si el humano tiene 2 fichas, le bloquea
        }

        // --- REGLA 3: ESTRATEGIA (Ocupar el centro) ---
        if (tablero[4] == 0) {
            return 4; // La casilla del centro (índice 4) es la más importante
        }

        // --- REGLA 4: Si no hay nada especial, juega en cualquier hueco libre ---
        return moverAleatorio(tablero);
    }

    /**
     * Método auxiliar que busca si un jugador tiene 2 fichas alineadas y la tercera vacía.
     * jugador = 1 (humano), jugador = 2 (IA)
     */
    private int buscarCasillaClave(int[] tablero, int jugador) {
        for (int[] linea : LINEAS_GANADORAS) {
            int posA = linea[0];
            int posB = linea[1];
            int posC = linea[2];

            // Caso A: Ficha, Ficha, Vacío (ej: X, X, 0)
            if (tablero[posA] == jugador && tablero[posB] == jugador && tablero[posC] == 0) return posC;
            // Caso B: Ficha, Vacío, Ficha (ej: X, 0, X)
            if (tablero[posA] == jugador && tablero[posC] == jugador && tablero[posB] == 0) return posB;
            // Caso C: Vacío, Ficha, Ficha (ej: 0, X, X)
            if (tablero[posB] == jugador && tablero[posC] == jugador && tablero[posA] == 0) return posA;
        }
        return -1; // No se ha encontrado ninguna jugada de peligro/victoria
    }
}
