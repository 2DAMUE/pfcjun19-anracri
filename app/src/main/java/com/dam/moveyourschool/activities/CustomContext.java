package com.dam.moveyourschool.activities;

import android.app.Application;

import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.bean.Usuario;

public class CustomContext extends Application {
    private Actividad actividad;
    private Actividad modActivity;
    private Usuario usuario;

    public Actividad getActividad() {
        return  actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Actividad getModActivity() {
        return modActivity;
    }

    public void setModActivity(Actividad modActivity) {
        this.modActivity = modActivity;
    }
}
