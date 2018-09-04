package com.example.uer.trabajogradofittness.BD;

public class Usuario {

    public static final String TABLA_USUARIO = "usuario";

    public static final String ID = "id";
    public static final String USUARIO = "usuario";
    public static final String PASSWORD = "password";
    public static final String ID_TIPO_USUARIO = "id_tipo_usuario";
    public static final String ID_PERSONA = "id_persona";

    public static final String CREAR_TABLA_USUARIO="create table "+TABLA_USUARIO+"("+ID+" INTEGER, "+USUARIO+" TEXT, "+
            ""+PASSWORD+" TEXT, "+ID_TIPO_USUARIO+" INTEGER, "+ID_PERSONA+" INTEGER)";


    private int id;
    private String usuario;
    private String password;
    private int id_tipo_usuario;
    private int id_persona;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId_tipo_usuario() {
        return id_tipo_usuario;
    }

    public void setId_tipo_usuario(int id_tipo_usuario) {
        this.id_tipo_usuario = id_tipo_usuario;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }
}
