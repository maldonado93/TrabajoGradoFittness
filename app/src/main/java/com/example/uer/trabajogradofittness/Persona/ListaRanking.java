package com.example.uer.trabajogradofittness.Persona;

public class ListaRanking {

    private int posicion;
    private int id;
    private String imagen;
    private String nombre;
    private String nivel;
    private String puntos;

    public ListaRanking(int posicion, String imagen, String nombre, String nivel, String puntos) {
        this.posicion = posicion;
        this.imagen = imagen;
        this.nombre = nombre;
        this.nivel = nivel;
        this.puntos = puntos;
    }

    public ListaRanking(int posicion, int id, String nombre, String nivel, String puntos) {
        this.posicion = posicion;
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.puntos = puntos;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }
}
