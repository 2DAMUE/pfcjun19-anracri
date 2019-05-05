package com.dam.moveyourschool.bean;

public class Mensaje {
    private String mensaje;
    private String hora;
    private boolean leido;
    private Usuario userSender;

    public Mensaje(){}

    public Mensaje(Usuario userSender, String mensaje, String hora, boolean leido) {
        this.userSender = userSender;
        this.mensaje = mensaje;
        this.hora = hora;
        this.leido = leido;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
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

    @Override
    public String toString() {
        return "Mensaje{" +
                "mensaje='" + mensaje + '\'' +
                ", hora='" + hora + '\'' +
                ", leido=" + leido +
                ", userSender=" + userSender +
                '}';
    }
}
