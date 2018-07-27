package com.example.uer.trabajogradofittness.Nutricion;

public class AlimentosCategoria {

    private String nombre;
    private String valorCalorias;
    private String valorProteinas;
    private String valorCarbohidratos;

    public AlimentosCategoria(String nombre, String valorCalorias, String valorProteinas, String valorCarbohidratos) {
        this.nombre = nombre;
        this.valorCalorias = valorCalorias;
        this.valorProteinas = valorProteinas;
        this.valorCarbohidratos = valorCarbohidratos;
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
