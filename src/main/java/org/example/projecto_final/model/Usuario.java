package org.example.projecto_final.model;

import java.util.List;

public class Usuario extends Persona {

    private int id_usuario;
    private String password;
    public static Usuario usuarioSesion;

    // 1. CONSTRUCTOR PRINCIPAL
    public Usuario(int id_usuario, String nombre, String email, String password) {
        super(nombre, email);
        this.id_usuario = id_usuario;
        this.password = password;
    }

    // 2. CONSTRUCTOR PARA NUEVOS REGISTROS (Sin ID porque es Auto-incremental)
    public Usuario(String nombre, String email, String password) {
        super(nombre, email);
        this.password = password;
    }

    // 3. CONSTRUCTOR EAGER (Para traer el usuario con sus partidas)
    public Usuario(int id, String nombre, String email, String password, List<Partidas> partidas) {
        super(nombre, email);
        this.id_usuario = id;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}