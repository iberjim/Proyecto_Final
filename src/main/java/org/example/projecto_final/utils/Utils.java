package org.example.projecto_final.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.Optional;

public class Utils {
    /**
     * Cambia la pantalla actual de la aplicación por una nueva vista FXML.
     * * @param event    El evento que disparó la acción (ej. hacer clic en un botón).
     * @param rutaFxml La ruta relativa dentro de resources donde está el archivo .fxml.
     *  1. Localiza el archivo FXML en los recursos del proyecto usando la ruta recibida
     * 2. Lee el archivo FXML y carga toda la jerarquía de componentes visuales (los "hijos")
     *  3. A partir del botón que generó el 'event', viaja por la jerarquía:
     *  Botón -> Nodo -> Escena -> Ventana (Stage) actual para recuperarla en memoria
     *  4. Crea una nueva escena con el diseño cargado del FXML y la coloca en la ventana
     *  5. Refresca y muestra la ventana con el nuevo contenido
     */
    public static void cambiarPantalla(ActionEvent event, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Utils.class.getResource(rutaFxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarDialogo("Error","Error","Hay un error al cambiar de ventana",Alert.AlertType.ERROR);
        }
    }
    /**
     * Crea y muestra un cuadro de diálogo emergente (Alert) de forma genérica.
     * * @param titulo   Texto que aparecerá en la barra de título de la ventana.
     * @param cabecera Texto destacado en la parte superior del cuadro.
     * @param mensaje  Cuerpo del texto con la información detallada para el usuario.
     * @param tipo     El tipo de alerta (ERROR, INFORMATION, WARNING, CONFIRMATION).
     * @return         Un Optional que contiene el botón pulsado por el usuario (útil para confirmaciones).
     */
    public static Optional<ButtonType> mostrarDialogo(String titulo, String cabecera, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        return alert.showAndWait();
    }
    public static void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
