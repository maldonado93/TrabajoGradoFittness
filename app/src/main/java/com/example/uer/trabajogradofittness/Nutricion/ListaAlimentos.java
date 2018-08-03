package com.example.uer.trabajogradofittness.Nutricion;

public class ListaAlimentos {

    private String id;
    private String nombre;
    private String valorCalorias;
    private String valorProteinas;
    private String valorCarbohidratos;

    public ListaAlimentos(String id, String nombre, String valorCalorias, String valorProteinas, String valorCarbohidratos) {
        this.id = id;
        this.nombre = nombre;
        this.valorCalorias = valorCalorias;
        this.valorProteinas = valorProteinas;
        this.valorCarbohidratos = valorCarbohidratos;
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

    public String getValorCalorias() {
        return valorCalorias;
    }

    public void setValorCalorias(String valorCalorias) {
        this.valorCalorias = valorCalorias;
    }

    public String getValorProteinas() {
        return valorProteinas;
    }

    public void setValorProteinas(String valorProteinas) {
        this.valorProteinas = valorProteinas;
    }

    public String getValorCarbohidratos() {
        return valorCarbohidratos;
    }

    public void setValorCarbohidratos(String valorCarbohidratos) {
        this.valorCarbohidratos = valorCarbohidratos;
    }
}
