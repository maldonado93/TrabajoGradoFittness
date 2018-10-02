package com.example.uer.trabajogradofittness.Rutina;

public class ListaEjercicioRutina {

    private String id;
    private String categoria;
    private String nombre;
    private String variable;
    private String series;

    public ListaEjercicioRutina(String id, String categoria, String nombre, String variable, String series) {
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.variable = variable;
        this.series = series;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }
}
