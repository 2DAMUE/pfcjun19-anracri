package com.dam.moveyourschool.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dam.moveyourschool.bean.Actividad;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class FireDBActividades {
    private ChildEventListener cel;
    private DatabaseReference dbr;
    private static final String NODO_ACTIVIDADES = "actividades";

    public FireDBActividades() {

        dbr = FirebaseDatabase.getInstance().getReference().child(NODO_ACTIVIDADES);
        if (cel==null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    nodoAgregado(dataSnapshot, s);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    nodoModificado(dataSnapshot, s);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    nodoEliminado(dataSnapshot);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    nodoMovido(dataSnapshot, s);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    nodoCancelado(databaseError);
                }
            };
            dbr.addChildEventListener(cel);
        }
    }

    public void desconectarListener() {
        if (cel != null) {
            dbr.removeEventListener(cel);
        }
    }

    public void conectarListener() {
        if (cel == null) {
            dbr.addChildEventListener(cel);
        }
    }

    //Agrega una actividad nueva a la database
    public void agregarActividad(Actividad actividad) {

        String key = dbr.child(actividad.getUid_usuario()).push().getKey();
        dbr.child(actividad.getUid_usuario()).child(key).setValue(actividad);
    }

    public abstract void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s);
    public abstract void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s);
    public abstract void nodoEliminado(@NonNull DataSnapshot dataSnapshot);
    public abstract void nodoMovido(@NonNull DataSnapshot dataSnapshot, @Nullable String s);
    public abstract void nodoCancelado(@NonNull DatabaseError databaseError);
}
