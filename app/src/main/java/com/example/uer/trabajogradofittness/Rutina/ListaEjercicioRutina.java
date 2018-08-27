package com.example.uer.trabajogradofittness.Rutina;

public class ListaEjercicioRutina {

    private String id;
    private String nombre;
    private String serie;

    public ListaEjercicioRutina(String id, String nombre, String serie) {
        this.id = id;
        this.nombre = nombre;
        this.serie = serie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }
}
