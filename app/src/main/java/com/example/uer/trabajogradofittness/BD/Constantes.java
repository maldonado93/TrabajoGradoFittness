package com.example.uer.trabajogradofittness.BD;

public class Constantes {

    //NOMBRES DE LAS TABLAS
    public static final String TABLA_USUARIO = "usuario";
    public static final String TABLA_REGISTRO_ENTRENO = "registro_entreno";
    public static final String TABLA_DATOS_ENTRENO = "datos_entreno";


    //CONTANTES PARA CREACION DE LAS TABLAS
    public static final String CREAR_TABLA_USUARIO="create table usuario(id INTEGER, usuario TEXT, password TEXT, " +
            "id_tipo_usuario INTEGER, id_persona INTEGER)";

    public static final String CREAR_TABLA_REGISTRO_ENTRENO="create table registro_entreno(id INTEGER, id_rutina INTEGER, "+
            "id_persona INTEGER, tiempo TEXT, fecha DATE, hora TEXT)";

    public static final String CREAR_TABLA_DATOS_ENTRENO="create table usuario(id INTEGER, usuario TEXT, password TEXT, "+
            "id_tipo_usuario INTEGER, id_persona INTEGER)";

}
