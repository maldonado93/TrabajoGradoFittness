package com.example.uer.trabajogradofittness.Rutina;

public class ListaCategorias {

    private String categoria;
    private int imagen;

    public ListaCategorias(String categoria, int imagen) {
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public ListaCategorias(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
