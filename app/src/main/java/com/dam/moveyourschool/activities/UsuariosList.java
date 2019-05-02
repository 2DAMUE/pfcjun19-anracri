package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.adapters.AdapterUsuarios;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBMensajes;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;

public class UsuariosList extends BaseActivity {
    private RecyclerView recyclerUsuarios;
    private LinearLayoutManager lm;
    private AdapterUsuarios adapterUsuarios;
    private ArrayList<Usuario> listaUsuarios;
    private RequestManager glide;
    private FireDBUsuarios databaseServiceUsuarios;
    private FireDBMensajes serviceDBMensajes;
    private boolean filter;
    private ArrayList<String> keysChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicialización del ArrayList con los usuarios
        this.listaUsuarios = new ArrayList<>();

        //Inicialización del Request Manager de Glide para gestionar las imagenes del usuario
        glide = Glide.with(this);

        //Inicialización del RecyclerView y sus atributos y conectores
        lm = new LinearLayoutManager(this);
        adapterUsuarios = new AdapterUsuarios(listaUsuarios, glide);
        recyclerUsuarios = findViewById(R.id.recyclerView);
        recyclerUsuarios.setLayoutManager(lm);
        recyclerUsuarios.setAdapter(adapterUsuarios);
        recyclerUsuarios.setHasFixedSize(true);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                viewHolder.itemView.setTranslationX(dX / 5);
                //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        };

// attaching the touch helper to recycler view

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerUsuarios);

        //Inicialización del Listener del Recycler View
        startRecyclerListener();

        //Inicialización del servicio de la base de datos de usuarios
        loadDatabase();
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_usuarios_list;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }

    private void loadDatabase() {
        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        filter = getIntent().getBooleanExtra(getString(R.string.KEY_FILTER), false);


        //Si hay que filtrar porque solo hay que ver los chats del usuario
        if (filter) {

            //Obtenemos la lista de claves donde tienes mensajes el usuario
            serviceDBMensajes = new FireDBMensajes(userAuth.getUid()) {
                @Override
                public void callBackkeysChat() {

                    //Obtenemos una lista con los mensakes
                    keysChat = serviceDBMensajes.getKeysChat();

                    //Procedemos a comparar la lista de mensajes para mostrar los usuarios que se encuentren en la lista
                    databaseServiceUsuarios = new FireDBUsuarios() {
                        @Override
                        public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            if (userAuth != null) {
                                if (!userAuth.getUid().equals(dataSnapshot.getKey())) {

                                    Usuario aux = dataSnapshot.getValue(Usuario.class);

                                    if (aux.getNombre() != null && aux.getTitular() != null) {

                                        //Inicializamos la variable que nos confirma si la clave esta disponible
                                        boolean keyAvailable = false;


                                        //Si la clave esta disponible modificamos el flag
                                        for (String key: keysChat) {
                                            if (key.equals(aux.getUid())) {
                                                keyAvailable = true;
                                            }
                                        }

                                        //Si el flag confirma la clave añadimos el usuario
                                        if(keyAvailable) {
                                            listaUsuarios.add(dataSnapshot.getValue(Usuario.class));
                                            adapterUsuarios.notifyItemInserted(listaUsuarios.size() - 1);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            if (userAuth != null) {
                                if (!userAuth.getUid().equals(dataSnapshot.getKey())) {

                                    Usuario aux = dataSnapshot.getValue(Usuario.class);

                                    if (aux.getNombre() != null && aux.getTitular() != null) {

                                        //Inicializamos la variable que nos confirma si la clave esta disponible
                                        boolean keyAvailable = false;


                                        //Si la clave esta disponible modificamos el flag
                                        for (String key: keysChat) {
                                            if (key.equals(aux.getUid())) {
                                                keyAvailable = true;
                                            }
                                        }

                                        //Si el flag confirma la clave añadimos el usuario
                                        if(keyAvailable) {
                                            listaUsuarios.add(dataSnapshot.getValue(Usuario.class));
                                            adapterUsuarios.notifyItemInserted(listaUsuarios.size() - 1);
                                        }
                                    }
                                }
                            }

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
            };

            //si hay que mostrar todos los usuarios
        } else {

            databaseServiceUsuarios = new FireDBUsuarios() {
                @Override
                public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (userAuth != null) {
                        if (!userAuth.getUid().equals(dataSnapshot.getKey())) {

                            Usuario aux = dataSnapshot.getValue(Usuario.class);

                            if (aux.getNombre() != null && aux.getTitular() != null) {
                                listaUsuarios.add(dataSnapshot.getValue(Usuario.class));
                                adapterUsuarios.notifyItemInserted(listaUsuarios.size() - 1);
                            }
                        }
                    }
                }

                @Override
                public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (userAuth != null) {
                        if (!userAuth.getUid().equals(dataSnapshot.getKey())) {

                            Usuario aux = dataSnapshot.getValue(Usuario.class);

                            if (aux.getNombre() != null && aux.getTitular() != null) {
                                listaUsuarios.add(dataSnapshot.getValue(Usuario.class));
                                adapterUsuarios.notifyItemInserted(listaUsuarios.size() - 1);
                            }
                        }
                    }

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
    }

    private void startRecyclerListener() {
        adapterUsuarios.setonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = recyclerUsuarios.getChildAdapterPosition(view);

                //Si hay filtro enviamos a la ventana de chat y sino a los detalles del usuario
                if (filter) {
                    startActivity(new Intent(UsuariosList.this, ChatWindow.class).putExtra(getString(R.string.KEY_USER), listaUsuarios.get(index)));

                } else {
                    startActivity(new Intent(UsuariosList.this, UserDetail.class)
                            .putExtra(getString(R.string.KEY_USER), listaUsuarios.get(index)));
                }
            }
        });
    }
}
