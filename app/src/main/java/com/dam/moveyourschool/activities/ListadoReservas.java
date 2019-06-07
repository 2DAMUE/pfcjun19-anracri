package com.dam.moveyourschool.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dam.moveyourschool.R;
import com.dam.moveyourschool.adapters.AdapterReservas;
import com.dam.moveyourschool.bean.Reserva;
import com.dam.moveyourschool.bean.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListadoReservas extends BaseActivity {

    private DatabaseReference dbR;
    private DatabaseReference dbRUsers;
    private ChildEventListener cel;
    private ChildEventListener celUsers;

    AdapterReservas adapter;

    RecyclerView recicler;
    LinearLayoutManager miLayoutManager;
    ArrayList<Reserva> lista = new ArrayList<>();
    ArrayList<Reserva> listaCompleta = new ArrayList<>();

    ArrayList<Reserva> listaFiltrada = new ArrayList<>();

    ArrayList<Usuario> listaUsers = new ArrayList<>();

    private TabLayout alertsTabs;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbR = FirebaseDatabase.getInstance().getReference().child("reservas");
        dbRUsers = FirebaseDatabase.getInstance().getReference().child("usuario");


        user  = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            recicler = findViewById(R.id.recyclerViewReservas);
            recicler.setHasFixedSize(true);

            miLayoutManager = new LinearLayoutManager(this);
            adapter = new AdapterReservas(lista, this);

            recicler.setAdapter(adapter);
            recicler.setLayoutManager(miLayoutManager);
            recicler.setItemAnimator(new DefaultItemAnimator());

            adapter.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Reserva res = lista.get(recicler.getChildAdapterPosition(v));
                    Intent i = null;

                    if(localizarTipoUsuario()){
                        i = new Intent(ListadoReservas.this,Reserva_info_empresa.class);
                    }else{
                        i = new Intent(ListadoReservas.this,activity_reserva_info.class);
                    }

                    i.putExtra(getString(R.string.KEY_RESERVA_INFO),res);

                    startActivity(i);
                }
            });

            alertsTabs = findViewById(R.id.alertasTabs);
            alertsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Log.e("TAB", tab.getText().toString());

                    if (tab.getText().equals(getString(R.string.tab_filtrar))) {
                        verDialogFiltrar();
                    } else if(tab.getText().equals(getString(R.string.tab_ordenar))) {
                        verDialogOrdenar();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                    if (tab.getText().equals(getString(R.string.tab_filtrar))) {
                        verDialogFiltrar();
                    } else if(tab.getText().equals(getString(R.string.tab_ordenar))) {
                        verDialogOrdenar();
                    }
                }
            });


            addChildEvent();
            addChildEventUsers();

        } else {
            startActivity(new Intent(this, Actividades.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }


    }

    private boolean localizarTipoUsuario() {

        boolean empresa = false;
        boolean continuar = true;

        for (int i = 0; i< listaUsers.size() && continuar;i++){

            if (user.getUid().equals(listaUsers.get(i).getUid())){

                if (listaUsers.get(i).getTipo().equals("EMPRESA")){
                    empresa = true;
                    continuar = false;
                }

            }

        }

        return empresa;
    }


    private void addChildEvent() {
        if(cel == null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Reserva res = dataSnapshot.getValue(Reserva.class);

                    if(user.getUid().equals(res.getIdCliente()) || user.getUid().equals(res.getIdEmpresa())){
                        lista.add(res);
                        listaCompleta.add(res);
                        adapter.notifyItemInserted(lista.size() - 1);
                    }



                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Reserva res = dataSnapshot.getValue(Reserva.class);

                    for (int i = 0; i<lista.size();i++){

                        if(lista.get(i).getIdReserva().equals(res.getIdReserva())){
                            lista.set(i,res);
                            listaCompleta.set(i,res);
                        }
                    }

                    adapter.notifyDataSetChanged();


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dbR.addChildEventListener(cel);
        }
    }



    private void addChildEventUsers() {
        if(celUsers == null) {
            celUsers = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Usuario us = dataSnapshot.getValue(Usuario.class);

                    listaUsers.add(us);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dbRUsers.addChildEventListener(celUsers);
        }
    }






    public void verDialogOrdenar() {
        final CharSequence[] lista = {"Ordenar de la A a la Z", "Ordenar de la Z a la A"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_opcion));

        builder.setItems(lista, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if ( i == 0) {
                    adapter.ordenarAZ();
                } else {
                    adapter.ordenarZA();
                }
            }
        });

        builder.setNeutralButton(getString(R.string.dialog_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();
    }

    public void verDialogFiltrar(){

        final CharSequence[] lista = {"Aceptadas", "Rechazadas","Pendientes","Ver Todas"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_opcion));

        builder.setItems(lista, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                filtrarReservas(lista[i].toString());

            }
        });

        builder.setNeutralButton(getString(R.string.dialog_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();
    }

    public void filtrarReservas(String id){

        if(id.equals("Ver Todas")){

            adapter.eliminarTodasLasReservas();
            adapter.agregarReservas(listaCompleta);

        }else if(id.equals("Aceptadas")){
            aplicarFiltro("ACEPTADA");

        }else if(id.equals("Rechazadas")){
            aplicarFiltro("RECHAZADA");;

        }else{
            aplicarFiltro("PENDIENTE");
        }


    }

    private void aplicarFiltro(String tipo){
        listaFiltrada.clear();
        for (int i = 0; i< listaCompleta.size();i++){

            if (listaCompleta.get(i).getEstado().equals(tipo)){
                listaFiltrada.add(listaCompleta.get(i));
            }
        }
        adapter.eliminarTodasLasReservas();
        adapter.agregarReservas(listaFiltrada);

    }


    @Override
    public int cargarLayout() {
        return R.layout.activity_listado_reservas;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }
}
