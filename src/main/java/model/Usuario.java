package model;

import java.util.List;

public class Usuario {

    private int id_usuario;

    private String nombre;
    private String email;
    private String password;


    public Usuario(int id_usuario, String nombre, String email, String password) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
    public Usuario( String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
    public Usuario(int id, String nombre, String email, String password, List<Partidas> partidas)
        {
        this.id_usuario = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        }
    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
