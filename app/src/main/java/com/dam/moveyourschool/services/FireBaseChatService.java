package com.dam.moveyourschool.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dam.moveyourschool.adapters.AdapterChat;
import com.dam.moveyourschool.bean.Mensaje;
import com.dam.moveyourschool.bean.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class FireBaseChatService {
    //Constantes de la clase
    private static final String NODO_USUARIO = "usuario";
    private static final String NODO_MENSAJES = "mensajes";

    //Atributos de la Clase
    private ArrayList<Mensaje> listaMensajes;
    private Usuario userOrigen;
    private Usuario userDestino;
    private FirebaseDatabase dbr;
    private ChildEventListener cel;
    private AdapterChat adapterChat;

    public FireBaseChatService(ArrayList<Mensaje> listaMensajes, Usuario userOrigen, Usuario userDestino, AdapterChat adapterChat) {
        this.listaMensajes = listaMensajes;
        this.userOrigen = userOrigen;
        this.userDestino = userDestino;
        this.adapterChat = adapterChat;
        dbr = FirebaseDatabase.getInstance();
        startListener();
    }

    private void startListener(){
        if (cel == null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listaMensajes.add(dataSnapshot.getValue(Mensaje.class));
                    adapterChat.notifyDataSetChanged();
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
            dbr.getReference(NODO_USUARIO)
                    .child(userOrigen.getUid())
                    .child(NODO_MENSAJES)
                    .child(userDestino.getUid())
                    .addChildEventListener(cel);
        }
    }

    public void stopListener() {
        dbr.getReference().removeEventListener(cel);
    }

    public void enviarMensaje(String mensaje) {
        dbr.getReference(NODO_USUARIO)
                .child(userOrigen.getUid())
                .child(NODO_MENSAJES)
                .child(userDestino.getUid())
                .push()
                .setValue(new Mensaje(userOrigen, mensaje));

        dbr.getReference(NODO_USUARIO)
                .child(userDestino.getUid())
                .child(NODO_MENSAJES)
                .child(userOrigen.getUid())
                .push()
                .setValue(new Mensaje(userOrigen, mensaje));
    }
}
