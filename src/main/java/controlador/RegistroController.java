package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class RegistroController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;


    @FXML
    void onRegistrarUsuario(ActionEvent event) {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Campos vacíos");
            alerta.setHeaderText(null);
            alerta.setContentText("¡Cuidado! Debes rellenar todo los campos para continuar.");
            alerta.showAndWait();
        } else {
            System.out.println("Registrando a: " + nombre);
        }
    }
}