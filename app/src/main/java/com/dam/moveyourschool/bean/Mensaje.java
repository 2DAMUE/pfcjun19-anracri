package com.dam.moveyourschool.bean;

public class Mensaje {
    private String mensaje;
    private Usuario userSender;

    public Mensaje(){}

    public Mensaje(Usuario userSender, String mensaje) {
        this.userSender = userSender;
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUserSender() {
        return userSender;
    }

    public void setUserSender(Usuario userSender) {
        this.userSender = userSender;
    }
}
