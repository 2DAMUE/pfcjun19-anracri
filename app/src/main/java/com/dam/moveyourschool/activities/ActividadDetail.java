package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.fragments.Actividad_Detail_Detalles_Frag;
import com.dam.moveyourschool.fragments.Actividad_Detail_General_Frag;
import com.dam.moveyourschool.fragments.Actividad_Detail_Localizacion_Frag;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.dam.moveyourschool.utils.Constantes;
import com.dam.moveyourschool.views.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ActividadDetail extends BaseActivity {
    private Actividad actividad;
    private ImageView imagenActividad;
    private Actividad_Detail_Localizacion_Frag fragmentoLocalizacion;
    private Actividad_Detail_Detalles_Frag fragmentoDetalles;
    private Actividad_Detail_General_Frag fragmentoGeneral;
    private TabLayout tabActividadDetail;
    private FireDBUsuarios serviceDBUsuarios;
    private Button btnReservar;

    //Atributo que en esta Vista corresponde a la empresa que provee servicios o actividades
    private Usuario userDestino;

    //Atributo que en esta Vista corresponde a la institucion educativa que quiere realizar Reservas
    private Usuario userOrigen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializamos los atributos de la clase
        imagenActividad = findViewById(R.id.imagenActividad);
        tabActividadDetail = findViewById(R.id.tabActivityDetail);
        btnReservar = findViewById(R.id.btnReservar);
        btnReservar.setVisibility(View.GONE);

        //Lanzamos el primer fragmento que se visualiza por defecto
        fragmentoGeneral = new Actividad_Detail_General_Frag();

        fragmentoDetalles = new Actividad_Detail_Detalles_Frag();
        fragmentoLocalizacion = new Actividad_Detail_Localizacion_Frag();

        //Obtenemos la actividad a mostrar en el fragment desde el intent
        actividad = (Actividad) getIntent().getSerializableExtra(getString(R.string.KEY_ACTIVIDAD));


        //Comprobamos si el usuario esta loggeado y lo guardamos en el atributo
        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            userOrigen = new Usuario();
            userOrigen.setUid(userAuth.getUid());
        }


        //Comprobamos si la actividad es null antes de lanzar el listener
        if (actividad != null) {

            serviceDBUsuarios = new FireDBUsuarios() {
                @Override
                public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    //Obtenemos la información del usuario propietario de la actividad (empresa)
                    if (dataSnapshot.getKey().equals(actividad.getUid_usuario())) {
                        userDestino = dataSnapshot.getValue(Usuario.class);
                    }

                    //Si el usuario esta loggeado obtenemos su informacion de la base de datos
                    if (userOrigen != null) {

                        if (dataSnapshot.getKey().equals(userOrigen.getUid())) {
                            userOrigen = dataSnapshot.getValue(Usuario.class);

                            //si el usuario que accede a la ventana es de tipo institucion educativa
                            //Mostramos el botón que permite realizar una solicitud via chat
                            if (userOrigen.getTipo().equals(Constantes.USUARIO_EDUCACION)) {
                                btnReservar.setVisibility(View.VISIBLE);
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
        }

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
        changeFragment(fragmentoGeneral);
    }

    public void loadDetalles() {
       changeFragment(fragmentoDetalles);
    }

    public void loadLocalizacion() {
        changeFragment(fragmentoLocalizacion);
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void solicitarPresupuesto(View view) {
        serviceDBUsuarios.desconectarListener();

        if (actividad != null && userDestino != null) {
            Intent i = new Intent(this, ChatWindow.class);
            i.putExtra(getString(R.string.KEY_USER), userDestino);
            startActivity(i);

        } else {
            new CustomDialog(this, R.string.VAL_SERVER_TIMEOUT).show();
        }


    }

    private void tabListener() {
        tabActividadDetail.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadGeneral();
                } else if (tab.getPosition() == 1) {
                    loadDetalles();
                } else if (tab.getPosition() == 2) {
                    loadLocalizacion();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.KEY_ACTIVIDAD), actividad);
        outState.putParcelable(getString(R.string.KEY_USER), userDestino);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userDestino = savedInstanceState.getParcelable(getString(R.string.KEY_USER));
        actividad = (Actividad) savedInstanceState.getSerializable(getString(R.string.KEY_ACTIVIDAD));
    }
}
