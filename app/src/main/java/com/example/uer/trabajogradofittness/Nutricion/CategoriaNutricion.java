package com.example.uer.trabajogradofittness.Nutricion;

public class CategoriaNutricion {

    private String categoria;
    private int imagen;

    public CategoriaNutricion() {
    }

    public CategoriaNutricion(String categoria, int imagen) {
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public CategoriaNutricion(String categoria) {
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
