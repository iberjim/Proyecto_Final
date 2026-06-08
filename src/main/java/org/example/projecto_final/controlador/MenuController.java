package org.example.projecto_final.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;      
import  org.example.projecto_final.utils.Utils;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.Optional;

public class MenuController {

    @FXML
    private Button btnIA;

    @FXML
    private Button btnDUO;

    @FXML
    private Button btnRanking;
    @FXML
    private Button btnAdmin;

    //Metodo del menu principal para elegir una opcion.
    @FXML
    void ElegirModo(ActionEvent event) {
        if (event.getSource() == btnIA) {
            System.out.println("Has elegido jugar contra la IA");
            Utils.modoIA = true;
            Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/Login.fxml");
        } else if (event.getSource() == btnDUO) {
            Utils.modoIA = false;
            System.out.println("Has elegido Persona vs Persona");
            Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/Tablero.fxml");

        }else {
            System.out.println("Abriendo la pantalla de estadísticas...");
            Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/Ranking.fxml");
        }
    }
    /**
     * Inserta un login para solicitar las credenciales de administrador.
     * Si las credenciales introducidas son válidas, permite el cambio de vista hacia la
     * vista de administración; de lo contrario, muestra una alerta de error.
     * * @param event El evento de acción que dispara la apertura del panel.
     */

    @FXML
    private void abrirPanelAdmin(ActionEvent event) {
        // 1. Crear el diálogo personalizado
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Acceso Administrador");
        dialog.setHeaderText("Por favor, introduce tus credenciales de acceso:");

        // 2. Configurar los botones
        ButtonType loginButtonType = new ButtonType("Entrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // 3. Crear los campos de texto
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Usuario");
        PasswordField password = new PasswordField();
        password.setPromptText("Contraseña");

        //Añadimos los campos a rellenar con las coordenadas de la posicion.
        grid.add(new Label("Usuario:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // 4. Lógica de validación al pulsar "Entrar"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(creds -> {
            //  AQUÍ VA LA VALIDACIÓN REAL
            if (creds.getKey().equals("admin") && creds.getValue().equals("1234")) {
                // Si la contraseña es correcta, abrimos la ventana
                Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/Admin.fxml");
            } else {
                Utils.mostrarAlerta("Error", "Credenciales incorrectas. Acceso denegado.");
            }
        });
    }


}
