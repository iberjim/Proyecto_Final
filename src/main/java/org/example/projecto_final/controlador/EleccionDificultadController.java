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

    //Metodo para elegir la dificultad.
@FXML
    void ElegirFacil(ActionEvent event) {
    dificultadElegida = "FACIL";
    irAlTablero(event);
}
    //Metodo para elegir la dificultad.
@FXML
    void ElegirMedio(ActionEvent event) {
    dificultadElegida = "NORMAL";
    irAlTablero(event);
}
    //Metodo para elegir la dificultad.
@FXML
    void ElegirDificil(ActionEvent event) {
    dificultadElegida = "DIFICIL";
    irAlTablero(event);
}
    //Metodo para pasar a la siguiente vista.
@FXML
private void irAlTablero(ActionEvent event) {
    Utils.cambiarPantalla(event,"/org/example/projecto_final/vistas/Tablero.fxml");
    }
}
