package org.example.projecto_final.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.projecto_final.DAO.UsuarioDAO;
import org.example.projecto_final.model.Usuario;
import org.example.projecto_final.utils.Utils;

import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    void IniciarSesionClick(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String pass = txtPassword.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Utils.mostrarDialogo("Campos vacíos", "Información incompleta", "Por favor, introduce tu correo y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Buscamos el usuario en la BD mediante las credenciales
            Usuario usuarioLogueado = UsuarioDAO.login(email, pass);

            if (usuarioLogueado != null) {

                System.out.println("✅ Login correcto. Bienvenido, " + usuarioLogueado.getNombre());

                // Redirigimos directamente a la pantalla de dificultad
                Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/EleccionDificultad.fxml");
            } else {
                Utils.mostrarDialogo("Error de acceso", "Credenciales incorrectas", "El email o la contraseña no coinciden.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.mostrarDialogo("Error técnico", "Fallo de conexión", "No se pudo conectar con la base de datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void IrARegistroClick(ActionEvent event) {
        // Por si no tiene cuenta, habilitamos el enlace para ir a registrarse
        Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/Resgistro.fxml");
    }
}