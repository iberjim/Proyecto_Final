package org.example.projecto_final.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;      // Para tus botones btnIA y btnDuo
import  org.example.projecto_final.utils.Utils;

public class MenuController {

    @FXML
    private Button btnIA;

    @FXML
    private Button btnDUO;

    @FXML
    void ElegirModo(ActionEvent event) {
        if (event.getSource() == btnIA) {
            System.out.println("Has elegido jugar contra la IA");
            Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/Registro.fxml");
        } else {
            System.out.println("Has elegido Persona vs Persona");
            Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/Tablero.fxml");
        }
    }



}
