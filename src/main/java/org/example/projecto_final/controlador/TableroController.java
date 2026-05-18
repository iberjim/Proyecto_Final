package org.example.projecto_final.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.example.projecto_final.model.Juego;
import org.example.projecto_final.model.Usuario;
public class TableroController {

private Juego partida;

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


    private void actualizarLabelTurno(){
        lblTurno.setText("Turno de: "+partida.getTurnoActual());
    }
private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
}
private void bloquearTablero(){}
}
