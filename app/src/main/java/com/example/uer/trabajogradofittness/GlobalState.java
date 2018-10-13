package com.example.uer.trabajogradofittness;

import android.app.Application;

public class GlobalState extends Application{

    public String ip = "piperomero1226.000webhostapp.com";

    public int nivel = 1;
    public int puntos= 0;
    public double rendimiento = 1;


    public int sesion_usuario = 0;
    public int tipo_usuario = 0;
    public int id_registro_entreno = 0;
    public int id_rutina = 0;
    public String orientacion = "";
    public int id_alumno = 0;
    public String fragmentActual = null;

    public int edad = 0;

    public int registro = 0;
    public String fotoPerfil = "";
    public String usuario = "";
    public String password = "";
    public int tipoIdentificacion = 0;
    public String identificacion = "";
    public String nombres = "";
    public String apellidos = "";
    public String movil = "";
    public String email = "";
    public String genero = "";
    public String fecha = "";
    public int departamento = 0;
    public String ciudad = "";
    public int idCiudad = 0;
    public String objetivo = "";
    public int estatura = 0;
    public float peso = 0;
    public String nivelActividad = "";
    public String fumador = "";

    int indPlan;
    boolean limiteCal;
    public double[] caloriasMax = null;
    public double[] caloriasPlan = null;
    public int[] alimentos = null;
    public double[] caloria = null;
    public int[] cantidad = null;
    public String[] accion = null;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public double getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(double rendimiento) {
        this.rendimiento = rendimiento;
    }


    public int getSesion_usuario() {
        return sesion_usuario;
    }

    public void setSesion_usuario(int sesion_usuario) {
        this.sesion_usuario = sesion_usuario;
    }

    public int getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(int tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public int getId_registro_entreno() {
        return id_registro_entreno;
    }

    public void setId_registro_entreno(int id_registro_entreno) {
        this.id_registro_entreno = id_registro_entreno;
    }

    public int getId_rutina() {
        return id_rutina;
    }

    public void setId_rutina(int id_rutina) {
        this.id_rutina = id_rutina;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    public int getId_alumno() {
        return id_alumno;
    }

    public void setId_alumno(int id_alumno) {
        this.id_alumno = id_alumno;
    }

    public String getFragmentActual() {
        return fragmentActual;
    }

    public void setFragmentActual(String fragmentActual) {
        this.fragmentActual = fragmentActual;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(int tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public int getEstatura() {
        return estatura;
    }

    public void setEstatura(int estatura) {
        this.estatura = estatura;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getNivelActividad() {
        return nivelActividad;
    }

    public void setNivelActividad(String nivelActividad) {
        this.nivelActividad = nivelActividad;
    }

    public String getFumador() {
        return fumador;
    }

    public void setFumador(String fumador) {
        this.fumador = fumador;
    }


    public int getIndPlan() {
        return indPlan;
    }

    public void setIndPlan(int indPlan) {
        this.indPlan = indPlan;
    }

    public boolean isLimiteCal() {
        return limiteCal;
    }

    public void setLimiteCal(boolean limiteCal) {
        this.limiteCal = limiteCal;
    }

    public double[] getCaloriasMax() {
        return caloriasMax;
    }

    public void setCaloriasMax(double[] caloriasMax) {
        this.caloriasMax = caloriasMax;
    }

    public void insCalorias(int cant){
        this.caloriasPlan = new double[cant];
        for(int i = 0; i < this.caloriasPlan.length; i++){
            this.caloriasPlan[i] = 0;
        }
    }

    public double[] getCaloriasPlan() {
        return caloriasPlan;
    }

    public void setCaloriasPlan(double[] caloriasPlan) {
        this.caloriasPlan = caloriasPlan;
    }

    public void insAlimentos(int cant){
        this.alimentos = new int[cant];
        for(int i = 0; i < this.alimentos.length; i++){
            this.alimentos[i] = 0;
        }
    }

    public int[] getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(int[] alimentos) {
        this.alimentos = alimentos;
    }

    public void insCaloria(int cant){
        this.caloria = new double[cant];
        for(int i = 0; i < this.caloria.length; i++){
            this.caloria[i] = 0;
        }
    }

    public double[] getCaloria() {
        return caloria;
    }

    public void setCaloria(double[] caloria) {
        this.caloria = caloria;
    }

    public void insCantidades(int cant){
        this.cantidad = new int[cant];
        for(int i = 0; i < this.cantidad.length; i++){
            this.cantidad[i] = 1;
        }
    }

    public int[] getCantidad() {
        return cantidad;
    }

    public void setCantidad(int[] cantidad) {
        this.cantidad = cantidad;
    }

    public void insAccion(int cant){
        this.accion = new String[cant];
        for(int i = 0; i < this.accion.length; i++){
            this.accion[i] = "";
        }
    }

    public String[] getAccion() {
        return accion;
    }

    public void setAccion(String[] accion) {
        this.accion = accion;
    }
}