package com.example.uer.trabajogradofittness;

import android.app.Application;

public class GlobalState extends Application{

    public String ip = "piperomero1226.000webhostapp.com";

    public int sesion_usuario = 0;
    public int tipo_usuario = 0;
    public int id_registro_entreno = 0;
    public int id_rutina = 0;
    public int id_alumno = 0;
    public String fragmentActual = null;

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

    public int getId_rutina() {
        return id_rutina;
    }

    public void setId_rutina(int id_rutina) {
        this.id_rutina = id_rutina;
    }

    public int getId_alumno() {
        return id_alumno;
    }

    public void setId_alumno(int id_alumno) {
        this.id_alumno = id_alumno;
    }

    public String getFragmentActual() {
        return fragmentActual;
    }

    public void setFragmentActual(String fragmentActual) {
        this.fragmentActual = fragmentActual;
    }
}