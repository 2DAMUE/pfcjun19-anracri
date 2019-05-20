package com.dam.moveyourschool.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Reserva;

public class Reserva_info_empresa extends BaseActivity {

    TextView tituloRes;
    TextInputEditText fecha;
    TextInputEditText hora;
    TextInputEditText cliente;
    TextInputEditText precioFinal;
    TextView estadoActual;
    ImageView imgRes;
    Reserva res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tituloRes = findViewById(R.id.txtTituloResInfoEmpresa);
        fecha = findViewById(R.id.fechaInforReservaEmpresa);
        hora = findViewById(R.id.horaInforReservaEmpresa);
        precioFinal = findViewById(R.id.precioFinalInforReservaEmpresa);
        imgRes = findViewById(R.id.imgResInfoEmpresa);
        cliente = findViewById(R.id.infoClienteEmpresa);
        estadoActual = findViewById(R.id.txtInfoEstadoEmpresa);

        res = getIntent().getParcelableExtra(getString(R.string.KEY_RESERVA_INFO));
        System.out.println(res);

        tituloRes.setText(res.getTituloActiRef());
        fecha.setText(res.getFecha());
        hora.setText(res.getHora());

        if(res.getUrlFotoAct() != null){
            Glide.with(this).load(res.getUrlFotoAct()).into(imgRes);
        }

        double precioF = res.getNumPersonas() * res.getPrecioFinal();
        precioFinal.setText(String.valueOf(precioF));

        tituloRes.setEnabled(false);
        fecha.setEnabled(false);
        hora.setEnabled(false);
        precioFinal.setEnabled(false);
        cliente.setEnabled(false);

        if (res.getEstado().equals("ACEPTADA")){
            estadoActual.setText("El cliente ha aceptado tu presupuesto");
            estadoActual.setCompoundDrawablesRelative(getDrawable(R.drawable.ic_aceptado),null,null,null);
        }else if(res.getEstado().equals("RECHAZADA")){
            estadoActual.setText("El cliente ha rechazado tu presupuesto");
            estadoActual.setCompoundDrawablesRelative(getDrawable(R.drawable.ic_rechazado),null,null,null);
        }else{
            estadoActual.setText("El cliente aun no ha respondido");
            estadoActual.setCompoundDrawablesRelative(getDrawable(R.drawable.ic_pendiente),null,null,null);
        }

    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_reserva_info;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }
}
