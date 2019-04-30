package com.dam.moveyourschool.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadForm;
import com.dam.moveyourschool.bean.Actividad;

public class Actividad_Form_Step2_Frag extends Fragment {
    private TextInputEditText etxTitulo;
    private TextInputEditText etxDescripcion;
    private TextInputEditText etxPrecio;
    private TextInputEditText etxMaxPersonas;
    private TextInputEditText etxMinPersonas;

    public Actividad_Form_Step2_Frag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contenedor = inflater.inflate(R.layout.fragment_actividad__form__step2_, container, false);
        etxTitulo = contenedor.findViewById(R.id.etxNombreActividad);
        etxMaxPersonas = contenedor.findViewById(R.id.etxMaximoPersonas);
        etxMinPersonas = contenedor.findViewById(R.id.etxMinPersonas);
        etxDescripcion = contenedor.findViewById(R.id.etxDescripcion);
        etxPrecio = contenedor.findViewById(R.id.etxPrecio);

        return contenedor;
    }

    public void onPickImage(View view) {

    }

    public void registrar() {

        if (checkFields(etxTitulo) || checkFields(etxDescripcion) || checkFields(etxPrecio) || checkFields(etxMaxPersonas) || checkFields(etxMinPersonas) ) {

        } else {
            Actividad actividad = new Actividad();
            actividad.setTitulo(etxTitulo.getText().toString());
            actividad.setPrecio(Double.parseDouble(etxPrecio.getText().toString()));
            actividad.setMaxPlazas(Integer.parseInt(etxMaxPersonas.getText().toString()));
            actividad.setMinPlazas(Integer.parseInt(etxMinPersonas.getText().toString()));
            actividad.setDescripcion(etxDescripcion.getText().toString());

            ((ActividadForm) (getActivity())).setActividad(actividad);
        }

    }

    private boolean checkFields(EditText field) {
        if (field.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
