package org.example.projecto_final.controlador;

import org.example.projecto_final.DAO.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.projecto_final.model.Usuario;
import org.example.projecto_final.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;


public class RegistroController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;


    @FXML
    void RegistrarUsuario(ActionEvent event) {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Utils.mostrarDialogo("Error","Campos Incompletos","Por favor, rellena todos los campos para poder continuar.", Alert.AlertType.ERROR);
    } else {

            try{
                Usuario usuario = new Usuario(nombre, email, pass);
                if (UsuarioDAO.insert(usuario)) {
                    Utils.mostrarDialogo("Registro Éxitoso","¡Bienvenido!","El usuario se ha guardado correctamente en la base de datos.",Alert.AlertType.INFORMATION);
                    Utils.cambiarPantalla(event,"/org/example/projecto_final/vistas/EleccionDificultad.fxml");
                }else {
                    Utils.mostrarDialogo("Erro de Registro","No se pudo guardar","El email ya está registrado en la base de datos.",Alert.AlertType.ERROR);
                }
            }catch (SQLException e){
                Utils.mostrarDialogo("Error","Error al registrar el usuario","No se ha podido establecer conexión con la base de datos",Alert.AlertType.ERROR);
            }
        }
    }
}
