package com.example.uer.trabajogradofittness.Rutina;

public class ListaEjercicioRutina {

    private String id;
    private String nombre;
    private String series;

    public ListaEjercicioRutina(String id, String nombre, String series) {
        this.id = id;
        this.nombre = nombre;
        this.series = series;
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

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }
}
