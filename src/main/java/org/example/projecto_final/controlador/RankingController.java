package org.example.projecto_final.controlador;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projecto_final.DAO.PartidasDAO;
import org.example.projecto_final.model.Partidas;
import org.example.projecto_final.model.Usuario;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.example.projecto_final.utils.Utils.mostrarAlerta;

public class RankingController {

    @FXML private Label lblMisDatos;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtIdUsuario;

    @FXML private TableView<Partidas> tablaRanking;
    @FXML private TableColumn<Partidas, Integer> colIdPartida;
    @FXML private TableColumn<Partidas, Date> colFecha;
    @FXML private TableColumn<Partidas, Time> colHora;
    @FXML private TableColumn<Partidas, Integer> colPuntuacion;
    @FXML private TableColumn<Partidas, Usuario> colIdUsuario;
    @FXML private TableColumn<Partidas, Integer> colIdModo;

    /**
     * Método de inicialización automática de JavaFX.
     * Configura las factorías de celdas para el TableView, vinculando cada columna
     * con los atributos del modelo Partidas. Además, personaliza la visualización
     * de la columna de usuarios y carga el historial inicial de partidas.
     */
    @FXML
    public void initialize() {
        colIdPartida.setCellValueFactory(new PropertyValueFactory<>("idPartida"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colPuntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colIdModo.setCellValueFactory(new PropertyValueFactory<>("idModo"));

        colIdUsuario.setCellFactory(column -> new TableCell<Partidas, Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (empty || usuario == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(usuario.getId_usuario()));
                }
            }
        });

        // Al arrancar cargamos el historial completo de partidas de forma limpia mediante el DAO
        filtrarPartidas("TODAS", null, -1);
    }

    /**
     * Gestiona el evento de clic en el botón de búsqueda por fecha.
     * Valida que se haya seleccionado una fecha en el componente DatePicker,
     * la convierte al formato compatible con SQL y solicita al motor de
     * filtrado que actualice la tabla mostrando solo las partidas de dicha fecha.
     * * @param event El evento de acción disparado por el botón de búsqueda.
     */
    @FXML
    void BuscarPorFechaClick(ActionEvent event) {
        LocalDate fechaSel = dpFecha.getValue();
        if (fechaSel == null) {
            mostrarAlerta("Atención", "Por favor, selecciona una fecha en el calendario.");
            return;
        }
        filtrarPartidas("FECHA", Date.valueOf(fechaSel), -1);
        lblMisDatos.setText("Mostrando partidas del día: " + fechaSel);
    }

    /**
     * Gestiona el evento de clic en el botón de búsqueda por ID de usuario.
     * Valida que el texto introducido no esté vacío y que sea un número entero válido.
     * Si la validación es correcta, solicita al motor de filtrado actualizar la tabla
     * con las partidas correspondientes al ID especificado.
     * * @param event El evento de acción disparado por el botón de búsqueda.
     */
    @FXML
    void BuscarPorUsuarioClick(ActionEvent event) {
        String textoId = txtIdUsuario.getText().trim();
        if (textoId.isEmpty()) {
            mostrarAlerta("Atención", "Introduce un ID de usuario numérico.");
            return;
        }
        try {
            int idUsuario = Integer.parseInt(textoId);
            filtrarPartidas("USUARIO", null, idUsuario);
            lblMisDatos.setText("Mostrando partidas del Usuario con ID: " + idUsuario);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de usuario debe ser un número entero.");
        }
    }

    @FXML
    void BuscarPorAmbosClick(ActionEvent event) {
        LocalDate fechaSel = dpFecha.getValue();
        String textoId = txtIdUsuario.getText().trim();

        if (fechaSel == null || textoId.isEmpty()) {
            mostrarAlerta("Atención", "Para combinar, debes seleccionar una fecha Y escribir un ID de usuario.");
            return;
        }

        try {
            int idUsuario = Integer.parseInt(textoId);
            filtrarPartidas("AMBOS", Date.valueOf(fechaSel), idUsuario);
            lblMisDatos.setText("Filtrado combinado: Usuario " + idUsuario + " el día " + fechaSel);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de usuario debe ser un número entero.");
        }
    }

    /**
     * Gestiona el evento de clic en el botón de ranking.
     * Ejecuta el filtrado de datos para el ranking, reconfigura dinámicamente
     * la fábrica de celdas de la columna de usuarios para mostrar sus nombres
     * en lugar de sus IDs, y refresca la tabla para visualizar los cambios.
     * * @param event El evento de acción disparado por el botón de ranking.
     */
    @FXML
    void VerRankingClick(ActionEvent event) {
        // 1. Cargamos los datos normalmente
        filtrarPartidas("RANKING", null, -1);

        // 2. FORZAMOS el cambio de visualización en la columna del usuario
        colIdUsuario.setCellFactory(column -> new TableCell<Partidas, Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (empty || usuario == null) {
                    setText(null);
                } else {
                    // Aquí mostramos el nombre solo cuando pulsamos el botón
                    setText(usuario.getNombre());
                }
            }
        });

        // 3. Refrescamos la tabla para que se aplique el cambio
        tablaRanking.refresh();

        lblMisDatos.setText("🏆 RANKING: Mostrando nombres de jugadores");
    }
    /**
     * Filtra la lista de partidas basándose en criterios de fecha, usuario o ambos.
     * Gestiona también la ordenación para el modo RANKING y la configuración
     * visual de la columna de usuarios.
     * * @param modoFiltro Define el tipo de filtrado a aplicar (TODAS, FECHA, USUARIO, AMBOS, RANKING).
     * @param fecha La fecha seleccionada para filtrar (o null si no se filtra por fecha).
     * @param idUsuario El ID del usuario para filtrar (o -1 si no se filtra por usuario).
     */
    private void filtrarPartidas(String modoFiltro, Date fecha, int idUsuario) {
        List<Partidas> listaOriginal = new ArrayList<>();

        try {
            if (modoFiltro.equals("USUARIO") || modoFiltro.equals("AMBOS")) {
                listaOriginal = PartidasDAO.findByIdUsuario(idUsuario);
            } else {
                listaOriginal = PartidasDAO.findAll();
            }
        } catch (Exception e) {
            System.out.println("Error al recuperar partidas desde el DAO:");
            e.printStackTrace();
        }

        List<Partidas> listaFiltrada = new ArrayList<>();

        for (Partidas p : listaOriginal) {
            if (p == null) continue;

            boolean cumpleFecha = (fecha == null) || (p.getFecha() != null && p.getFecha().toString().equals(fecha.toString()));
            boolean cumpleUsuario = (idUsuario == -1) || (p.getUsuario() != null && p.getUsuario().getId_usuario() == idUsuario);

            if (modoFiltro.equals("FECHA") && cumpleFecha) {
                listaFiltrada.add(p);
            } else if (modoFiltro.equals("USUARIO") && cumpleUsuario) {
                listaFiltrada.add(p);
            } else if (modoFiltro.equals("AMBOS") && cumpleFecha && cumpleUsuario) {
                listaFiltrada.add(p);
            } else if (modoFiltro.equals("TODAS") || modoFiltro.equals("RANKING")) {
                listaFiltrada.add(p);
            }
        }

        if (modoFiltro.equals("RANKING")) {
            listaFiltrada.sort((p1, p2) -> Integer.compare(p2.getPuntuacion(), p1.getPuntuacion()));

            //CAMBIAMOS A NOMBRE
            colIdUsuario.setCellFactory(column -> new TableCell<Partidas, Usuario>() {
                @Override
                protected void updateItem(Usuario usuario, boolean empty) {
                    super.updateItem(usuario, empty);
                    setText(empty || usuario == null ? null : usuario.getNombre());
                }
            });
        } else {
            //RESTAURAMOS A ID PARA LAS DEMÁS BÚSQUEDAS
            colIdUsuario.setCellFactory(column -> new TableCell<Partidas, Usuario>() {
                @Override
                protected void updateItem(Usuario usuario, boolean empty) {
                    super.updateItem(usuario, empty);
                    setText(empty || usuario == null ? null : String.valueOf(usuario.getId_usuario()));
                }
            });
        }

        tablaRanking.setItems(FXCollections.observableArrayList(listaFiltrada));
        tablaRanking.refresh(); //refrescar para que se vean los cambios
    }

    @FXML
    void VolverClick(javafx.event.ActionEvent event) {
        System.out.println("Volviendo al menú principal...");
        org.example.projecto_final.utils.Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/hello-view.fxml");
    }
}