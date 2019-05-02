package com.dam.moveyourschool.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadForm;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.dam.moveyourschool.views.CustomDialog;
import com.dam.moveyourschool.views.ProgressBarAlert;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class Actividad_Form_Step_3_Frag extends Fragment {
    private TextInputEditText etxDireccion;
    private TextInputEditText etxLocalidad;
    private TextInputEditText etxMunicipio;
    private TextInputEditText etxCodigoPostal;
    private CheckBox checkBoxDir;
    private FireDBUsuarios serviceDBusuarios;
    private Actividad  actividad;
    private Usuario usuarioActual;
    private ProgressBarAlert progressBarAlert;


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
        checkBoxDir = contenedor.findViewById(R.id.checkDir);
        checkBoxDir.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.bambino_light));
        etxCodigoPostal = contenedor.findViewById(R.id.etxCodigoPostal);
        progressBarAlert = new ProgressBarAlert(getActivity());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            usuarioActual = new Usuario();
            usuarioActual.setUid(uid);
        }




        serviceDBusuarios = new FireDBUsuarios() {
            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (usuarioActual != null) {

                    Usuario aux = dataSnapshot.getValue(Usuario.class);
                    if (aux.getUid().equals(usuarioActual.getUid())) {

                        if (aux.getDireccion() != null && !aux.getDireccion().equals("")) {
                            usuarioActual.setDireccion(aux.getDireccion());
                        }

                        if (aux.getMunicipio() != null && !aux.getMunicipio().equals("")) {
                            usuarioActual.setMunicipio(aux.getMunicipio());
                        }

                        if (aux.getLocalidad() != null && !aux.getLocalidad().equals("")) {
                            usuarioActual.setLocalidad(aux.getLocalidad());
                        }

                        if (aux.getCodigo_postal() != null && !aux.getCodigo_postal().equals("")) {
                            usuarioActual.setCodigo_postal(aux.getCodigo_postal());
                        }
                    }

                }

            }

            @Override
            public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void nodoEliminado(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void nodoMovido(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void nodoCancelado(@NonNull DatabaseError databaseError) {

            }
        };
        checkBoxDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean dir = false;

                if (checkBoxDir.isChecked()) {

                    if (usuarioActual != null) {

                        if (usuarioActual.getDireccion() != null && !usuarioActual.getDireccion().equals("")) {
                            etxDireccion.setText(usuarioActual.getDireccion());
                            dir = true;
                        }

                        if (usuarioActual.getMunicipio() != null && !usuarioActual.getMunicipio().equals("")) {
                            etxMunicipio.setText(usuarioActual.getMunicipio());
                            dir = true;
                        }

                        if (usuarioActual.getLocalidad() != null && !usuarioActual.getLocalidad().equals("Localidad")) {
                            etxLocalidad.setText(usuarioActual.getLocalidad());
                            dir = true;
                        }

                        if (usuarioActual.getCodigo_postal() != null && !usuarioActual.getCodigo_postal().equals("")) {
                            etxCodigoPostal.setText(usuarioActual.getCodigo_postal());
                            dir = true;
                        }

                        if (!dir) {
                            new CustomDialog(getActivity(), R.string.USUARIO_SIN_DATOS).show();
                            checkBoxDir.setChecked(false);
                        }
                    }
                } else {
                    etxDireccion.setText("");
                    etxMunicipio.setText("");
                    etxLocalidad.setText("");
                    etxCodigoPostal.setText("");
                }
            }
        });


        return contenedor;
    }

    public boolean registrar() {
        boolean success = false;
        boolean exceptions = true;

        if (checkFields(etxDireccion) || checkFields(etxLocalidad) || checkFields(etxMunicipio) || checkFields(etxCodigoPostal)) {
            new CustomDialog(getActivity(), R.string.EXCEPT_RELLENAR_TODOS_CAMPOS).show();
        } else {
            try{
                Integer.parseInt(etxCodigoPostal.getText().toString());
                exceptions = false;

            } catch (NumberFormatException e) {
                new CustomDialog(getActivity(), R.string.EXCEPT__CODPOSTAL_CAMPO_NUMERICO).show();
            }

            if (!exceptions) {
                actividad.setDireccion(etxDireccion.getText().toString());
                actividad.setMunicipio(etxMunicipio.getText().toString());
                actividad.setLocalidad(etxLocalidad.getText().toString());
                success = true;
                Log.e("actividad", actividad.toString());
            }
        }
        return success;
    }

    private boolean checkFields(TextInputEditText field) {
        if (field.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}
