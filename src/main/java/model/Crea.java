package model;

import java.util.ArrayList;

public class Crea {
    public ArrayList<Usuario> idusuario;
    public ArrayList<Partidas> idpartida;

    public enum simbolo{
        X,
        O;
    }
    public Crea(){
        this.idusuario = new ArrayList<>();
        this.idpartida = new ArrayList<>();
    }

}
