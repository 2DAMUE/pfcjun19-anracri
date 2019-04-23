package com.dam.moveyourschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    private String uid;
    private String cif;
    private String titular;
    private String email;
    private String direccion;
    private String localidad;
    private String municipio;
    private String codigo_postal;
    private String tipo;
    private String descripcion;
    private String urlFoto;

    public Usuario(){}

    protected Usuario(Parcel in) {
        uid = in.readString();
        cif = in.readString();
        titular = in.readString();
        email = in.readString();
        direccion = in.readString();
        localidad = in.readString();
        municipio = in.readString();
        codigo_postal = in.readString();
        tipo = in.readString();
        descripcion = in.readString();
        urlFoto = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(cif);
        dest.writeString(titular);
        dest.writeString(email);
        dest.writeString(direccion);
        dest.writeString(localidad);
        dest.writeString(municipio);
        dest.writeString(codigo_postal);
        dest.writeString(tipo);
        dest.writeString(descripcion);
        dest.writeString(urlFoto);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
}
