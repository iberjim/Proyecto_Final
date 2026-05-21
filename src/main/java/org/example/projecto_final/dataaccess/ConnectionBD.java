package org.example.projecto_final.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class ConnectionBD {

        private final String FILE = "connection.xml";
        private Connection con;
        private ConnectionProperties properties;

        //instancia singleton
        private static ConnectionBD _instance;

        //2. Constructor privado
        private ConnectionBD() {
            //leo del connection.xml los datos para la conexion
            properties = XMLManager.readXML(new ConnectionProperties(), FILE);
        }

        public void connect() throws SQLException {
            try {
                con = DriverManager.getConnection(properties.getURL(), properties.getUser(), properties.getPassword());
            } catch (SQLException e) {
                con = null;
                e.printStackTrace();
                throw e;
            }
        }

        private void disconnect() {
            con = null;
        }

        private boolean isConnected() {
            return con != null;
        }

        //3. metodo publico que me devuelve la instancia ya creada, si la primera vez la crea

        public static ConnectionBD getInstance() {
            if (_instance == null) {
                _instance = new ConnectionBD();
            }
            return _instance;
        }

        public Connection getConnection() {
            try {
                // Si la conexión es nula o se ha cerrado por llevar tiempo inactiva, la creamos
                if (con == null || con.isClosed()) {
                    System.out.println("Conexión nula o cerrada. Conectando a la base de datos...");
                    connect();
                }
            } catch (SQLException e) {
                System.out.println("Error crítico al intentar conectar automáticamente desde getConnection():");
                e.printStackTrace();
            }
            return con;
        }
    }
