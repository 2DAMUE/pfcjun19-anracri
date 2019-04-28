package com.dam.moveyourschool.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public abstract class FireDBMensajes {
    private ChildEventListener cel;
    private DatabaseReference dbr;
    private static final String NODO_USUARIOS = "usuario";
    private static final String NODO_MENSAJES = "mensajes";
    private ArrayList<String> keysChat;

    public FireDBMensajes(String uid) {
        keysChat = new ArrayList<>();

        dbr = FirebaseDatabase.getInstance().getReference().child(NODO_USUARIOS).child(uid)
                .child(NODO_MENSAJES);
        if (cel==null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    keysChat.add(dataSnapshot.getKey());
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
            dbr.addChildEventListener(cel);
        }

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callBackkeysChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public abstract void callBackkeysChat();

    public ArrayList<String> getKeysChat() {
        return keysChat;
    }
}
