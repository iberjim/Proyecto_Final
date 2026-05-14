package controlador;

import javafx.fxml.FXML;

import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuController {

    @FXML
    private Button btnIA;

    @FXML
    private Button btnDuo;

    @FXML
    void onElegirModo(ActionEvent event) {
        if (event.getSource() == btnIA) {
            System.out.println("Has elegido jugar contra la IA");
        } else {
            System.out.println("Has elegido Persona vs Persona");
        }
    }



}
