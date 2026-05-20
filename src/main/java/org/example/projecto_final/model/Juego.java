package org.example.projecto_final.model;

public class Juego {

    private int[] tablero = new int[9];
    private int turno = 1;
    private boolean hayGanador = false;

    public void cambiarTurno() {
        this.turno = (this.turno == 1) ? 2 : 1;
    }

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
        return exito; // Único return
    }

    public boolean comprobarSiGana() {

        return (chequearLinea(0, 1, 2) || chequearLinea(3, 4, 5) || chequearLinea(6, 7, 8) || //de izquierda a derecha
                chequearLinea(0, 3, 6) || chequearLinea(1, 4, 7) || chequearLinea(2, 5, 8) || // de abajo a arriba
                chequearLinea(0, 4, 8) || chequearLinea(2, 4, 6)); // diagonal
    }

    private boolean chequearLinea(int a, int b, int c) {
        return tablero[a] != 0 && tablero[a] == tablero[b] && tablero[a] == tablero[c];
    }

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
        String letra = "O"; // Valor por defecto

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
