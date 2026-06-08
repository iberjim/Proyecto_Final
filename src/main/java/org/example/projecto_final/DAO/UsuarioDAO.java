package org.example.projecto_final.DAO;

import org.example.projecto_final.dataaccess.ConnectionBD;
import org.example.projecto_final.model.Partidas;
import org.example.projecto_final.model.Usuario;
import org.example.projecto_final.utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final static String SQL_LOGIN = "SELECT * FROM usuario WHERE email = ? AND password = ?";
    private final static String SQL_ALL = "SELECT * FROM usuario";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM usuario WHERE id_usuario = ?";
    private final static String SQL_INSERT = "INSERT INTO usuario (nombre, email, password) VALUES (?, ?, ?)";
    private final static String SQL_UPDATE = "UPDATE usuario SET nombre = ?, email = ?, password = ? WHERE id_usuario = ?";
    private final static String SQL_DELETE = "DELETE FROM usuario WHERE id_usuario = ?";

    // CREATE: Insertar usuario
    public static boolean insert(Usuario u) {
        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_INSERT)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Utils.mostrarAlerta("Error de Registro", "No se puede registrar el usuario. El email podría estar duplicado.");
            return false;
        }
    }

    // 📖 READ ALL: Ver todos los usuarios
    public static List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = ConnectionBD.getInstance().getConnection();

        // Solo dejamos dentro del try los statements y resultsets para que se limpien ellos solos
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SQL_ALL)) {

            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password");
                usuarios.add(new Usuario(idUsuario, nombre, email, password));
            }
        } catch (SQLException e) {
            Utils.mostrarAlerta("Error de Base de Datos", "No se pudo cargar la lista de usuarios.");
            e.printStackTrace();
        }
        return usuarios;
    }


    // READ BY ID: Versión Lazy
    public static Usuario findById(int idUsuario) {
        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idUsuario2 = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    return new Usuario(idUsuario2, nombre, email, password);
                }
            }
        } catch (SQLException e) {
            Utils.mostrarAlerta("Error de Búsqueda", "No se pudo encontrar al usuario con ID: " + idUsuario);
        }
        return null;
    }


    //LOGIN: Autenticación de usuario
    public static Usuario login(String email, String password) {
        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_LOGIN)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idUsuario = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String emailBd = rs.getString("email");
                    String passwordBd = rs.getString("password");

                    Usuario usuario = new Usuario(idUsuario, nombre, emailBd, passwordBd);
                    usuario.setId_usuario(idUsuario);
                    return usuario;
                }
            }
        } catch (SQLException e) {
            Utils.mostrarAlerta("Error de Autenticación", "Ocurrió un problema al validar tus credenciales.");
        }
        return null;
    }

    //UPDATE: Modificar datos del usuario
    public static boolean update(Usuario u) {
        Connection con = ConnectionBD.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setInt(4, u.getId_usuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Utils.mostrarAlerta("Error de Modificación", "No se pudieron actualizar los datos del usuario. El email podría estar duplicado.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina permanentemente a un usuario y todo su historial de partidas asociado.
     * Utiliza una transacción para garantizar la integridad referencial, deshabilitando
     * temporalmente las restricciones de claves foráneas durante la operación.
     *
     * @param idUsuario El ID único del usuario que se desea eliminar.
     * @return true si el usuario fue eliminado con éxito, false en caso contrario o si ocurrió un error.
     */
    // DELETE: Eliminar cuenta a usuario
    public static boolean delete(int idUsuario) {
        Connection con = ConnectionBD.getInstance().getConnection();

        String sqlDesactivarFK = "SET FOREIGN_KEY_CHECKS = 0";

        String sqlBorrarPartidas = "DELETE FROM partidas WHERE idUsuario = ?";

        String sqlBorrarUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
        String sqlActivarFK = "SET FOREIGN_KEY_CHECKS = 1";

        try {
            con.setAutoCommit(false);

            // 1. Apagamos control de claves foráneas
            try (Statement st = con.createStatement()) {
                st.executeUpdate(sqlDesactivarFK);
            }

            // 2. Borramos historial de partidas usando el nombre correcto de columna
            try (PreparedStatement psPartidas = con.prepareStatement(sqlBorrarPartidas)) {
                psPartidas.setInt(1, idUsuario);
                psPartidas.executeUpdate();
                System.out.println("[ADMIN] Historial de partidas limpiado para el usuario: " + idUsuario);
            }

            // 3. Borramos definitivamente al usuario
            boolean usuarioBorrado = false;
            try (PreparedStatement psUsuario = con.prepareStatement(sqlBorrarUsuario)) {
                psUsuario.setInt(1, idUsuario);
                usuarioBorrado = psUsuario.executeUpdate() > 0;
            }

            // 4. Volvemos a encender el control de claves foráneas
            try (Statement st = con.createStatement()) {
                st.executeUpdate(sqlActivarFK);
            }

            con.commit();
            con.setAutoCommit(true);

            return usuarioBorrado;

        } catch (SQLException e) {
            try {
                System.out.println("Error detectado en el borrado. Aplicando Rollback...");
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            Utils.mostrarAlerta("Error de Eliminación", "No se pudo eliminar el usuario debido a un problema con las columnas de la BD.");
            e.printStackTrace();
            return false;
        }
    }
}