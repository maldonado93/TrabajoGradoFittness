package com.example.uer.trabajogradofittness.Rutina;

public class ListaRutinas {

    private String id;
    private String rutina;
    private String orientacion;
    private String cantidad;

    public ListaRutinas(String id, String rutina, String orientacion, String cantidad) {
        this.id = id;
        this.rutina = rutina;
        this.orientacion = orientacion;
        this.cantidad = cantidad;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRutina() {
        return rutina;
    }

    public void setRutina(String rutina) {
        this.rutina = rutina;
    }

    public String getCategoria() {
        return orientacion;
    }

    public void setCategoria(String orientacion) {
        this.orientacion = orientacion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
