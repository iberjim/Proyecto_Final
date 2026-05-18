package org.example.projecto_final.model;

public class Partidas {
    private Usuario usuario;
private int id_partida;
private int dia;
private int mes;
private int ano;

public enum estado{
    activo,
    finalizado;
}
    public enum modo{
    FACIL,
        MEDIO,
        DIFICIL,
        LOCO,
        UNOVSUNO
    }
public Partidas(int id_partida, int dia, int mes, int ano) {
    this.id_partida = id_partida;
    this.dia = dia;
    this.mes = mes;
    this.ano = ano;
}
    public Partidas(int id_partida, int dia, int mes, int ano, Usuario usuario) {
        this.id_partida = id_partida;
        this.usuario = usuario;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }


    public int getId_partida() {
        return id_partida;
    }

    public void setId_partida(int id_partida) {
        this.id_partida = id_partida;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Partidas partidas = (Partidas) o;
        return id_partida == partidas.getId_partida(); // o el campo identificador único
    }

}
