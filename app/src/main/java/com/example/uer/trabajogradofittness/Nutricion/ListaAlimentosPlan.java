package com.example.uer.trabajogradofittness.Nutricion;

public class ListaAlimentosPlan {
    
    private int id;
    private int cantidad;
    private String nombre;
    private String categoria;
    private float calorias;
    private float proteinas;
    private float carbohidratos;
    private int seleccion;

    public ListaAlimentosPlan(int id, int cantidad, String nombre, String categoria, float calorias, float proteinas, float carbohidratos) {
        this.id = id;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.categoria = categoria;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;

    }

    public ListaAlimentosPlan(int id, int cantidad, String nombre, String categoria, float calorias, float proteinas, float carbohidratos, int seleccion) {
        this.id = id;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.categoria = categoria;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.seleccion = seleccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getCalorias() {
        return calorias;
    }

    public void setCalorias(float calorias) {
        this.calorias = calorias;
    }

    public float getProteinas() {
        return proteinas;
    }

    public void setProteinas(float proteinas) {
        this.proteinas = proteinas;
    }

    public float getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(float carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }
}
