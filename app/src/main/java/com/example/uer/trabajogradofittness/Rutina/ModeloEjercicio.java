package com.example.uer.trabajogradofittness.Rutina;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ModeloEjercicio {

    private String id;
    private Bitmap imagen;
    private String nombre;
    private String categoria;
    private String descripcion;
    private String dato;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        dato = dato;

        try{
            byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
            this.imagen = BitmapFactory.decodeByteArray(byteCode, 0 ,byteCode.length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
