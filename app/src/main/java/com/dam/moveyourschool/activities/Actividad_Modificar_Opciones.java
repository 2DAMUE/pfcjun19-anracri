package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.services.FireDBActividades;
import com.dam.moveyourschool.utils.Constantes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class Actividad_Modificar_Opciones extends BaseActivity {
    private Actividad actividadModif;
    private FireDBActividades serviceDBActividades;
    private TextView tvLock;
    private Button btnLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvLock = findViewById(R.id.tvLock);
        btnLock = findViewById(R.id.btnLock);

        actividadModif = (Actividad) getIntent().getSerializableExtra(getString(R.string.KEY_ACTIVIDAD));

        if (actividadModif.isDisponible()) {
            tvLock.setText(getString(R.string.VAL_TEXT_LOCKED));

        } else {
            tvLock.setText(getString(R.string.VAL_TEXT_UNLOCKED));
        }
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_actividad__modificar__opciones;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }

    public void modificar(View view) {
        Intent i = new Intent(this, ActividadForm.class);
        Log.e("TAG", view.getTag().toString());

        if (view.getTag().equals(Constantes.fragmentCategoria)) {
            i.putExtra(getString(R.string.KEY_SELECTED_FRAGMENT), Constantes.fragmentCategoria);
            i.putExtra(getString(R.string.KEY_MOD_ACTIVITY), true);
            i.putExtra(getString(R.string.KEY_ACTIVIDAD), actividadModif);
        }

        if (view.getTag().equals(Constantes.fragmentGeneral)) {
            i.putExtra(getString(R.string.KEY_SELECTED_FRAGMENT), Constantes.fragmentGeneral);
            i.putExtra(getString(R.string.KEY_MOD_ACTIVITY), true);
            i.putExtra(getString(R.string.KEY_ACTIVIDAD), actividadModif);
        }

        if (view.getTag().equals(Constantes.fragmentLocalizacion)) {
            i.putExtra(getString(R.string.KEY_SELECTED_FRAGMENT), Constantes.fragmentLocalizacion);
            i.putExtra(getString(R.string.KEY_MOD_ACTIVITY), true);
            i.putExtra(getString(R.string.KEY_ACTIVIDAD), actividadModif);
        }

        if (view.getTag().equals(Constantes.fragmentDetalles)) {
            i.putExtra(getString(R.string.KEY_SELECTED_FRAGMENT), Constantes.fragmentDetalles);
            i.putExtra(getString(R.string.KEY_MOD_ACTIVITY), true);
            i.putExtra(getString(R.string.KEY_ACTIVIDAD), actividadModif);
        }

        startActivity(i);
    }

    @Override
    protected void onResume() {
        serviceDBActividades = new FireDBActividades() {
            @Override
            public void getKey(String key) {

            }

            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (actividadModif != null){
                    for (DataSnapshot aux : dataSnapshot.getChildren()) {
                        if (aux.getKey().equals(actividadModif.getUid_actividad())) {
                            actividadModif = aux.getValue(Actividad.class);
                            serviceDBActividades.desconectarListener();
                        }
                    }
                } else {
                    serviceDBActividades.desconectarListener();
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

        super.onResume();
    }
}
