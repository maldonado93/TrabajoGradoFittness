package com.example.uer.trabajogradofittness.Nutricion;

public class ListaCategoriasAlimentos {

    private String categoria;
    private int imagen;

    public ListaCategoriasAlimentos() {
    }

    public ListaCategoriasAlimentos(String categoria, int imagen) {
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public ListaCategoriasAlimentos(String categoria) {
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
