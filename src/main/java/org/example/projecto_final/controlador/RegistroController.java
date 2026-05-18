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

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Campos vacíos");
            alerta.setHeaderText(null);
            alerta.setContentText("¡Cuidado! Debes rellenar todo los campos para continuar.");
            alerta.showAndWait();
    } else {

            try{
                Usuario usuario = new Usuario(nombre, email, pass);
                if (UsuarioDAO.insert(usuario)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projecto_final/vistas/EleccionDificultad.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }else {
                    System.out.println("ERROR: No se pudo insertar.");
                }
            }catch (SQLException e){
                System.out.println("ERROR de la base de datos: "+e.getMessage());
            }catch (IOException e){
                System.out.println("Error al cargar la ventana.");
            }
        }
    }
}
