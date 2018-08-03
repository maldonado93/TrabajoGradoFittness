package com.example.uer.trabajogradofittness;

import android.app.Application;

import java.io.File;

public class GlobalState extends Application{

    public String ip = "192.168.1.2";
    public int sesion_usuario = 0;
    public int tipo_usuario = 0;
    public int id_registro_entreno = 0;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getSesion_usuario() {
        return sesion_usuario;
    }

    public void setSesion_usuario(int sesion_usuario) {
        this.sesion_usuario = sesion_usuario;
    }

    public int getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(int tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public int getId_registro_entreno() {
        return id_registro_entreno;
    }

    public void setId_registro_entreno(int id_registro_entreno) {
        this.id_registro_entreno = id_registro_entreno;
    }
}