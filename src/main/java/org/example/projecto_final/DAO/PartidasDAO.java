package org.example.projecto_final.DAO;

import org.example.projecto_final.dataaccess.ConnectionBD;
import org.example.projecto_final.model.Partidas;
import org.example.projecto_final.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidasDAO {

    // 1. Constantes SQL
    private static final String SQL_FIND_ALL = "SELECT * FROM partidas ORDER BY id_partida";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM partidas WHERE id_partida=?";
    private static final String SQL_FIND_BY_ID_USUARIO = "SELECT * FROM partidas WHERE id_usuario=? ORDER BY id_partida";
    private static final String SQL_INSERT = "INSERT INTO partidas (fecha, hora, puntuacion, id_usuario, id_modo) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE partidas SET fecha=?, hora=?, puntuacion=?, id_usuario=?, id_modo=? WHERE id_partida=?";
    private static final String SQL_DELETE = "DELETE FROM partidas WHERE id_partida=?";

    private PartidasDAO() {
        // Constructor privado para patrón de métodos estáticos
    }

    // LISTAR TODAS LAS PARTIDAS
    public static List<Partidas> findAll() {
        List<Partidas> partidas = new ArrayList<>();
        Connection con = ConnectionBD.getInstance().getConnection();

        // Sacamos 'con' del try para proteger el Singleton
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SQL_FIND_ALL)) {

            while (rs.next()) {
                partidas.add(createPartidaFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en findAll de PartidasDAO:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return partidas;
    }

    // BUSCAR PARTIDA POR ID
    public static Partidas findById(int idPartida) throws SQLException {
        Partidas partida = null;
        Connection con = ConnectionBD.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, idPartida);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    partida = createPartidaFromResultSet(rs);
                }
            }
        }
        return partida;
    }

    // INSERTAR NUEVA PARTIDA (Se llamará automáticamente al terminar de jugar)
    public static Partidas addPartidas(Partidas partida) throws SQLException {
        if (partida == null) return null;

        Connection con = ConnectionBD.getInstance().getConnection();
        // Usamos RETURN_GENERATED_KEYS para que MySQL nos devuelva el id_partida auto-incremental
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, partida.getFecha());
            ps.setTime(2, partida.getHora());
            ps.setInt(3, partida.getPuntuacion());
            ps.setInt(4, partida.getUsuario().getId_usuario());
            ps.setInt(5, partida.getIdModo());

            ps.executeUpdate();

            // Recuperamos el ID que le ha asignado la base de datos
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    partida.setIdPartida(generatedKeys.getInt(1));
                }
            }
        }
        return partida;
    }

    // ACTUALIZAR PARTIDA
    public static boolean updatePartida(Partidas partidaNueva) throws SQLException {
        if (partidaNueva == null || findById(partidaNueva.getIdPartida()) == null) {
            return false;
        }

        Connection con = ConnectionBD.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setDate(1, partidaNueva.getFecha());
            ps.setTime(2, partidaNueva.getHora());
            ps.setInt(3, partidaNueva.getPuntuacion());
            ps.setInt(4, partidaNueva.getUsuario().getId_usuario());
            ps.setInt(5, partidaNueva.getIdModo());
            ps.setInt(6, partidaNueva.getIdPartida());

            return ps.executeUpdate() > 0;
        }
    }

    // ELIMINAR PARTIDA BY ID
    public static boolean deletePartidaById(int idPartida) throws SQLException {
        if (findById(idPartida) == null) {
            return false;
        }

        Connection con = ConnectionBD.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idPartida);
            return ps.executeUpdate() > 0;
        }
    }

    // BUSCAR PARTIDAS DE UN USUARIO ESPECÍFICO
    public static List<Partidas> findByIdUsuario(int idUsuario) throws SQLException {
        List<Partidas> partidas = new ArrayList<>();
        Connection con = ConnectionBD.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID_USUARIO)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    partidas.add(createPartidaFromResultSet(rs));
                }
            }
        }
        return partidas;
    }

    // MÉTODO: Transforma la fila de la BD en Objeto Java
    private static Partidas createPartidaFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_partida");
        Date fecha = rs.getDate("fecha");
        Time hora = rs.getTime("hora");
        int puntuacion = rs.getInt("puntuacion");
        int idModo = rs.getInt("id_modo");

        // Buscamos el objeto Usuario real usando su id_usuario de la base de datos
        // Usando findById de la clase UsuarioDAO.
        Usuario usuario = UsuarioDAO.findById(rs.getInt("id_usuario"));
        // Creamos el objeto Partidas
        return new Partidas(id, fecha, hora, puntuacion, usuario, idModo);


    }
}