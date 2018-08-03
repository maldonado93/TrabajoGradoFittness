package com.example.uer.trabajogradofittness.Rutina;

public class ListaEjercicios {

    private String id;
    private String imagen;
    private String nombre;

    public ListaEjercicios(String id, String imagen, String nombre) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
    }

    public ListaEjercicios(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
