module org.example.projecto_final {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.projecto_final to javafx.fxml;
    exports org.example.projecto_final;
}