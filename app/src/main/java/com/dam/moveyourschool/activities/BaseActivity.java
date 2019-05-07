package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireBaseAuthentication;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.dam.moveyourschool.views.CustomDialog;
import com.dam.moveyourschool.views.DialogLogin;
import com.dam.moveyourschool.views.ProgressBarAlert;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String EMPRESA = "EMPRESA";
    private static final String INSTITUCION_EDUCATIVA = "EDUCACION";
    private CircleImageView imgMenuImgUser;
    private TextView tvMenuInstitucion;
    private NavigationView navigationView;
    private Usuario user;
    private FireBaseAuthentication fireBaseAuthentication;
    private DialogLogin dialogLogin;
    private ProgressBarAlert progressBarAlert;
    private FireDBUsuarios serviceDBUsuarios;
    protected boolean showCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        showCart = false;

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Inicialización de los atributos del Nav Header
        final View hView =  navigationView.getHeaderView(0);
        imgMenuImgUser = hView.findViewById(R.id.imgMenuUsuario);
        tvMenuInstitucion = hView.findViewById(R.id.tvMenuInstitucion);

        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);

            serviceDBUsuarios = new FireDBUsuarios() {
                @Override
                public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getKey().equals(userAuth.getUid())) {
                        user = dataSnapshot.getValue(Usuario.class);

                        if (user.getUrlFoto() != null && !user.getUrlFoto().equals("")) {
                            Glide.with(BaseActivity.this).load(user.getUrlFoto()).into(imgMenuImgUser);
                        }

                        if (user.getNombre() != null && !user.getNombre().equals("")) {
                            tvMenuInstitucion.setText(user.getNombre());
                        }

                        serviceDBUsuarios.desconectarListener();
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
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Carga el Drawer
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Decide si estamos en modo Menu o Modo Back en función del parámetro que recibe el método
        //desde la implementación en el activity
        toggle.setDrawerIndicatorEnabled(setDrawer());
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Añadimos un listener para que cuando se haga click en el boton back de navegación
        //invoquemos el metodo onBackpressed que nos devolvera al activity anterior.
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        View container = findViewById(R.id.contenedorMain);
        ConstraintLayout rel = (ConstraintLayout) container;
        getLayoutInflater().inflate(cargarLayout(), rel);

    }

    //Metodo abstracto que utilizamos desde la clase que extienda para cargar el layout.
    public abstract int cargarLayout();

    //Metodo abstracto para establecer la modalidad del activity (menu o back)
    public abstract boolean setDrawer();

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            menu.findItem(R.id.menu_Perfil).setVisible(false);
        }

        if (showCart) {
            menu.findItem(R.id.icoCart).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.isVisible()) {
            if (id == R.id.menu_Perfil) {
                startActivity(new Intent(this, UserDetail.class));
                return true;
            }

            if (id == R.id.icoCart) {
                String idU = ((ChatWindow) (this)).obtenerUsuarioDestino();
                Intent i = new Intent(this,NuevaReserva.class);
                i.putExtra("KEY_ID_USU", idU);
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (user != null && user.getTipo().equals(EMPRESA)) {



        } else if (user != null  && user.getTipo().equals(INSTITUCION_EDUCATIVA)) {



        } else {

        }


        if (id == R.id.nav_Usuarios) {
            startActivity(new Intent(this, UsuariosList.class));
        } else if (id == R.id.nav_MisChats) {
            startActivity(new Intent(this, UsuariosList.class).putExtra(getString(R.string.KEY_FILTER), true));

        } else if (id == R.id.nav_MisReservas) {

        } else if (id == R.id.navActividades) {
            startActivity(new Intent(this, Actividades.class));

        } else if (id == R.id.nav_login) {
            loginView();
            dialogLogin.show();

        } else if (id == R.id.nav_logout) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                recreate();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loginView() {
        progressBarAlert = new ProgressBarAlert(this);

        dialogLogin = new DialogLogin(this);
        dialogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!dialogLogin.comprobarCamposVacios()) {
                    progressBarAlert.show();
                    fireBaseAuthentication.login(dialogLogin.getEtxLoginNombre().getText().toString(), dialogLogin.getEtxLoginPassword().getText().toString());
                } else {
                    new CustomDialog(BaseActivity.this, R.string.TXT_DEBE_RELLENAR_CORRECTAMENTE).show();
                }
            }
        });

        fireBaseAuthentication = new FireBaseAuthentication() {
            @Override
            public void callBackSignUp(int result) {

            }

            @Override
            public void callBackLogin(int result) {
                progressBarAlert.cancel();

                if (result == 0 && FirebaseAuth.getInstance().getCurrentUser() != null) {
                    dialogLogin.cancel();
                    recreate();

                } else if (result == 1) {
                    new CustomDialog(BaseActivity.this, R.string.VAL_USER_INEXISTENT).show();

                } else if (result == 2) {
                    new CustomDialog(BaseActivity.this, R.string.VAL_INVALID_PASSWORD).show();

                } else if (result == 3) {
                    new CustomDialog(BaseActivity.this, R.string.VAL_SERVER_TIMEOUT).show();

                }
            }
        };
    }
}
