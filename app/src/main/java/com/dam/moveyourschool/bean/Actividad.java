package com.dam.moveyourschool.bean;

import java.io.Serializable;

public class Actividad implements Serializable {
    private String uid_actividad;
    private String uid_usuario;
    private String titulo;
    private String descripcion;
    private String urlFoto;
    private String direccion;
    private String localidad;
    private String municipio;
    private boolean aseos;
    private boolean riesgoAccidente;
    private double precio;
    private int minPlazas;
    private int maxPlazas;
    private boolean personasDiversidad;
    private boolean cafeteria;
    private int duracion;
    private int likes;
    private int notLikes;
    private String categoria;
    private boolean disponible;

    public Actividad(){}

    public String getUid_actividad() {
        return uid_actividad;
    }

    public void setUid_actividad(String uid_actividad) {
        this.uid_actividad = uid_actividad;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public boolean isAseos() {
        return aseos;
    }

    public void setAseos(boolean aseos) {
        this.aseos = aseos;
    }

    public boolean isRiesgoAccidente() {
        return riesgoAccidente;
    }

    public void setRiesgoAccidente(boolean riesgoAccidente) {
        this.riesgoAccidente = riesgoAccidente;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getMinPlazas() {
        return minPlazas;
    }

    public void setMinPlazas(int minPlazas) {
        this.minPlazas = minPlazas;
    }

    public int getMaxPlazas() {
        return maxPlazas;
    }

    public void setMaxPlazas(int maxPlazas) {
        this.maxPlazas = maxPlazas;
    }

    public boolean isPersonasDiversidad() {
        return personasDiversidad;
    }

    public void setPersonasDiversidad(boolean personasDiversidad) {
        this.personasDiversidad = personasDiversidad;
    }

    public boolean isCafeteria() {
        return cafeteria;
    }

    public void setCafeteria(boolean cafeteria) {
        this.cafeteria = cafeteria;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getNotLikes() {
        return notLikes;
    }

    public void setNotLikes(int notLikes) {
        this.notLikes = notLikes;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "uid_actividad='" + uid_actividad + '\'' +
                ", uid_usuario='" + uid_usuario + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", direccion='" + direccion + '\'' +
                ", localidad='" + localidad + '\'' +
                ", municipio='" + municipio + '\'' +
                ", aseos=" + aseos +
                ", riesgoAccidente=" + riesgoAccidente +
                ", precio=" + precio +
                ", minPlazas=" + minPlazas +
                ", maxPlazas=" + maxPlazas +
                ", personasDiversidad=" + personasDiversidad +
                ", cafeteria=" + cafeteria +
                ", duracion=" + duracion +
                ", likes=" + likes +
                ", notLikes=" + notLikes +
                ", categoria='" + categoria + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
