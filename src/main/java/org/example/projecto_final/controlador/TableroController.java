package org.example.projecto_final.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.example.projecto_final.model.Juego;
import org.example.projecto_final.model.Usuario;
import org.example.projecto_final.model.Partidas; // Importamos el nuevo modelo
import org.example.projecto_final.model.InteligenciaArtificial;
import org.example.projecto_final.DAO.PartidasDAO; // Importamos el nuevo DAO
import org.example.projecto_final.utils.Utils;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.example.projecto_final.utils.Utils.mostrarAlerta;

public class TableroController {
    private Juego partida;
    private InteligenciaArtificial ia;

    //  CONTADOR DE MOVIMIENTOS: Para calcular la puntuación por eficiencia
    private int movimientosJugador = 0;

    // CONTROL DE MODOS Y DIFICULTAD
    private boolean contraIA = true;
    private String dificultadSeleccionada = "difícil";

    @FXML private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML private Button btnReiniciar;
    @FXML private Label lblTurno;

    private Button[] tableroBotones;

    @FXML
    public void initialize(){
        partida = new Juego();
        ia = new InteligenciaArtificial();
        tableroBotones = new Button[]{btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22};
        this.dificultadSeleccionada = EleccionDificultadController.dificultadElegida;
        this.contraIA = Utils.modoIA;
        this.movimientosJugador = 0; // Inicializamos a 0
        actualizarTextoTurno();
    }

    @FXML
    void CasillaClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        int fila = (GridPane.getRowIndex(btn) == null) ? 0 : GridPane.getRowIndex(btn);
        int col = (GridPane.getColumnIndex(btn) == null) ? 0 : GridPane.getColumnIndex(btn);

        if (partida.marcarCasilla(fila, col)) {
            //CADA CLIC VÁLIDO DEL JUGADOR SUMA UN MOVIMIENTO
            movimientosJugador++;

            btn.setText(partida.getTurnoActual());
            btn.setDisable(true);

            if (comprobarEstadoPartida()) {
                return;
            }

            partida.cambiarTurno();

            if (contraIA) {
                lblTurno.setText("Turno de la IA (O)...");
                ejecutarTurnoIA();
            } else {
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

                partida.cambiarTurno();
                actualizarTextoTurno();
            }
        }
    }

    private void actualizarTextoTurno() {
        if (partida.getTurno() == 1) {
            if (Usuario.usuarioSesion != null) {
                lblTurno.setText("Turno de: " + Usuario.usuarioSesion.getNombre() + " (X)");
            } else {
                lblTurno.setText("Turno de: Jugador 1 (X)");
            }
        } else {
            lblTurno.setText("Turno de: Jugador 2 (O)");
        }
    }

    /**
     * Comprueba el estado de la partida y calcula/guarda los puntos en la base de datos.
     */
    private boolean comprobarEstadoPartida() {
        System.out.println("[RASTREO] ComprobarEstadoJuego. Movimientos: " + movimientosJugador);

        if (partida.comprobarSiGana()) {
            String ganadorVisual = "Jugador 1 (X)";
            String resultadoParaHistorial = "PERDIDO"; // Por defecto, asumimos que se pierde (frente a IA o J2)

            if (partida.getTurno() == 1) {
                // Ha ganado el Jugador 1 (el usuario en sesión)
                resultadoParaHistorial = "GANADO";
                if (Usuario.usuarioSesion != null) {
                    ganadorVisual = Usuario.usuarioSesion.getNombre();
                }
            } else {
                ganadorVisual = contraIA ? "Inteligencia Artificial (O)" : "Jugador 2 (O)";
            }

            // GUARDADO EN LA BASE DE DATOS (Victorias y Derrotas)
            guardarDatosPartida(resultadoParaHistorial);

            mostrarAlerta("Ganador", "¡Ha ganado " + ganadorVisual + "!");
            bloquearTablero();
            return true;

        } else if (partida.tableroLleno()) {
            // GUARDADO AUTOMÁTICO EN LA BASE DE DATOS (Empates)
            guardarDatosPartida("EMPATE");

            mostrarAlerta("Empate", "¡Tablero lleno! Buen intento.");
            return true;
        }
        return false;
    }

    /**
     * MÉTODO: Calcula la puntuación según eficiencia y la manda a la BD mediante la clase PartidasDAO
     */
    private void guardarDatosPartida(String resultado) {
        if (Usuario.usuarioSesion != null) {
            int puntosFinales = 0;

            // Lógica de puntuación
            if (resultado.equals("GANADO")) {
                puntosFinales = 100 - (movimientosJugador * 10);
                if (puntosFinales < 10) puntosFinales = 10; // Garantizamos un mínimo por ganar
            } else if (resultado.equals("EMPATE")) {
                puntosFinales = 20;
            } else {
                puntosFinales = 0; // 0 puntos si pierde contra la IA o el J2
            }

            System.out.println("[HISTORIAL] Registrando partida de " + Usuario.usuarioSesion.getNombre() +
                    " (ID: " + Usuario.usuarioSesion.getId_usuario() + ") con " + puntosFinales + " puntos.");

            try {
                // Capturamos tiempos del sistema operativo
                Date fechaSQL = Date.valueOf(LocalDate.now());
                Time horaSQL = Time.valueOf(LocalTime.now());

                // Identificamos el modo de juego (1 para IA, 2 para Dos Jugadores)
                int idModoActual = contraIA ? 1 : 2;

                // Añadimos un '0' al principio como ID provisional de la partida.
                // Esto garantiza que Java utilice el constructor correcto de 6 parámetros:
                // (int idPartida, Date fecha, Time hora, int puntuacion, Usuario usuario, int idModo)
                Partidas nuevaPartida = new Partidas(0, fechaSQL, horaSQL, puntosFinales, Usuario.usuarioSesion, idModoActual);

                // Insertamos de forma segura en la base de datos
                PartidasDAO.addPartidas(nuevaPartida);
                System.out.println("¡ÉXITO! Partida almacenada correctamente en la tabla 'partidas'.");

            } catch (Exception e) {
                System.out.println("ERROR CRÍTICO al intentar insertar el registro en PartidasDAO:");
                e.printStackTrace();
            }
        } else {
            System.out.println("[ANÓNIMO] Jugando como invitado. No se guardan datos en el historial.");
        }
    }

    @FXML
    void ReiniciarClick(ActionEvent event) {
        partida = new Juego();
        movimientosJugador = 0; //Reseteamos el contador de movimientos para la nueva partida
        actualizarTextoTurno();

        for (Button btn : tableroBotones) {
            if (btn != null) {
                btn.setText("");
                btn.setDisable(false);
            }
        }
    }

    @FXML
    void volverAlMenuClick(javafx.event.ActionEvent event) {
        System.out.println("Partida interrumpida. Volviendo al menú principal...");
        org.example.projecto_final.utils.Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/hello-view.fxml");
    }

    private void bloquearTablero() {
        for (Button btn : tableroBotones) {
            if (btn != null) {
                btn.setDisable(true);
            }
        }
    }
}