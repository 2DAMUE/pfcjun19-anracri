package com.dam.moveyourschool.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadDetail;
import com.dam.moveyourschool.bean.Actividad;

public class Actividad_Detail_Detalles_Frag extends Fragment {
    private LinearLayout valAseos;
    private LinearLayout valCafeteria;
    private LinearLayout valRiesgo;
    private LinearLayout valMaxPlazas;
    private LinearLayout valMinPlazas;
    private LinearLayout valDiversidad;
    private TextView valTxtMaxPlazas;
    private TextView valTxtMinPlazas;
    private TextView valDuracion;

    public Actividad_Detail_Detalles_Frag() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividad__detail__detalles_, container, false);

        valAseos = view.findViewById(R.id.valAseos);
        valCafeteria = view.findViewById(R.id.valCafeteria);
        valRiesgo = view.findViewById(R.id.valRiesgo);
        valMaxPlazas = view.findViewById(R.id.valMaxPlazas);
        valDiversidad = view.findViewById(R.id.valDiversidad);
        valMinPlazas = view.findViewById(R.id.valMinPlazas);
        valTxtMaxPlazas = view.findViewById(R.id.valTxtMaxPlazas);
        valTxtMinPlazas = view.findViewById(R.id.valTxtMinPlazas);
        valDuracion = view.findViewById(R.id.valDuracion);

        valAseos.setVisibility(View.GONE);
        valCafeteria.setVisibility(View.GONE);
        valRiesgo.setVisibility(View.GONE);
        valMaxPlazas.setVisibility(View.GONE);
        valMinPlazas.setVisibility(View.GONE);
        valDiversidad.setVisibility(View.GONE);


        Actividad actividad = (((ActividadDetail) (getActivity())).getActividad());

        if (actividad != null) {

            valDuracion.setText(String.valueOf(actividad.getDuracion()) + "H");

            if (actividad.isAseos()) {
                valAseos.setVisibility(View.VISIBLE);
            }

            if (actividad.isCafeteria()) {
                valCafeteria.setVisibility(View.VISIBLE);
            }

            if (actividad.isRiesgoAccidente()) {
                valRiesgo.setVisibility(View.VISIBLE);
            }

            if (actividad.getMaxPlazas() != 0) {
                valMaxPlazas.setVisibility(View.VISIBLE);
                valTxtMaxPlazas.setText(String.valueOf(actividad.getMaxPlazas()));
            }

            if (actividad.getMinPlazas() != 0){
                valMinPlazas.setVisibility(View.VISIBLE);
                valTxtMinPlazas.setText(String.valueOf(actividad.getMinPlazas()));
            }

        }

        return view;
    }

}
