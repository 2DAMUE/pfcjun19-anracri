package com.dam.moveyourschool.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class Reserva implements Parcelable {

    private String idReserva;
    private String estado;
    private String idCliente;
    private String idEmpresa;
    private double precioFinal;
    private String observaciones;
    private String fecha;
    private int numPersonas;

    public Reserva() {
    }

    public Reserva(String idReserva, String estado, String idCliente, String idEmpresa, double precioFinal, String observaciones, String fecha, int numPersonas) {
        this.idReserva = idReserva;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idEmpresa = idEmpresa;
        this.precioFinal = precioFinal;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.numPersonas = numPersonas;
    }

    protected Reserva(Parcel in) {
        idReserva = in.readString();
        estado = in.readString();
        idCliente = in.readString();
        idEmpresa = in.readString();
        precioFinal = in.readDouble();
        observaciones = in.readString();
        fecha = in.readString();
        numPersonas = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idReserva);
        dest.writeString(estado);
        dest.writeString(idCliente);
        dest.writeString(idEmpresa);
        dest.writeDouble(precioFinal);
        dest.writeString(observaciones);
        dest.writeString(fecha);
        dest.writeInt(numPersonas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reserva> CREATOR = new Creator<Reserva>() {
        @Override
        public Reserva createFromParcel(Parcel in) {
            return new Reserva(in);
        }

        @Override
        public Reserva[] newArray(int size) {
            return new Reserva[size];
        }
    };

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }



    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva='" + idReserva + '\'' +
                ", estado='" + estado + '\'' +
                ", idCliente='" + idCliente + '\'' +
                ", idEmpresa='" + idEmpresa + '\'' +
                ", precioFinal=" + precioFinal +
                ", observaciones='" + observaciones + '\'' +
                ", fecha='" + fecha + '\'' +
                ", numPersonas=" + numPersonas +
                '}';
    }

}
