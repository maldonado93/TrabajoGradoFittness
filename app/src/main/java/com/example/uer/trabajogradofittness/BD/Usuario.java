package com.example.uer.trabajogradofittness.BD;

public class Usuario {

    private int id;
    private String usuario;
    private String password;
    private int id_tipo_usuario;
    private int id_persona;

    public Usuario(int id, String usuario, String password, int id_tipo_usuario, int id_persona) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.id_tipo_usuario = id_tipo_usuario;
        this.id_persona = id_persona;
    }

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
