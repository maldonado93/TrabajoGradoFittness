package com.example.uer.trabajogradofittness.BD;

import android.graphics.Bitmap;

public class Persona {

    public static final String TABLA_PERSONA = "persona";

    public static final String ID = "id";
    public static final String ID_TIPO_IDENTIFICACION = "id_tipo_identificacion";
    public static final String IDENTIFICACION = "identificacion";
    public static final String NOMBRES = "password";
    public static final String APELLIDOS = "apellidos";
    public static final String EMAIL = "email";
    public static final String MOVIL = "movil";
    public static final String FOTO = "foto";
    public static final String ID_CIUDAD = "id_ciudad";

    public static final String CREAR_TABLA_PERSONA="create table "+TABLA_PERSONA+"("+ID+" INTEGER, "+ID_TIPO_IDENTIFICACION+" INTEGER, "+
            ""+IDENTIFICACION+" TEXT,"+NOMBRES+" TEXT, "+APELLIDOS+" TEXT, "+EMAIL+" TEXT, "+MOVIL+" TEXT, "+FOTO+" LONGBLOB, "+ID_CIUDAD+" INTEGER)";


    private int id;
    private int id_tipo_identificacion;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String email;
    private String movil;
    private Bitmap foto;
    private int id_ciudad;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_tipo_identificacion() {
        return id_tipo_identificacion;
    }

    public void setId_tipo_identificacion(int id_tipo_identificacion) {
        this.id_tipo_identificacion = id_tipo_identificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public int getId_ciudad() {
        return id_ciudad;
    }

    public void setId_ciudad(int id_ciudad) {
        this.id_ciudad = id_ciudad;
    }
}
