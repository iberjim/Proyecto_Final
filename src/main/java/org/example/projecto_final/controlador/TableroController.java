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

    // CONTROL DE MODOS Y DIFICULTAD
    private boolean contraIA = true; // Cambiar a 'false' si en el menú eligen 2 Jugadores
    private String dificultadSeleccionada = "difícil"; // 'fácil', 'medio' o 'difícil'

    @FXML private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML private Button btnReiniciar;
    @FXML private Label lblTurno;
    //Array pa agrupa los 9 botones físicos del tablero.
    private Button[] tableroBotones;

    /**
     * Método de inicialización automática de JavaFX.
     * Se ejecuta justo antes de mostrar la pantalla al usuario.
     * Se encarga de instanciar la lógica, mapear los componentes visuales
     * y configurar el modo de juego seleccionado (Humano o IA).
     */
    @FXML
    public void initialize(){
        partida = new Juego();
        ia = new InteligenciaArtificial();
        tableroBotones = new Button[]{btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22};
        this.contraIA = Utils.modoIA;
        actualizarTextoTurno();
    }
    /**
     * Se ejecuta automáticamente cada vez que el usuario hace clic en una casilla del tablero.
     * Obtiene la posición del botón pulsado, registra el movimiento en la lógica,
     * actualiza la interfaz visual y decide si le toca jugar al Jugador 2 o a la IA.
     * * @param event Objeto que contiene la información del clic.
     */
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

            // RAMIFICACIÓN DE MODOS DE JUEGO:
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
    /**
     * Gestiona el turno de la Inteligencia Artificial.
     * Pide un movimiento a la IA, calcula su posición en el tablero visual,
     * marca la casilla y cede el turno de vuelta al jugador humano si no ha terminado la partida.
     */

    private void ejecutarTurnoIA() {
        int movimientoIA = ia.decidirMovimiento(partida.getTablero(), dificultadSeleccionada);

        if (movimientoIA != -1) {
           //Convertimos el índice lineal (0-8) a coordenadas de matriz (3x3)
            // Ejemplo: Si movimientoIA es 4 -> fila = 4 / 3 = 1 | col = 4 % 3 = 1 (Centro)
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
     * Actualiza el Label de la vista del tablero dependiendo de quién le toque jugar.
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
    /**
     * Comprueba si la partida ha finalizado por victoria o por empate.
     * Si la partida termina, muestra un mensaje al usuario y congela el juego.
     * * @return true si la partida ha terminado (fin del juego), false si se puede seguir jugando.
     */
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
        // Boton para reincial el tablero y poder volver a jugar.
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
    /*
        Metodo para volver al menu con el btn en pantalla-
     */
    @FXML
    void volverAlMenuClick(javafx.event.ActionEvent event) {
        System.out.println("Partida interrumpida. Volviendo al menú principal...");
        org.example.projecto_final.utils.Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/hello-view.fxml");
    }
    // Metodo para que no se pueda sobre escribir las jugadas.
    private void bloquearTablero() {
        for (Button btn : tableroBotones) {
            if (btn != null) {
                btn.setDisable(true);
            }
        }
    }
}
