package com.dam.moveyourschool.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Switch;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadForm;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.views.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;

public class Actividad_Form_Step_4 extends Fragment {
    private Actividad actividad;
    private Switch swDiversidad;
    private Switch swAseos;
    private Switch swRiesgo;
    private Switch swCafeteria;
    private Switch swBloquear;
    private NumberPicker pickerDuracion;
    private boolean modificar;


    public Actividad_Form_Step_4() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        modificar = ((ActividadForm) getActivity()).getModificar();

        View contenedor = inflater.inflate(R.layout.fragment_actividad__form__step_4, container, false);
        swDiversidad = contenedor.findViewById(R.id.swDiversidad);
        swAseos = contenedor.findViewById(R.id.swAseos);
        swRiesgo = contenedor.findViewById(R.id.swRiesgo);
        swBloquear = contenedor.findViewById(R.id.swBloqueo);
        swCafeteria = contenedor.findViewById(R.id.swCafeteria);
        pickerDuracion = contenedor.findViewById(R.id.pickerDuracion);
        pickerDuracion.setMinValue(0);
        pickerDuracion.setMaxValue(500);
        pickerDuracion.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                actividad.setDuracion(numberPicker.getValue());
            }
        });

        actividad = ((ActividadForm) (getActivity())).getActividad();

        if (actividad == null) {
            new CustomDialog(getContext(), R.string.EXCEPT_UNKNOWN_ERROR).show();
        }

        if (modificar) {
            swDiversidad.setChecked(actividad.isPersonasDiversidad());
            swAseos.setChecked(actividad.isAseos());
            swRiesgo.setChecked(actividad.isRiesgoAccidente());
            swCafeteria.setChecked(actividad.isCafeteria());
            pickerDuracion.setValue(actividad.getDuracion());
            swBloquear.setChecked(actividad.isDisponible());
        }

        return contenedor;
    }

    public boolean registrar() {
        boolean success = false;

        if (actividad.getDuracion() != 0) {

            actividad.setAseos(swAseos.isChecked());
            actividad.setPersonasDiversidad(swDiversidad.isChecked());
            actividad.setRiesgoAccidente(swRiesgo.isChecked());
            actividad.setRiesgoAccidente(swRiesgo.isChecked());
            actividad.setCafeteria(swCafeteria.isChecked());
            actividad.setDisponible(swBloquear.isChecked());

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                actividad.setUid_usuario(FirebaseAuth.getInstance().getCurrentUser().getUid());
                success = true;

            } else {
                new CustomDialog(getContext(), R.string.USUARIO_CADUCADO).show();
            }

        } else {
            new CustomDialog(getContext(), R.string.EXCEPT_DURACION).show();
        }
        return success;
    }

    private boolean comprobacionFinal() {
        return true;
    }
}
