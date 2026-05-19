package org.example.projecto_final.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import org.example.projecto_final.utils.Utils;

public class EleccionDificultadController {

public static String dificultadElegida;

@FXML
    void ElegirFacil(ActionEvent event) {
    dificultadElegida = "FACIL";
    irAlTablero(event);
}
@FXML
    void ElegirMedio(ActionEvent event) {
    dificultadElegida = "NORMAL";
    irAlTablero(event);
}
@FXML
    void ElegirDificil(ActionEvent event) {
    dificultadElegida = "DIFICIL";
    irAlTablero(event);
}
@FXML
private void irAlTablero(ActionEvent event) {
    Utils.cambiarPantalla(event,"/org/example/projecto_final/vistas/Tablero.fxml");
    }
}
