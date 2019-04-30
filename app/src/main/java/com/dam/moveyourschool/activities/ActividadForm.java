package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.fragments.Actividad_Form_Step2_Frag;
import com.dam.moveyourschool.fragments.Actividad_Form_Step_3_Frag;
import com.dam.moveyourschool.fragments.Actividad_Form_Step_4;
import com.dam.moveyourschool.services.FireDBActividades;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ActividadForm extends BaseActivity {
    private FrameLayout fragmentContainer;
    private Actividad actividad;
    private FireDBActividades serviceDBActividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        ((CustomContext) (getApplicationContext())).setActividad(null);
        actividad = ((CustomContext) (getApplicationContext())).getActividad();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new Actividad_Form_Step2_Frag())
                .commit();

        listenService();
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_actividad_form;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }

    public void nextStep(View view) {
        //Obtenemos el fragmento que se muestra en cada momento
        Fragment fragmentoActual = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);


        //Si es el segundo fragmento, invocamos el metodo registrar y pasamos al siguiente fragmento
        if (fragmentoActual instanceof Actividad_Form_Step2_Frag) {

            ((Actividad_Form_Step2_Frag) fragmentoActual).registrar();
            changeFragment(new Actividad_Form_Step_3_Frag());

        } else if (fragmentoActual instanceof Actividad_Form_Step_3_Frag) {
            ((Actividad_Form_Step_3_Frag) fragmentoActual).registrar();
            changeFragment(new Actividad_Form_Step_4());

        } else if (fragmentoActual instanceof Actividad_Form_Step_4) {
            ((Actividad_Form_Step_4) fragmentoActual).registrar();
            startActivity(new Intent(this, ActividadFormSuccess.class));
            listenService();
            serviceDBActividades.agregarActividad(actividad);
        }
    }

    private void changeFragment(Fragment fragmento) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmento)
                .commit();
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad (Actividad actividad){
        this.actividad = actividad;
    }

    private void listenService() {
        serviceDBActividades = new FireDBActividades() {
            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Actividad actividad = dataSnapshot.getValue(Actividad.class);
                //Log.e("Actividad DEL SERVICIO", actividad.toString());
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    Log.e("valor", snap.getValue().toString());
                }



            }

            @Override
            public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("Actividad DEL SERVICIO", actividad.toString());
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
    }
}
