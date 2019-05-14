package com.dam.moveyourschool.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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
import java.util.HashSet;

public class Actividades extends BaseActivity {
    private boolean primeraVez = true;
    private RecyclerView recyclerActividades;
    private LinearLayoutManager lm;
    private ArrayList<Actividad> listaActividades;
    private AdapterActividades adapterActividades;
    private FireDBActividades serviceDBActividades;
    private FireDBUsuarios serviceDBUsuarios;
    private FloatingActionButton fabAgregarActividad;
    private TabLayout tabActividades;
    private Usuario user;
    private boolean filtrando;
    private boolean userActividades;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userActividades = getIntent().getBooleanExtra(getString(R.string.KEY_USER_ACTIVIDADES), false);

        tabActividades = findViewById(R.id.tabActividades);
        tabActividades.setVisibility(View.GONE);
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

        //listaActividades.clone();

        //Inicializamos el recyclerview y su layout
        recyclerActividades = findViewById(R.id.recyclerActividades);
        lm = new LinearLayoutManager(this);

        //Unimos los componentes del Recycler e inicializamos el listener on click
        recyclerActividades.setLayoutManager(lm);
        recyclerActividades.setAdapter(adapterActividades);

        if (!userActividades) {
            tabActividades.setVisibility(View.VISIBLE);
        }

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

        //Cargamos el tab layout
        loadTab();
    }

    private void loadTab() {
        tabActividades.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    verDialogFilter();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    verDialogFilter();
                }
            }
        });
    }

    public void verDialogFilter() {
        final CharSequence[] lista;

        //Si esta filtrando mostramos el boton para cerrar el filtro
        if (filtrando) {
            lista = new CharSequence[1];
            lista[0] = AdapterActividades.TODAS_LAS_ACTIVIDADES;

        //Si no esta filtrando mostramos todos los filtros disponibles
        } else {
            //Generamos un Set para guardar las categorias sin duplicados
            HashSet<String> categorias = new HashSet<>();

            //Obtenemos las categorias disponibles en las actividades que haya cargadas
            for (int i = 0; i < listaActividades.size(); i++) {
                categorias.add(listaActividades.get(i).getCategoria());
            }

            //Traspasamos las categorias a un charsequence para poder hacerlo funcionar en un alert dialog
            lista = new CharSequence[categorias.size()];
            int pos = lista.length - 1;

            for (String categoria : categorias) {
                lista[pos] = categoria;
                pos--;
            }

            CharSequence aux = "";
            //Ordenamos las categorias del charsequence
            for (int i = 0; i < lista.length - 1; i++) {
                for (int j = i + 1; j < lista.length; j++) {

                    if (lista[i].toString().compareTo(lista[j].toString()) > 0) {
                        aux = lista[i];
                        lista[i] = lista[j];
                        lista[j] = aux;
                    }
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.TXT_ELEGIR_FILTRO));


        builder.setNeutralButton(getString(R.string.BTN_CANCEL), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setItems(lista, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapterActividades.filterByCategoria(lista[i].toString());

                if (filtrando) {
                    filtrando = false;
                } else {
                    filtrando = true;
                }
            }

        }).create().show();
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
            public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void nodoEliminado(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void nodoMovido(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void nodoCancelado(@NonNull DatabaseError databaseError) {}
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

    public void filterByName(String term) {
        if (listaActividades != null && adapterActividades != null) {
            adapterActividades.filtrarByName(term);
        }
    }

    private void loadDatabaseActividades() {
        serviceDBActividades = new FireDBActividades() {
            @Override
            public void getKey(String key) {}

            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (userActividades) {

                    if (dataSnapshot.getKey().equals(user.getUid())) {

                        for (DataSnapshot aux: dataSnapshot.getChildren()) {
                            Actividad actividad = aux.getValue(Actividad.class);
                            listaActividades.add(actividad);
                            adapterActividades.notifyItemInserted(listaActividades.size() - 1);
                        }
                    }

                } else {

                    for (DataSnapshot aux : dataSnapshot.getChildren()) {
                        Actividad actividad = aux.getValue(Actividad.class);
                        listaActividades.add(actividad);
                        adapterActividades.notifyItemInserted(listaActividades.size() - 1);

                    }
                }
            }

            @Override
            public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                for (DataSnapshot aux : dataSnapshot.getChildren()) {
                    if (!filtrando) {
                        Actividad actividad = aux.getValue(Actividad.class);

                        for (int i = 0; i < listaActividades.size(); i++) {
                            if (listaActividades.get(i).getUid_actividad().equals(actividad.getUid_actividad())) {
                                listaActividades.set(i, actividad);
                                adapterActividades.notifyItemChanged(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void nodoEliminado(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void nodoMovido(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void nodoCancelado(@NonNull DatabaseError databaseError) {}
        };
    }

    public void agregarActividad(View view) {
        startActivity(new Intent(this, ActividadForm.class));
    }

    @Override
    protected void onPause() {
        primeraVez = false;

        if (serviceDBActividades != null) {
            serviceDBActividades.desconectarListener();
        }

        if (serviceDBUsuarios != null){
            serviceDBUsuarios.desconectarListener();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {

        if (!primeraVez) {

            if (serviceDBActividades != null) {

                if (listaActividades != null) {
                    int size = listaActividades.size();
                    listaActividades.clear();
                    adapterActividades.notifyItemRangeRemoved(0, size);
                }

                serviceDBActividades.conectarListener();
            }

            if (serviceDBUsuarios != null) {
                serviceDBUsuarios.conectarListener();
            }
        }
        super.onResume();
    }
}
