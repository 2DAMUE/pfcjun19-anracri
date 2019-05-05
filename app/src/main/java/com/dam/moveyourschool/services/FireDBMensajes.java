package com.dam.moveyourschool.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.dam.moveyourschool.bean.Mensaje;
import com.dam.moveyourschool.bean.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class FireDBMensajes {
    private ChildEventListener cel;
    private DatabaseReference dbr;
    private static final String NODO_USUARIOS = "usuario";
    private static final String NODO_MENSAJES = "mensajes";
    private ArrayList<String> keysChat;
    private HashMap<String, Boolean> keysChatAux;

    public FireDBMensajes(String uid) {
        keysChat = new ArrayList<>();
        keysChatAux = new HashMap<>();

        dbr = FirebaseDatabase.getInstance().getReference().child(NODO_USUARIOS).child(uid)
                .child(NODO_MENSAJES);
        if (cel==null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.e("MENSAJE AGREGADO", "AGREGADO");
                    boolean  checkLeido = true;
                    keysChat.add(dataSnapshot.getKey());

                    //Vamos a comprobar
                    for (DataSnapshot aux : dataSnapshot.getChildren()) {
                        checkLeido = true;
                        Mensaje mensajeAux = aux.getValue(Mensaje.class);

                        if (!mensajeAux.isLeido()) {
                            checkLeido = false;
                        }
                    }

                    keysChatAux.put(dataSnapshot.getKey(), checkLeido);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    callBackModificado();
                }

                //Si se elimina un chat lo detectamos a través del listener y borramos la clave del array
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    //keysChatAux.remove(dataSnapshot.getKey());
                    callBackDeleteChat();

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

    public void eliminarMensajesConUnUsuario(String uidUsuarioBorrar) {
        dbr.child(uidUsuarioBorrar).removeValue();
    }

    public void desconectarListener() {
        if (cel != null) {
            dbr.removeEventListener(cel);
        }
    }

    public void conectarListener() {
        if (cel != null) {
            dbr.addChildEventListener(cel);
        }
    }

    public abstract void callBackkeysChat();
    public abstract void callBackDeleteChat();
    public abstract void callBackModificado();

    public ArrayList<String> getKeysChat() {
        return keysChat;
    }

    public HashMap<String, Boolean> getKeysMap() {
        return keysChatAux;
    }
}
