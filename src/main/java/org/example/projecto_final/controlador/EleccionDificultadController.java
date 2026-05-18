package org.example.projecto_final.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

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
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projecto_final/vistas/Tablero.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)  ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }catch(IOException e){
        System.out.println("Error al abrir el tablero: "+e.getMessage());
    }
}






}
