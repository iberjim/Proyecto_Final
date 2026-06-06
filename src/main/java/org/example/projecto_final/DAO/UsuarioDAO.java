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

    // ➕ CREATE: Insertar usuario
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

    // 📖 READ ALL: Versión Eager básica
    public static List<Usuario> findByEagle() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection con = ConnectionBD.getInstance().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SQL_ALL)) {

            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password");
                usuarios.add(new Usuario(idUsuario, nombre, email, password));
            }
        } catch (SQLException e) {
            Utils.mostrarAlerta("Error de Base de Datos", "No se pudo realizar la consulta de usuarios.");
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

    // READ BY ID: Versión Eager real (Trae el usuario con sus partidas)
    public static Usuario findByIdEager(int idUsuario) {
        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idUsuario2 = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    // Manejo del error controlado si falla la lectura de partidas asociadas
                    List<Partidas> partidas = new ArrayList<>();
                    try {
                        partidas = PartidasDAO.findByIdUsuario(idUsuario);
                    } catch (Exception ex) {
                        System.err.println("Error al recuperar partidas del usuario: " + ex.getMessage());
                    }

                    return new Usuario(idUsuario2, nombre, email, password, partidas);
                }
            }
        } catch (SQLException e) {
            Utils.mostrarAlerta("Error de Búsqueda", "Error al cargar los detalles del usuario.");
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

    // DELETE: Eliminar cuenta a usuario
    public static boolean delete(int idUsuario) {
        //Sacamos con del try() para proteger la conexión del Singleton
        Connection con = ConnectionBD.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Utils.mostrarAlerta("Error de Eliminación", "No se pudo borrar el usuario. Asegúrate de que no tenga partidas vinculadas primero.");
            e.printStackTrace();
            return false;
        }
    }
}