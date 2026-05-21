package org.example.projecto_final.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.example.projecto_final.model.Juego;
import org.example.projecto_final.model.Usuario;
import org.example.projecto_final.model.InteligenciaArtificial;
import org.example.projecto_final.utils.Utils;

import static org.example.projecto_final.utils.Utils.mostrarAlerta;

public class TableroController {

    private Juego partida;
    private InteligenciaArtificial ia;

    // ⚙️ CONTROL DE MODOS Y DIFICULTAD
    private boolean contraIA = true; // Cambiar a 'false' si en el menú eligen 2 Jugadores
    private String dificultadSeleccionada = "difícil"; // 'fácil', 'medio' o 'difícil'

    @FXML private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML private Button btnReiniciar;
    @FXML private Label lblTurno;

    private Button[] tableroBotones;

    @FXML
    public void initialize(){
        partida = new Juego();
        ia = new InteligenciaArtificial();
        tableroBotones = new Button[]{btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22};
        this.contraIA = Utils.modoIA;
        actualizarTextoTurno();
    }

    @FXML
    void CasillaClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        int fila = (GridPane.getRowIndex(btn) == null) ? 0 : GridPane.getRowIndex(btn);
        int col = (GridPane.getColumnIndex(btn) == null) ? 0 : GridPane.getColumnIndex(btn);

        // Intentamos marcar la casilla en la lógica del juego
        if (partida.marcarCasilla(fila, col)) {
            // Pintamos la ficha del jugador que tenga el turno actual (X o O)
            btn.setText(partida.getTurnoActual());
            btn.setDisable(true);

            // Comprobamos si este movimiento ha terminado la partida
            if (comprobarEstadoPartida()) {
                return;
            }

            // Si la partida continúa, cambiamos de turno
            partida.cambiarTurno();

            // 🤖 RAMIFICACIÓN DE MODOS DE JUEGO:
            if (contraIA) {
                // MODO CONTRA LA IA:
                lblTurno.setText("Turno de la IA (O)...");
                ejecutarTurnoIA(); // La IA mueve inmediatamente de forma automática
            } else {
                // MODO 2 JUGADORES (Humano contra Humano):
                // No llamamos a la IA, simplemente cambiamos el texto de la pantalla para el siguiente jugador
                actualizarTextoTurno();
            }
        }
    }

    private void ejecutarTurnoIA() {
        int movimientoIA = ia.decidirMovimiento(partida.getTablero(), dificultadSeleccionada);

        if (movimientoIA != -1) {
            int fila = movimientoIA / 3;
            int col = movimientoIA % 3;

            if (partida.marcarCasilla(fila, col)) {
                Button btnIA = tableroBotones[movimientoIA];
                btnIA.setText("O");
                btnIA.setDisable(true);

                if (comprobarEstadoPartida()) {
                    return;
                }

                // Devuelve el turno al humano
                partida.cambiarTurno();
                actualizarTextoTurno();
            }
        }
    }

    /**
     * Actualiza el Label superior dependiendo de quién le toque jugar
     */
    private void actualizarTextoTurno() {
        if (partida.getTurno() == 1) {
            if (Usuario.usuarioSesion != null) {
                lblTurno.setText("Turno de: " + Usuario.usuarioSesion.getNombre() + " (X)");
            } else {
                lblTurno.setText("Turno de: Jugador 1 (X)");
            }
        } else {
            // Si es turno de la 'O' y no es la IA, significa que juega el Jugador 2 humano
            lblTurno.setText("Turno de: Jugador 2 (O)");
        }
    }

    private boolean comprobarEstadoPartida() {
        if (partida.comprobarSiGana()) {
            String ganadorVisual;
            if (partida.getTurno() == 1) {
                ganadorVisual = (Usuario.usuarioSesion != null) ? Usuario.usuarioSesion.getNombre() : "Jugador 1 (X)";
            } else {
                ganadorVisual = contraIA ? "Inteligencia Artificial (O)" : "Jugador 2 (O)";
            }

            mostrarAlerta("Ganador", "¡Ha ganado " + ganadorVisual + "!");
            bloquearTablero();
            return true;
        } else if (partida.tableroLleno()) {
            mostrarAlerta("Empate", "¡Tablero lleno! Buen intento.");
            return true;
        }
        return false;
    }

    @FXML
    void ReiniciarClick(ActionEvent event) {
        partida = new Juego();
        actualizarTextoTurno();

        for (Button btn : tableroBotones) {
            if (btn != null) {
                btn.setText("");
                btn.setDisable(false);
            }
        }
    }

    private void bloquearTablero() {
        for (Button btn : tableroBotones) {
            if (btn != null) {
                btn.setDisable(true);
            }
        }
    }
}