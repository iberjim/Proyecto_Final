package org.example.projecto_final.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.example.projecto_final.model.Juego;
import org.example.projecto_final.model.Usuario;
import org.example.projecto_final.utils.Utils;

import static org.example.projecto_final.utils.Utils.mostrarAlerta;

public class TableroController {

private Juego partida;
    // 1. Inyectamos los 9 botones del tablero para poder limpiarlos y reactivarlos
    @FXML private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML private Button btnReiniciar;
@FXML
    private Label lblTurno;

    @FXML
    public void initialize(){
        partida = new Juego();

        if(Usuario.usuarioSesion != null){
            lblTurno.setText("Turno de: "+Usuario.usuarioSesion.getNombre()+ " (X)");

        }
    }
    @FXML
    void CasillaClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        int fila = (GridPane.getRowIndex(btn) == null) ? 0 : GridPane.getRowIndex(btn);
        int col = (GridPane.getColumnIndex(btn) == null) ? 0 : GridPane.getColumnIndex(btn);

        if (partida.marcarCasilla(fila, col)) {
            btn.setText(partida.getTurnoActual()); // Ponemos X o O
            btn.setDisable(true);

            if (partida.comprobarSiGana()) {
                mostrarAlerta("Ganador", "Ha ganado el jugador " + partida.getTurnoActual());
            } else if (partida.tableroLleno()) {
                mostrarAlerta("Empate", "Tablero lleno");
            } else {
                partida.cambiarTurno();
                lblTurno.setText("Turno de: " + partida.getTurnoActual());
            }
        }
    }
    @FXML
    void ReiniciarClick(ActionEvent event) {
        partida = new Juego();

        if(Usuario.usuarioSesion != null){
            lblTurno.setText("Turno de: "+Usuario.usuarioSesion.getNombre()+" (X)");
        }else {
            lblTurno.setText("Turno de: X");
        }
        Button[] tableroBotones = {btn00, btn02, btn10, btn11, btn12, btn20, btn21, btn22};
        for (Button btn : tableroBotones) {
            btn.setText("");
            btn.setDisable(false);
        }
    }

    private void actualizarLabelTurno(){
        lblTurno.setText("Turno de: "+partida.getTurnoActual());
    }
private void bloquearTablero(){}
}
