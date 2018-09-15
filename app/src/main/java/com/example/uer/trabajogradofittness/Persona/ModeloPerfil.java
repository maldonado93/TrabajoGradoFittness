package com.example.uer.trabajogradofittness.Persona;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ModeloPerfil {

    private String dato;
    private Bitmap foto;
    private String id_tipo_identificacion;
    private String identificacion;
    private String nombre;
    private String email;
    private String movil;
    private String localidad;

    private String peso;
    private String estatura;


    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getId_tipo_identificacion() {
        return id_tipo_identificacion;
    }

    public void setId_tipo_identificacion(String id_tipo_identificacion) {
        this.id_tipo_identificacion = id_tipo_identificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        dato = dato;

        try{
            byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
            this.foto = BitmapFactory.decodeByteArray(byteCode, 0 ,byteCode.length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getEstatura() {
        return estatura;
    }

    public void setEstatura(String estatura) {
        this.estatura = estatura;
    }

}
