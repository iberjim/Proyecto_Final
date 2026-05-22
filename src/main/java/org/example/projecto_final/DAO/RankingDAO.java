package org.example.projecto_final.DAO;

import org.example.projecto_final.dataaccess.ConnectionBD;
import org.example.projecto_final.model.Ranking;
import org.example.projecto_final.model.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RankingDAO {

    /**
     * Incrementa en 1 la estadística correspondiente del usuario en la tabla ranking.
     * resultado: "victoria", "derrota" o "empate"
     */
    public void actualizarEstadisticas(int idUsuario, String resultado) {
        String columna = "";

        switch (resultado.toLowerCase()) {
            case "victoria":
                columna = "victorias";
                break;
            case "derrota":
                columna = "derrotas";
                break;
            case "empate":
                columna = "empates";
                break;
            default:
                return;
        }

        String sql = "UPDATE ranking SET " + columna + " = " + columna + " + 1 WHERE id_usuario = ?";

        try (Connection con = ConnectionBD.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idUsuario);
            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("[SQL] Ranking actualizado con éxito (" + resultado + ") para el ID de usuario: " + idUsuario);
            } else {
                System.out.println("[SQL] No se encontró ninguna fila en la tabla 'ranking' para el usuario: " + idUsuario);
            }

        } catch (SQLException e) {
            System.out.println("Error crítico en la base de datos al intentar actualizar el ranking:");
            e.printStackTrace();
        }
    }

    /**
     * MÉTODO: Obtiene las filas del Top 5 de jugadores desde la base de datos
     */
    public ObservableList<Ranking> obtenerTopRanking() {
        ObservableList<Ranking> listaRanking = FXCollections.observableArrayList();
        String sql = "SELECT u.nombre AS nombre_usuario, r.victorias, r.derrotas, r.empates " +
                "FROM ranking r " +
                "JOIN usuario u ON r.id_usuario = u.id_usuario " +
                "ORDER BY r.victorias DESC LIMIT 5";

        try (Connection con = ConnectionBD.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("[DAO] Ejecutando consulta de ranking...");

            while (rs.next()) {
                String nom = rs.getString("nombre_usuario");
                int vic = rs.getInt("victorias");
                int der = rs.getInt("derrotas");
                int emp = rs.getInt("empates");

                // Chivato en consola para comprobar si los datos se leen bien
                System.out.println("[DAO] Fila detectada -> Usuario: " + nom + " | V: " + vic + " | D: " + der + " | E: " + emp);

                // Pasamos los datos al constructor de la clase Ranking
                listaRanking.add(new Ranking(nom, vic, der, emp));
            }

            System.out.println("[DAO] Total filas cargadas para enviar a la tabla: " + listaRanking.size());

        } catch (SQLException e) {
            System.out.println("Error crítico al obtener el ranking de la base de datos:");
            e.printStackTrace();
        }

        return listaRanking;
    }
}