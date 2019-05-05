package com.dam.moveyourschool.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.dam.moveyourschool.adapters.AdapterChat;
import com.dam.moveyourschool.bean.Mensaje;
import com.dam.moveyourschool.bean.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private SimpleDateFormat dateFormat;
    private RecyclerView recyclerView;

    public FireBaseChatService(ArrayList<Mensaje> listaMensajes, Usuario userOrigen, Usuario userDestino,
                               AdapterChat adapterChat, RecyclerView recyclerView) {
        this.listaMensajes = listaMensajes;
        this.userOrigen = userOrigen;
        this.userDestino = userDestino;
        this.adapterChat = adapterChat;
        this.recyclerView = recyclerView;
        dbr = FirebaseDatabase.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm");
        startListener();
    }

    private void startListener(){
        if (cel == null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key = dataSnapshot.getKey();
                    Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);

                    if (!mensaje.isLeido()) {
                        Log.e("MARCA A LEID", "MARCA A LEIDO");
                        mensaje.setLeido(true);
                        marcarLeido(key, mensaje);
                    }

                    listaMensajes.add(mensaje);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.scrollToPosition(listaMensajes.size() -1);
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
        if (cel != null) {
            dbr.getReference(NODO_USUARIO)
                    .child(userOrigen.getUid())
                    .child(NODO_MENSAJES)
                    .child(userDestino.getUid())
                    .removeEventListener(cel);
        }
    }

    public void enviarMensaje(String mensaje) {
        String hora = dateFormat.format(new Date());

        dbr.getReference(NODO_USUARIO)
                .child(userOrigen.getUid())
                .child(NODO_MENSAJES)
                .child(userDestino.getUid())
                .push()
                .setValue(new Mensaje(userOrigen, mensaje, hora, true));

        dbr.getReference(NODO_USUARIO)
                .child(userDestino.getUid())
                .child(NODO_MENSAJES)
                .child(userOrigen.getUid())
                .push()
                .setValue(new Mensaje(userOrigen, mensaje, hora, false));
    }

    private void marcarLeido(String key, Mensaje mensaje) {
        dbr.getReference(NODO_USUARIO)
                .child(userOrigen.getUid())
                .child(NODO_MENSAJES)
                .child(userDestino.getUid())
                .child(key)
                .setValue(mensaje);
    }
}
