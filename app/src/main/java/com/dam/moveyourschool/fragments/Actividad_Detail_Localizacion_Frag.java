package com.dam.moveyourschool.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadDetail;
import com.dam.moveyourschool.bean.Actividad;

public class Actividad_Detail_Localizacion_Frag extends Fragment {
    private TextView valDireccion;
    private TextView valMunicipio;
    private TextView valLocalidad;


    public Actividad_Detail_Localizacion_Frag() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividad__detail__localizacion_, container, false);

        valDireccion = view.findViewById(R.id.ValDireccion);
        valMunicipio = view.findViewById(R.id.ValMunicipio);
        valLocalidad = view.findViewById(R.id.valLocalidad);

        Actividad actividad = (((ActividadDetail) (getActivity())).getActividad());

        if (actividad != null) {

            if (actividad.getLocalidad() != null && !actividad.getLocalidad().equals("")) {
                valLocalidad.setText(actividad.getLocalidad());
            }

            if (actividad.getMunicipio() != null && !actividad.getMunicipio().equals("")) {
                valMunicipio.setText(actividad.getMunicipio());
            }

            if (actividad.getDireccion() != null && !actividad.getDireccion().equals("")) {
                valDireccion.setText(actividad.getDireccion());
            }
        }

        return view;
    }

}
