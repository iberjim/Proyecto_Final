package org.example.projecto_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // AQUÍ CARGAMOS El Menú
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/projecto_final/vistas/hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Tres en Raya");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}