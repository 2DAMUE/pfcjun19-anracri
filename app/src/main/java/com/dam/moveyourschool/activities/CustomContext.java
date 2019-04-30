package com.dam.moveyourschool.activities;

import android.app.Application;

import com.dam.moveyourschool.bean.Actividad;

public class CustomContext extends Application {
    private Actividad actividad;

    public Actividad getActividad() {
        return  actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
}
