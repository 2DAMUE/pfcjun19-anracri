package com.dam.moveyourschool.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.fragments.Actividad_Detail_General_Frag;

public class ActividadDetail extends BaseActivity {
    private Actividad actividad;
    private ImageView imagenActividad;
    private Actividad_Detail_General_Frag fragmentoGeneral;
    private TabLayout tabActividadDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagenActividad = findViewById(R.id.imagenActividad);
        tabActividadDetail = findViewById(R.id.tabActivityDetail);

        fragmentoGeneral = new Actividad_Detail_General_Frag();

        actividad = (Actividad) getIntent().getSerializableExtra(getString(R.string.KEY_ACTIVIDAD));

        //Inicializamos el listener del tab
        tabListener();

        //Cargamos la imagen de la actividad
        loadImagen();

        //Cargamos el primer fragmento del activity
        loadGeneral();
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_actividad_detail;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }

    private void loadImagen() {
        if (actividad != null) {

            if (actividad.getUrlFoto() != null && !actividad.getUrlFoto().equals("")) {
                Glide.with(this).load(actividad.getUrlFoto()).into(imagenActividad);
            }
        }
    }

    private void changeFragment(Fragment fragmento) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detailContainer, fragmento)
                .commit();
    }

    public void loadGeneral() {
        Log.e("LOAD GENERAL", "LOAD GENERAL");
        changeFragment(fragmentoGeneral);
    }

    public void loadDetalles() {
        Log.e("LOAD DETALLES", "LOAD DETALLES");
    }

    public void loadLocalizacion() {
        Log.e("LOAD LOCALIZACION", "LOAD LOCALIZACION");
    }

    public Actividad getActividad() {
        return actividad;
    }

    private void tabListener() {
        tabActividadDetail.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadGeneral();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
