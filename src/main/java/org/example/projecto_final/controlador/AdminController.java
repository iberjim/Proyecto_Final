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

    /**
     * Inicializa la vista de gestión de usuarios. Configura el enlace entre las columnas
     * de la tabla y los atributos del modelo Usuario, carga los datos iniciales y
     * establece un 'listener' que detecta cuando un usuario es seleccionado en la tabla
     * para rellenar automáticamente los campos de texto del formulario.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        refrescarTabla();

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usuarioSeleccionado = newSelection;
                txtNombre.setText(usuarioSeleccionado.getNombre());
                txtEmail.setText(usuarioSeleccionado.getEmail());
                txtPassword.setText(usuarioSeleccionado.getPassword());
            }
        });
    }
    /**
     * Actualiza el contenido de la tabla de usuarios obteniendo la información
     * más reciente desde la base de datos a través del DAO. Posteriormente,
     * limpia los campos del formulario para preparar la interfaz para una nueva acción.
     */
    private void refrescarTabla() {
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

    @FXML
    public void ActualizarClick(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            Utils.mostrarAlerta("Atención", "Por favor, selecciona primero un usuario de la tabla.");
            return;
        }

        usuarioSeleccionado.setNombre(txtNombre.getText());
        usuarioSeleccionado.setEmail(txtEmail.getText());
        usuarioSeleccionado.setPassword(txtPassword.getText());

        if (UsuarioDAO.update(usuarioSeleccionado)) {
            Utils.mostrarAlerta("Éxito", "Usuario actualizado correctamente en la base de datos.");
            refrescarTabla();
        }
    }

    @FXML
    public void EliminarClick(ActionEvent event) {
        // 1. Forzamos a JavaFX a decirnos qué fila real está marcada con el ratón ahora mismo
        Usuario usuarioMarcado = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (usuarioMarcado == null) {
            Utils.mostrarAlerta("Atención", "Por favor, selecciona primero un usuario de la tabla de la interfaz.");
            return;
        }

        // RASTREO EN CONSOLA: Para verificar qué ID está leyendo Java realmente
        System.out.println("[DEBUG ADMIN] Intentando borrar a: " + usuarioMarcado.getNombre() + " con ID: " + usuarioMarcado.getId_usuario());

        // 2. Si el ID sigue saliendo 0, usamos una alternativa de seguridad o le pasamos el objeto
        if (usuarioMarcado.getId_usuario() == 0) {
            Utils.mostrarAlerta("Error de Mapeo", "El ID del usuario seleccionado es 0.");
            return;
        }

        // 3. Llamamos al DAO con el ID verificado
        if (UsuarioDAO.delete(usuarioMarcado.getId_usuario())) {
            Utils.mostrarAlerta("Éxito", "Usuario y su historial eliminados correctamente de la base de datos.");
            refrescarTabla(); // Recargamos la lista para que desaparezca visualmente
        } else {
            Utils.mostrarAlerta("Error", "No se pudo eliminar al usuario seleccionado.");
        }
    }

    @FXML
    public void VolverClick(ActionEvent event) {
        System.out.println("Saliendo del Panel de Administrador de forma segura...");
        org.example.projecto_final.utils.Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/hello-view.fxml");
    }
}