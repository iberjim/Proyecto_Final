package org.example.projecto_final.DAO;

import org.example.projecto_final.dataaccess.ConnectionBD;
import org.example.projecto_final.model.Partidas;
import org.example.projecto_final.model.Usuario;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UsuarioDAO {
    private final static String SQL_LOGIN = "SELECT * FROM usuario WHERE email = ? AND password = ?";
    private final static String SQL_ALL = "SELECT * FROM usuario";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM usuario WHERE id_usuario = ?";
    private final static String SQL_FIND_BY_NAME = "SELECT * FROM usuario WHERE nombre = ?";
    private final static String SQL_INSERT = "INSERT INTO usuario (nombre, email, password) VALUES (?, ?, ?)";
    private final static String SQL_UPDATE = "UPDATE usuario SET nombre = ? WHERE id_usuario = ?";
    private final static String SQL_DELETE = "DELETE FROM usuario WHERE id_usuario = ?";

    public static boolean insert(Usuario u) throws SQLException {

        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_INSERT)) {
           ps.setString(1, u.getNombre());
           ps.setString(2, u.getEmail());
           ps.setString(3, u.getPassword());

           return ps.executeUpdate() > 0; // Devuelve true si se insertó al menos una fila
        }
    }

public static List<Usuario> findAll() throws SQLException{

    Usuario usuario = null;
    List<Usuario> usuarios = new ArrayList<>();
    Connection con;
    con = ConnectionBD.getInstance().getConnection();
    //Creamos un objeto Statement
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(SQL_ALL);
    while(rs.next()){
        int idUsuario = rs.getInt("id_usuario");
        String nombre = rs.getString("nombre");
        String email = rs.getString("email");
        String password = rs.getString("password");
        usuario = new Usuario(idUsuario, nombre, email, password);
        usuarios.add(usuario);
    }
    return usuarios;
}

    /**
     * Versión EAGER del Método que devuelve una lista con todos los usuarios almacenados en la tabla Usuario de la bbdd tres_en_raya,
     * * SI obtengo la lista de partidas de cada Usuario
     * * @return lista con todos los Usuarios
     */

public static List<Usuario> findByEagle() throws SQLException{

    Usuario usuario = null;
    List<Usuario> usuarios = new ArrayList<>();
    //conectamos a la bd
    Connection con;
    con = ConnectionBD.getInstance().getConnection();
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(SQL_ALL);
    while(rs.next()){
        int idUsuario = rs.getInt("id_usuario");
        String nombre = rs.getString("nombre");
        String email = rs.getString("email");
        String password = rs.getString("password");
        usuario = new Usuario(idUsuario, nombre, email, password);
        usuarios.add(usuario);
    }
    return usuarios;

}

/**
 * Método que devuelve un objeto Usuairo por su id, en versión Lazy, ya que no estamos rellanado la lista de partidas de ese Usuario
 *
 * @param idUsuario id del usuario
 * @return devuel el objeto de tipo Usuario con el id del Usuario, y el nombre del Usuario, si lo ha encontrado en la bbdd, sino devuelve NULL
 */

public static Usuario findById(int idUsuario) throws SQLException {
    Usuario usuario = null;
    try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_FIND_BY_ID)) {
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            int idUsuario2 = rs.getInt("id_usuario");
            String nombre = rs.getString("nombre");
            String email = rs.getString("email");
            String password = rs.getString("password");
            usuario = new Usuario(idUsuario2, nombre, email, password);
        }
    }
    return usuario;
}

    /**
     * Método que devuelve un objeto Usuario por su id, en versión Eager,es decir, la lista de partidas de ese Usuario se obtiene en la misma consulta
     *
     * @param idUsuario id del Usuario
     * @return devuelve el objeto de tipo Usuario con el id del Usuario, y el nombre del Usuario, si lo ha encontrado en la bbdd, sino devuelve NULL
     */

    public static Usuario findByIdEager(int idUsuario) throws SQLException {
            Usuario usuario = null;
            try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_FIND_BY_ID)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    int idUsuario2 = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    //esta linea de abajo saca la lista de partidas de ese usuario
                    List<Partidas> partidas = PartidasDAO.findByIdUsuario(idUsuario);
                    usuario = new Usuario(idUsuario2, nombre, email, password, partidas);
                }
            }
            return usuario;
    }
    /**
     * Método que busca un usuario por su email y contraseña para el inicio de sesión.
     * @return El objeto Usuario si las credenciales son correctas, o null si no coincide ninguna.
     */
    public static Usuario login(String email, String password) throws SQLException {
        Usuario usuario = null;

        try (PreparedStatement ps = ConnectionBD.getInstance().getConnection().prepareStatement(SQL_LOGIN)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idUsuario = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String emailBd = rs.getString("email");
                    String passwordBd = rs.getString("password");

                    // Creamos el objeto con el constructor
                    usuario = new Usuario(idUsuario, nombre, emailBd, passwordBd);


                    usuario.setId_usuario(idUsuario);

                }
            }
        }
        return usuario; // Devuelve el usuario encontrado, o null si fallaron los datos
    }













}
