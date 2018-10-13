package com.example.uer.trabajogradofittness.Persona;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ListaRanking {

    private int posicion;
    private int id;
    private Bitmap imagen;
    private String nombre;
    private Drawable insignia;
    private String nivel;
    private String puntos;

    public ListaRanking(int posicion, Bitmap imagen, String nombre, Drawable insignia, String nivel, String puntos) {
        this.posicion = posicion;
        this.imagen = imagen;
        this.nombre = nombre;
        this.insignia = insignia;
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

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Drawable getInsignia() {
        return insignia;
    }

    public void setInsignia(Drawable insignia) {
        this.insignia = insignia;
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
