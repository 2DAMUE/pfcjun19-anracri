package com.dam.moveyourschool.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dam.moveyourschool.bean.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class FireDBUsuarios {
    private ChildEventListener cel;
    private DatabaseReference dbr;
    private static final String NODO_USUARIOS = "usuario";

    public FireDBUsuarios() {

        dbr = FirebaseDatabase.getInstance().getReference().child(NODO_USUARIOS);
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

    //Agrega un usuario nuevo en un nodo con su identificador
    public void agregarUsuarioById(Usuario usuario) {
        dbr.child(usuario.getUid()).setValue(usuario);
    }

    public abstract void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s);
    public abstract void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s);
    public abstract void nodoEliminado(@NonNull DataSnapshot dataSnapshot);
    public abstract void nodoMovido(@NonNull DataSnapshot dataSnapshot, @Nullable String s);
    public abstract void nodoCancelado(@NonNull DatabaseError databaseError);

}
