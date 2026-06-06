package org.example.projecto_final.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projecto_final.DAO.UsuarioDAO;
import org.example.projecto_final.model.Usuario;
import org.example.projecto_final.utils.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colPassword;

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;

    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnVolver;

    private ObservableList<Usuario> listaObservable;
    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize() {
        // 1. Configurar las columnas de la tabla para que sepan qué dato mostrar del objeto Usuario
        colId.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        // 2. Cargar los datos desde la Base de Datos
        refrescarTabla();

        // 3. Escuchar los clics de la tabla: cuando pinchen en una fila, rellenamos los campos
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usuarioSeleccionado = newSelection;
                txtNombre.setText(usuarioSeleccionado.getNombre());
                txtEmail.setText(usuarioSeleccionado.getEmail());
                txtPassword.setText(usuarioSeleccionado.getPassword());
            }
        });
    }

    private void refrescarTabla() {
        // Llamamos al DAO que creamos antes para traernos todo de la BD
        listaObservable = FXCollections.observableArrayList(UsuarioDAO.findAll());
        tablaUsuarios.setItems(listaObservable);
        limpiarCampos();
    }

    public void limpiarCampos() {
        txtNombre.clear();
        txtEmail.clear();
        txtPassword.clear();
        usuarioSeleccionado = null;
    }

    // ACCIÓN: ACTUALIZAR (U del CRUD)
    @FXML
    public void ActualizarClick(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            Utils.mostrarAlerta("Atención", "Por favor, selecciona primero un usuario de la tabla.");
            return;
        }

        // Modificamos el objeto con los nuevos datos de los cuadros de texto
        usuarioSeleccionado.setNombre(txtNombre.getText());
        usuarioSeleccionado.setEmail(txtEmail.getText());
        usuarioSeleccionado.setPassword(txtPassword.getText());

        // Le pedimos al DAO que lo guarde en la BD
        if (UsuarioDAO.update(usuarioSeleccionado)) {
            Utils.mostrarAlerta("Éxito", "Usuario actualizado correctamente en la base de datos.");
            refrescarTabla();
        }
    }

    // ACCIÓN: ELIMINAR (D del CRUD)
    @FXML
   public void EliminarClick(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            Utils.mostrarAlerta("Atención", "Por favor, selecciona primero un usuario de la tabla.");
            return;
        }

        // Le pedimos al DAO que lo borre usando su ID
        if (UsuarioDAO.delete(usuarioSeleccionado.getId_usuario())) {
            Utils.mostrarAlerta("Éxito", "Usuario eliminado de la base de datos de forma permanente.");
            refrescarTabla();
        }
    }

    // ACCIÓN: VOLVER AL MENÚ
    @FXML
   public void VolverClick(ActionEvent event) {
        try {

            Parent menu = FXMLLoader.load(getClass().getResource("/org/example/projecto_final/hello-view.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(menu));
            stage.show();
        } catch (IOException e) {
            Utils.mostrarAlerta("Error", "No se pudo regresar al menú principal.");
        }
    }
}