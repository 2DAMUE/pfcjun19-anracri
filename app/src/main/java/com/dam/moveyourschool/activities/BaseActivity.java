package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String EMPRESA = "EMPRESA";
    private static final String INSTITUCION_EDUCATIVA = "EDUCACION";
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            new FireDBUsuarios() {
                @Override
                public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getKey().equals(userAuth.getUid())) {
                        user = dataSnapshot.getValue(Usuario.class);
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_movehoriz_large);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

         */

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


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            item.setVisible(false);
        }

        if (item.isVisible()) {
            if (id == R.id.menu_Perfil) {
                startActivity(new Intent(this, UserDetail.class));
                return true;

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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void logOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            user = null;
        }
    }
}
