package com.dam.moveyourschool.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadForm;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.views.CustomDialog;

public class Actividad_Form_Step_3_Frag extends Fragment {
    private TextInputEditText etxDireccion;
    private TextInputEditText etxLocalidad;
    private TextInputEditText etxMunicipio;
    private Actividad  actividad;


    public Actividad_Form_Step_3_Frag() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        actividad = ((ActividadForm) (getActivity())).getActividad();

        if (actividad == null) {
            new CustomDialog(getContext(), R.string.EXCEPT_UNKNOWN_ERROR).show();
        }

        //Log.e("ACTIVIDAD", ((ActividadForm) (getActivity())).getActividad().toString());

        View contenedor = inflater.inflate(R.layout.fragment_actividad__form__step_3_, container, false);
        etxDireccion = contenedor.findViewById(R.id.etxDireccion);
        etxLocalidad = contenedor.findViewById(R.id.etxLocalidad);
        etxMunicipio = contenedor.findViewById(R.id.etxMunicipio);


        return contenedor;
    }

    public void registrar() {

        if (checkFields(etxDireccion) || checkFields(etxLocalidad) || checkFields(etxMunicipio)) {

        } else {

            actividad.setDireccion(etxDireccion.getText().toString());
            actividad.setMunicipio(etxMunicipio.getText().toString());
            actividad.setLocalidad(etxLocalidad.getText().toString());
            Log.e("actividad", actividad.toString());
        }

    }

    private boolean checkFields(TextInputEditText field) {
        if (field.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}
