package org.example.projecto_final.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projecto_final.DAO.RankingDAO;
import org.example.projecto_final.model.Ranking;
import org.example.projecto_final.model.Usuario;

public class RankingController {

    @FXML private Label lblMisDatos;

    // La tabla se configura con la clase Ranking
    @FXML private TableView<Ranking> tablaRanking;
    @FXML private TableColumn<Ranking, String> colNombre;
    @FXML private TableColumn<Ranking, Integer> colVictorias;
    @FXML private TableColumn<Ranking, Integer> colDerrotas;
    @FXML private TableColumn<Ranking, Integer> colEmpates;

    private RankingDAO rankingDAO = new RankingDAO();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colVictorias.setCellValueFactory(new PropertyValueFactory<>("victorias"));
        colDerrotas.setCellValueFactory(new PropertyValueFactory<>("derrotas"));
        colEmpates.setCellValueFactory(new PropertyValueFactory<>("empates"));

        tablaRanking.setItems(rankingDAO.obtenerTopRanking());

        if (Usuario.usuarioSesion != null) {
            String misEstadisticas = "Buscando tus datos en el ranking...";
            for (Ranking fila : tablaRanking.getItems()) {
                if (fila.getNombreUsuario().equalsIgnoreCase(Usuario.usuarioSesion.getNombre())) {
                    misEstadisticas = "Tus estadísticas: Victorias: " + fila.getVictorias() +
                            " | Derrotas: " + fila.getDerrotas() + " | Empates: " + fila.getEmpates();
                    break;
                }
            }
            lblMisDatos.setText(misEstadisticas);
        } else {
            lblMisDatos.setText("Jugando como Invitado (Inicia sesión para guardar estadísticas)");
        }
    }

    @FXML
        void VolverClick(javafx.event.ActionEvent event) {
            System.out.println("Volviendo al menú principal...");
            org.example.projecto_final.utils.Utils.cambiarPantalla(event, "/org/example/projecto_final/vistas/hello-view.fxml");
        }
    }
