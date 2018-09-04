package com.example.uer.trabajogradofittness.BD;

public class DatosEntreno {

    public static final String TABLA_DATOS_ENTRENO = "datos_entreno";

    public static final String ID = "id";
    public static final String ID_REGISTRO = "id_registro";
    public static final String BPM = "bpm";

    public static final String CREAR_TABLA_DATOS_ENTRENO="create table "+TABLA_DATOS_ENTRENO+"("+ID+" INTEGER PRIMARY KEY, "+ID_REGISTRO+" INTEGER, "+BPM+" INTEGER)";

    private int id;
    private int id_registro;
    private int bpm;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_registro() {
        return id_registro;
    }

    public void setId_registro(int id_registro) {
        this.id_registro = id_registro;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
}
