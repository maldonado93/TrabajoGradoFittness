package com.example.uer.trabajogradofittness.RegistroEntreno;

public class ListaRegistros {

    private String idRegistro;
    private String idRutina;
    private String rutina;
    private String categoria;
    private String promedioFrecuencia;
    private String dia;
    private String fecha;
    private String hora;
    private String tiempo;

    public ListaRegistros(String idRegistro, String idRutina, String rutina, String categoria, String promedioFrecuencia, String dia, String fecha, String hora, String tiempo) {
        this.idRegistro = idRegistro;
        this.idRutina = idRutina;
        this.rutina = rutina;
        this.categoria = categoria;
        this.promedioFrecuencia = promedioFrecuencia;
        this.dia = dia;
        this.fecha = fecha;
        this.hora = hora;
        this.tiempo = tiempo;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(String idRutina) {
        this.idRutina = idRutina;
    }

    public String getRutina() {
        return rutina;
    }

    public void setRutina(String rutina) {
        this.rutina = rutina;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getPromedioFrecuencia() {
        return promedioFrecuencia;
    }

    public void setPromedioFrecuencia(String promedioFrecuencia) {
        this.promedioFrecuencia = promedioFrecuencia;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
}
