package org.example.projecto_final.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import javafx.scene.control.Button;      // Para tus botones btnIA y btnDuo


public class MenuController {

    @FXML
    private Button btnIA;

    @FXML
    private Button btnDuo;

    @FXML
    void ElegirModo(ActionEvent event) {
        if (event.getSource() == btnIA) {
            System.out.println("Has elegido jugar contra la IA");
        } else {
            System.out.println("Has elegido Persona vs Persona");
        }
    }



}
