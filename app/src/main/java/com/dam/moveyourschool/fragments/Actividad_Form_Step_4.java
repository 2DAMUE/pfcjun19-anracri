package com.dam.moveyourschool.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadForm;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.views.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;

public class Actividad_Form_Step_4 extends Fragment {
    private Actividad actividad;
    private TextInputEditText etxDuracion;
    private Switch swDiversidad;
    private Switch swAseos;
    private Switch swRiesgo;
    private Switch swCafeteria;
    private Switch swBloquear;


    public Actividad_Form_Step_4() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contenedor = inflater.inflate(R.layout.fragment_actividad__form__step_4, container, false);
        etxDuracion = contenedor.findViewById(R.id.etxDuracion);
        swDiversidad = contenedor.findViewById(R.id.swDiversidad);
        swAseos = contenedor.findViewById(R.id.swAseos);
        swRiesgo = contenedor.findViewById(R.id.swRiesgo);
        swBloquear = contenedor.findViewById(R.id.swBloqueo);
        swCafeteria = contenedor.findViewById(R.id.swCafeteria);

        actividad = ((ActividadForm) (getActivity())).getActividad();

        if (actividad == null) {
            new CustomDialog(getContext(), R.string.EXCEPT_UNKNOWN_ERROR).show();
        }

        return contenedor;
    }

    public void registrar() {

        if (checkFields(etxDuracion)) {

        } else {

            actividad.setDuracion(Integer.parseInt(etxDuracion.getText().toString()));
            actividad.setAseos(swAseos.isChecked());
            actividad.setPersonasDiversidad(swDiversidad.isChecked());
            actividad.setRiesgoAccidente(swRiesgo.isChecked());
            actividad.setRiesgoAccidente(swRiesgo.isChecked());
            actividad.setCafeteria(swCafeteria.isChecked());
            actividad.setDisponible(swBloquear.isChecked());

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                actividad.setUid_usuario(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

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
