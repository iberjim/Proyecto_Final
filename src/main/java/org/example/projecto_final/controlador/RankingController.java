package org.example.projecto_final.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projecto_final.DAO.RankingDAO;
import org.example.projecto_final.model.Ranking;
import org.example.projecto_final.model.Usuario;

import java.util.List;

public class RankingController {

    @FXML private Label lblMisDatos;

    // La tabla se configura con la clase Ranking
    @FXML private TableView<Ranking> tablaRanking;
    @FXML private TableColumn<Ranking, String> colNombre;
    @FXML private TableColumn<Ranking, Integer> colVictorias;
    @FXML private TableColumn<Ranking, Integer> colDerrotas;
    @FXML private TableColumn<Ranking, Integer> colEmpates;

    private RankingDAO rankingDAO = new RankingDAO();
    /**
     * Método de inicialización automática de JavaFX para la pantalla de Ranking.
     * Configura las columnas de la tabla, carga los datos desde el DAO y,
     * si hay un usuario logueado, busca sus estadísticas utilizando un bucle controlado
     * por condición (sin hacer uso de la sentencia 'break').
     */
    @FXML
    public void initialize() {
        // 1. CONFIGURACIÓN DE COLUMNAS: Vinculamos las columnas con las propiedades del modelo
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colVictorias.setCellValueFactory(new PropertyValueFactory<>("victorias"));
        colDerrotas.setCellValueFactory(new PropertyValueFactory<>("derrotas"));
        colEmpates.setCellValueFactory(new PropertyValueFactory<>("empates"));

        // 2. CARGA DE DATOS: Volcamos el Top del ranking en la tabla visual
        tablaRanking.setItems(rankingDAO.obtenerTopRanking());

        // 3. PERSONALIZACIÓN DE LA INTERFAZ SEGÚN LA SESIÓN:
        if (Usuario.usuarioSesion != null) {
            String misEstadisticas = "Buscando tus datos en el ranking...";

            // Obtenemos la lista de elementos para poder recorrerla de forma controlada
            List<Ranking> listaRanking = tablaRanking.getItems();

            // Inicializamos las variables para controlar el bucle de forma estructurada
            int i = 0;
            boolean encontrado = false; // Variable bandera/condición

            // El bucle se ejecutará de forma segura mientras queden elementos Y NO hayamos encontrado al usuario
            while (i < listaRanking.size() && !encontrado) {
                Ranking fila = listaRanking.get(i);

                // Comprobamos si la fila actual pertenece al usuario en sesión
                if (fila.getNombreUsuario().equalsIgnoreCase(Usuario.usuarioSesion.getNombre())) {
                    // Formateamos las estadísticas del usuario
                    misEstadisticas = "Tus estadísticas: Victorias: " + fila.getVictorias() +
                            " | Derrotas: " + fila.getDerrotas() + " | Empates: " + fila.getEmpates();

                    encontrado = true; // Activamos la bandera para que la condición del while sea falsa y el bucle termine de forma limpia
                }

                i++; // Avanzamos al siguiente elemento
            }

            // Asignamos el texto definitivo al Label
            lblMisDatos.setText(misEstadisticas);

        } else {
            // Caso B: Si juega como invitado, no se realiza ninguna búsqueda
            lblMisDatos.setText("Jugando como Invitado (Inicia sesión para guardar estadísticas)");
        }
    }
    //boton para volver al menu inicial en el ranking.
    @FXML
        void VolverClick(javafx.event.ActionEvent event) {
            System.out.println("Volviendo al menú principal...");
            org.example.projecto_final.utils.Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/hello-view.fxml");
        }
    }
