package com.dam.moveyourschool.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.adapters.AdapterChat;
import com.dam.moveyourschool.bean.Mensaje;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireBaseChatService;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;

public class ChatWindow extends BaseActivity {
    private RecyclerView recyclerView;
    private AdapterChat adapterChat;
    private LinearLayoutManager lm;
    private ArrayList<Mensaje> listaMensajes;
    private Usuario userOrigen;
    private Usuario userDestino;
    private EditText etxChatBox;
    private FireBaseChatService fireBaseChatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtenemos el usuario al que queremos enviar un mensaje
        userDestino = getIntent().getParcelableExtra(getString(R.string.KEY_USER));

        //Inicialización de los atributos de la clase
        etxChatBox = findViewById(R.id.etxChatBox);
        recyclerView = findViewById(R.id.reyclerview_message_list);

        listaMensajes = new ArrayList<>();


        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {

            new FireDBUsuarios() {
                @Override
                public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getKey().equals(userAuth.getUid())) {
                        userOrigen = dataSnapshot.getValue(Usuario.class);
                        inicializarServicio();
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
        } else {

        }
    }

    private void inicializarServicio() {
        adapterChat = new AdapterChat(listaMensajes, userOrigen);
        recyclerView.setAdapter(adapterChat);
        lm = new LinearLayoutManager(ChatWindow.this);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);
        fireBaseChatService = new FireBaseChatService(listaMensajes, userOrigen, userDestino, adapterChat, recyclerView);
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_chat_window;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }

    public void enviar(View view) {
        fireBaseChatService.enviarMensaje(etxChatBox.getText().toString());
        etxChatBox.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        fireBaseChatService.stopListener();
        Log.e("ENTRA ONPAUSE", "ENTRA");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fireBaseChatService.stopListener();
        Log.e("ENTRA ONPAUSE", "ENTRA");
    }
}
