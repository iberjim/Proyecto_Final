module org.example.projecto_final {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.xml.bind;
    requires java.sql;

    opens org.example.projecto_final to javafx.fxml;
    opens org.example.projecto_final.controlador to javafx.fxml;
    opens org.example.projecto_final.model to javafx.fxml;
    opens org.example.projecto_final.dataaccess to jakarta.xml.bind;

    exports org.example.projecto_final;
    exports org.example.projecto_final.utils;
}