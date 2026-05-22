package org.example.projecto_final.model;

public class Juego {

    private int[] tablero = new int[9];
    private int turno = 1;
    private boolean hayGanador = false;

    /**
     * Alterna de forma automática el turno del juego.
     * Si el turno actual es del Jugador 1, pasa al Jugador 2 (o IA), y viceversa,

     */
    public void cambiarTurno() {

        this.turno = (this.turno == 1) ? 2 : 1;
    }
    /**
     * Intenta registrar un movimiento en el tablero lógico del juego.
     * Convierte las coordenadas en un índice lineal, verifica si la jugada
     * es válida y actualiza el estado del juego si se produce una victoria.
     *
     * @param fila Fila seleccionada (0, 1 o 2).
     * @param col  Columna seleccionada (0, 1 o 2).
     * @return true si la casilla estaba vacía y se pudo marcar con éxito; false en caso contrario.
     */
    public boolean marcarCasilla(int fila, int col) {
        boolean exito = false;
        int posicion = fila * 3 + col;

        if (tablero[posicion] == 0 && !hayGanador) {
            tablero[posicion] = turno;
            exito = true;

            if (comprobarSiGana()) {
                hayGanador = true;
            }
        }
        return exito; 
    }
    /**
     * Evalúa el tablero para determinar si el último movimiento ha producido una victoria.
     * Comprueba de forma exhaustiva las 8 líneas posibles de Tres en Raya (3 horizontales,
     * 3 verticales y 2 diagonales) llamando al método auxiliar 'chequearLinea'.
     *
     * @return true si alguna de las líneas contiene tres fichas idénticas del mismo jugador;
     * false si todavía no hay un ganador.
     */
    public boolean comprobarSiGana() {

        return (chequearLinea(0, 1, 2) || chequearLinea(3, 4, 5) || chequearLinea(6, 7, 8) || //de izquierda a derecha
                chequearLinea(0, 3, 6) || chequearLinea(1, 4, 7) || chequearLinea(2, 5, 8) || // de abajo a arriba
                chequearLinea(0, 4, 8) || chequearLinea(2, 4, 6)); // diagonal
    }
    /**
     * Comprueba si tres casillas específicas del tablero forman una línea ganadora.
     * Verifica que la primera casilla no esté vacía y que las otras dos tengan
     * exactamente la misma ficha (el mismo jugador).
     *
     * @param a Índice de la primera casilla (0-8).
     * @param b Índice de la segunda casilla (0-8).
     * @param c Índice de la tercera casilla (0-8).
     * @return true si las tres casillas están marcadas por el mismo jugador y no están vacías;
     * false en caso contrario.
     */
    private boolean chequearLinea(int a, int b, int c) {
        return tablero[a] != 0 && tablero[a] == tablero[b] && tablero[a] == tablero[c];
    }
    /**
     * Comprueba si todas las casillas del tablero han sido ocupadas.
     * Este método se utiliza para determinar si la partida ha terminado en empate
     * cuando ningún jugador ha logrado hacer un Tres en Raya.
     *
     * @return true si el tablero está completamente lleno (empate);
     * false si todavía queda al menos una casilla vacía.
     */
    public boolean tableroLleno() {
        boolean lleno = true; // Empezamos asumiendo que está lleno
        for (int i = 0; i < tablero.length; i++) {
            if (tablero[i] == 0) {
                lleno = false;
            }
        }
        return lleno;
    }

    public int getTurno() {
        return turno;
    }
    public String getTurnoActual() {
        String letra = "O"; 

        if (this.turno == 1) {
            letra = "X";
        }

        return letra;
    }
    public boolean hayGanador() {
        return hayGanador;
    }

    public int[] getTablero() {
        return this.tablero;
    }
}
