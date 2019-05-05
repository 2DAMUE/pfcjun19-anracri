package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.HashMap;
import java.util.Map;

public class UsuariosList extends BaseActivity {
    private RecyclerView recyclerUsuarios;
    private LinearLayoutManager lm;
    private AdapterUsuarios adapterUsuarios;
    private ArrayList<Usuario> listaUsuarios;
    private RequestManager glide;
    private FireDBUsuarios databaseServiceUsuarios;
    private FireDBMensajes serviceDBMensajes;
    private boolean filter;
    private HashMap<String, Boolean> keysChatMap;
    private boolean primeraVez = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        filter = getIntent().getBooleanExtra(getString(R.string.KEY_FILTER), false);

        //Inicialización del ArrayList con los usuarios
        this.listaUsuarios = new ArrayList<>();

        //Inicialización del Request Manager de Glide para gestionar las imagenes del usuario
        glide = Glide.with(this);

        //Inicialización del RecyclerView y sus atributos y conectores
        lm = new LinearLayoutManager(this);

        //Inicializamos el adaptador con el booleano que permite mostrar o no el swipe
        adapterUsuarios = new AdapterUsuarios(listaUsuarios, glide, filter);

        //Inicializamos el recyclerview y le agregamos los componentes que le dan funcionalidad
        recyclerUsuarios = findViewById(R.id.recyclerView);
        recyclerUsuarios.setLayoutManager(lm);
        recyclerUsuarios.setAdapter(adapterUsuarios);
        recyclerUsuarios.setHasFixedSize(true);

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

        //Si hay que filtrar porque solo hay que ver los chats del usuario
        if (filter) {

            //Obtenemos la lista de claves donde tienes mensajes el usuario
            if (userAuth != null){
                serviceDBMensajes = new FireDBMensajes(userAuth.getUid()) {
                    @Override
                    public void callBackkeysChat() {

                        //Obtenemos una lista con los mensakes
                        keysChatMap = serviceDBMensajes.getKeysMap();

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

                                            //Inicializamos una variable que nos dirá si el usuario ha leido los mensajes
                                            boolean claveTemporal = false;


                                            for (Map.Entry<String, Boolean> claveUsuario : keysChatMap.entrySet()) {
                                                if (claveUsuario.getKey().equals(aux.getUid())) {
                                                    keyAvailable = true;
                                                    claveTemporal = claveUsuario.getValue();
                                                }
                                            }

                                            //Si el flag confirma la clave añadimos el usuario
                                            if(keyAvailable) {
                                                Usuario userAdd = dataSnapshot.getValue(Usuario.class);
                                                userAdd.setLeido(claveTemporal);
                                                listaUsuarios.add(userAdd);
                                                adapterUsuarios.notifyItemInserted(listaUsuarios.size() - 1);
                                            }
                                        }
                                    }
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
                    }

                    @Override
                    public void callBackDeleteChat() {
                        View view = findViewById(R.id.rlUsuariosList);
                        Snackbar.make(view, getString(R.string.CHAT_ELIMINADO), Snackbar.LENGTH_LONG).show();
                        recreate();
                    }

                    @Override
                    public void callBackModificado() {
                        recreate();
                    }
                };
            }

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
                    //adapterUsuarios.notifyDataSetChanged();
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

                /*
                Recibimos la posicion desde la clase del adaptador porque el swipe no permite
                que invoquemos el metodo del recycler que nos indica esta. Por tanto la enviamos
                a través de un Tag
                 */
                int posicion = (int) view.getTag();

                //Si hay filtro enviamos a la ventana de chat y sino a los detalles del usuario
                if (filter) {
                    /*
                    Si el identificador del elemento de la vista corresponde al boton eliminar
                    procedemos a eliminar el chat seleccionado en la base de datos del usuario
                     */
                    if (view.getId() == R.id.btnEliminar) {
                        eliminarChat(posicion);

                        //Sino lanzamos la ventana de chat para mostrarla
                    } else {
                        startActivity(new Intent(UsuariosList.this, ChatWindow.class)
                                .putExtra(getString(R.string.KEY_USER), listaUsuarios.get(posicion)));
                    }

                } else {
                    startActivity(new Intent(UsuariosList.this, UserDetail.class)
                            .putExtra(getString(R.string.KEY_USER), listaUsuarios.get(posicion)));
                }
            }
        });
    }

    private void eliminarChat(int posUsuarioBorrar) {
        serviceDBMensajes.eliminarMensajesConUnUsuario(listaUsuarios.get(posUsuarioBorrar).getUid());
        listaUsuarios.remove(posUsuarioBorrar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (filter) {
            serviceDBMensajes.desconectarListener();
        }

        if (listaUsuarios != null) {
            listaUsuarios.clear();
            adapterUsuarios.notifyDataSetChanged();
        }
        databaseServiceUsuarios.desconectarListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (filter) {
            if (serviceDBMensajes != null && databaseServiceUsuarios != null) {
                recreate();
            }
        }

        if (primeraVez) {
            primeraVez = false;

        } else {
            databaseServiceUsuarios.conectarListener();
        }


    }
}
