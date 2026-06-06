package org.example.projecto_final.model;

import java.sql.Date;
import java.sql.Time;

public class Partidas {
    private int idPartida;
    private Date fecha;
    private Time hora;
    private int puntuacion;


    private Usuario usuario;
    private int idModo;

    // 1. Constructor completo
    public Partidas(int idPartida, Date fecha, Time hora, int puntuacion, Usuario usuario, int idModo) {
        this.idPartida = idPartida;
        this.fecha = fecha;
        this.hora = hora;
        this.puntuacion = puntuacion;
        this.usuario = usuario;
        this.idModo = idModo;
    }

    // 2. Constructor para guardar una partida nueva
    public Partidas(Date fecha, Time hora, int puntuacion, Usuario usuario, int idModo) {
        this.fecha = fecha;
        this.hora = hora;
        this.puntuacion = puntuacion;
        this.usuario = usuario;
        this.idModo = idModo;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdPartida() { return idPartida; }
    public void setIdPartida(int idPartida) { this.idPartida = idPartida; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Time getHora() { return hora; }
    public void setHora(Time hora) { this.hora = hora; }

    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }


    public int getIdModo() { return idModo; }
    public void setIdModo(int idModo) { this.idModo = idModo; }

    // Si no tiene nombre el ganador se le pondra anonimo
    public String getNombreJugador() {
        return (usuario != null) ? usuario.getNombre() : "Anónimo";
    }
}