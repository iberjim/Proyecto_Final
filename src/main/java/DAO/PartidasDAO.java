package DAO;

import dataaccess.ConnectionBD;
import model.Partidas;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidasDAO {

    private static final String SQL_FIND_ALL = "SELECT * FROM partidas ORDER BY idPartidas";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM partidas WHERE idPartidas=?";
    private static final String SQL_FIND_BY_ID_USUARIO = "SELECT * FROM partidas WHERE idUsuario=? ORDER BY idPartidas";
    private static final String SQL_INSERT = "INSERT INTO partidas (idUsuario, idPartidas) VALUES (?, ?,?)";
    private static final String SQL_UPDATE =  "UPDATE partidas SET idUsuario=? WHERE idPartidas=?";
    private static final String SQL_DELETE = "DELETE FROM partidas WHERE idPartidas=?";

    private PartidasDAO() {

    }
    public static List<Partidas> findAll() {
        List<Partidas> partidas = new ArrayList<>();

        try (Statement st = ConnectionBD.getInstance().getConnection().createStatement();
             ResultSet rs = st.executeQuery(SQL_FIND_ALL)) {

            while (rs.next()) {
                partidas.add(createPartidaFromResultSet(rs));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return partidas;

    }
    public static Partidas findById(int idPartidas) throws SQLException {
        Partidas partidas = null;
        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, idPartidas);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                partidas = createPartidaFromResultSet(rs);
            }
        }

        return partidas;
    }

    public static Partidas addPartidas(Partidas partidas, Usuario usuario) throws SQLException {
        if (!isPartidaValida(partidas) || findById(partidas.getId_partida()) != null) {
            return null;
        }
        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, partidas.getId_partida());
            ps.setInt(2, usuario.getId_usuario());
            ps.setInt(3, partidas.getDia());
            ps.setInt(4, partidas.getMes());
            ps.setInt(5, partidas.getAno());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                partidas.setId_partida(generatedKeys.getInt(1));
            }
        }

        return partidas;
    }
    public static boolean updateLibro(Partidas partidaNueva) throws SQLException {
        if (!isPartidaValida(partidaNueva)) {
            return false;
        }

        Partidas partidaActual = findById(partidaNueva.getId_partida());
        if (partidaActual == null) {
            return false;
        }
        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_UPDATE)) {
            ps.setInt(1, partidaNueva.getId_partida());
            ps.setInt(3, partidaNueva.getUsuario().getId_usuario());
            ps.executeUpdate();
            return true;
        }

        }public static boolean deleteLibroById(int idpartida) throws SQLException {
        if (findById(idpartida) == null) {
            return false;
        }

        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_DELETE)) {
            ps.setInt(1,idpartida );
            ps.executeUpdate();
            return true;
        }
    }


    /**
     * Método que devuelve una lista con todas las partidas de un usuario especifico.
     */
    public static List<Partidas> findByIdUsuario(int idusuario) throws SQLException {
        ArrayList<Partidas> partidas = new ArrayList<>();

        try (PreparedStatement st = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_FIND_BY_ID_USUARIO)) {
            st.setInt(1, idusuario);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idPartida");
                int dia = rs.getInt("dia");
                int mes = rs.getInt("mes");
                int anio = rs.getInt("año");
                Usuario usuario = UsuarioDAO.findById(rs.getInt("idUsuario"));
                Partidas partida = new Partidas(id,dia,mes,anio, usuario);
                partidas.add(partida);
            }
        }
        return partidas;
    }

    private static boolean isPartidaValida(Partidas partida){
        return partida != null
                && partida.getId_partida() > 0
                && partida.getUsuario() != null;
    }

    private static Partidas createPartidaFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idPartida");
        int dia = rs.getInt("dia");
        int mes  = rs.getInt("mes");
        int anio = rs.getInt("anio");
        Usuario usuario = UsuarioDAO.findById(rs.getInt("idUsuario"));
        return new Partidas(id, dia, mes, anio, usuario);
    }





    }
