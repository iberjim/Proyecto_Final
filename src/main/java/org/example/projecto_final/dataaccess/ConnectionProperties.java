package org.example.projecto_final.dataaccess;

import jakarta.xml.bind.annotation.XmlAccessType;

import jakarta.xml.bind.annotation.XmlAccessorType;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="connection")
public class ConnectionProperties implements Serializable {
    private String server;
    private String port;
    private String dataBase;
    private String user;
    private String password;

    public ConnectionProperties(){}

    public ConnectionProperties (String server, String port, String dataBase, String user, String password) {
        this.server = server;
        this.port = port;
        this.dataBase=dataBase;
        this.user=user;
        this.password=password;
    }
    public String getUser(){return user;}
    public String getPassword(){return password;}
    public String getURL(){ return "jdbc:mysql://"+server+":"+port+"/"+dataBase;}





}
