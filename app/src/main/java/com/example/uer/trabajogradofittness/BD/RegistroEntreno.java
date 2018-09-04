package com.example.uer.trabajogradofittness.BD;

public class RegistroEntreno {

    public static final String TABLA_REGISTRO_ENTRENO = "registro_entreno";

    public static final String ID = "id";
    public static final String ID_RUTINA = "id_rutina";
    public static final String ID_PERSONA = "id_persona";
    public static final String TIEMPO = "tiempo";
    public static final String FECHA = "fecha";
    public static final String HORA = "hora";

    public static final String CREAR_TABLA_REGISTRO_ENTRENO="create table "+TABLA_REGISTRO_ENTRENO+"("+ID+" INTEGER PRIMARY KEY, "+ID_RUTINA+" INTEGER, "+
            ""+ID_PERSONA+" INTEGER, "+TIEMPO+" TEXT, "+FECHA+" DATE, "+HORA+" TEXT)";


    private int id;
    private int id_rutina;
    private int id_persona;
    private String tiempo;
    private String fecha;
    private String hora;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_rutina() {
        return id_rutina;
    }

    public void setId_rutina(int id_rutina) {
        this.id_rutina = id_rutina;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
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
}
