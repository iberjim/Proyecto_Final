package org.example.projecto_final.DAO;

import org.example.projecto_final.dataaccess.ConnectionBD;
import org.example.projecto_final.model.Ranking;

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

        // 1. INTENTAMOS PRIMERO EL UPDATE (Para cuando el usuario ya tiene fila en el ranking)
        String sqlUpdate = "UPDATE ranking SET " + columna + " = COALESCE(" + columna + ", 0) + 1 WHERE id_usuario = ?";

        try (Connection con = org.example.projecto_final.dataaccess.ConnectionBD.getInstance().getConnection()) {
            System.out.println("Conectando a la base de datos para actualizar ranking...");

            try (PreparedStatement pstUpdate = con.prepareStatement(sqlUpdate)) {
                pstUpdate.setInt(1, idUsuario);
                int filasAfectadas = pstUpdate.executeUpdate();

                // 2. SI NO EXISTE LA FILA (filasAfectadas == 0), SE HACE EL INSERT PASANDO EL MODO OBLIGATORIO
                if (filasAfectadas == 0) {
                    System.out.println("[DAO] No había registro previo para el usuario " + idUsuario + ". Creando fila inicial...");

                    // Le pasamos id_modo = 1
                    String sqlInsert = "INSERT INTO ranking (id_usuario, id_modo, victorias, derrotas, empates, puntuaciones) VALUES (?, 1, 0, 0, 0, 0)";

                    try (PreparedStatement pstInsert = con.prepareStatement(sqlInsert)) {
                        pstInsert.setInt(1, idUsuario);
                        pstInsert.executeUpdate();

                        // Una vez creada la fila limpia, ejecutamos de nuevo el UPDATE para que sume el resultado actual
                        try (PreparedStatement pstRetryUpdate = con.prepareStatement(sqlUpdate)) {
                            pstRetryUpdate.setInt(1, idUsuario);
                            pstRetryUpdate.executeUpdate();
                        }
                    }
                }

                System.out.println("[SQL] ¡ÉXITO TOTAL! Ranking actualizado (" + resultado + ") para el usuario ID: " + idUsuario);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error crítico de claves o integridad en la base de datos:");
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