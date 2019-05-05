package com.dam.moveyourschool.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.adapters.AdapterActividades;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBActividades;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class Actividades extends BaseActivity {
    private RecyclerView recyclerActividades;
    private LinearLayoutManager lm;
    private ArrayList<Actividad> listaActividades;
    private AdapterActividades adapterActividades;
    private FireDBActividades serviceDBActividades;
    private FireDBUsuarios serviceDBUsuarios;
    private FloatingActionButton fabAgregarActividad;
    private Usuario user;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fabAgregarActividad = findViewById(R.id.fabAgregar);
        fabAgregarActividad.setVisibility(View.INVISIBLE);


        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            user = new Usuario();
            user.setUid(userAuth.getUid());
            loadDatabaseUsuarios();
        }

        //Inicializamos el Array de Datos del adaptador y el adaptador
        listaActividades = new ArrayList<>();
        adapterActividades = new AdapterActividades(listaActividades, Glide.with(this));

        //Inicializamos el recyclerview y su layout
        recyclerActividades = findViewById(R.id.recyclerActividades);
        lm = new LinearLayoutManager(this);

        //Unimos los componentes del Recycler e inicializamos el listener on click
        recyclerActividades.setLayoutManager(lm);
        recyclerActividades.setAdapter(adapterActividades);

        adapterActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Actividades.this, ActividadDetail.class);
                i.putExtra(getString(R.string.KEY_ACTIVIDAD), listaActividades.get(recyclerActividades.getChildAdapterPosition(view)));
                startActivity(i);
            }
        });

        //Ponemos la Firebase RDB a la escucha
        loadDatabaseActividades();
    }

    private void loadDatabaseUsuarios() {
        serviceDBUsuarios = new FireDBUsuarios() {
            @SuppressLint("RestrictedApi")
            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Usuario userAux = dataSnapshot.getValue(Usuario.class);

                if (userAux.getUid().equals(user.getUid())) {
                    fabAgregarActividad.setVisibility(View.VISIBLE);
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

    @Override
    public int cargarLayout() {
        return R.layout.activity_actividades;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }

    private void loadDatabaseActividades() {
        serviceDBActividades = new FireDBActividades() {
            @Override
            public void getKey(String key) {

            }

            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot aux : dataSnapshot.getChildren()) {
                    Actividad actividad = aux.getValue(Actividad.class);
                    listaActividades.add(actividad);
                    adapterActividades.notifyItemInserted(listaActividades.size() - 1);
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

    public void agregarActividad(View view) {
        startActivity(new Intent(this, ActividadForm.class));
    }
}
