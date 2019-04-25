package com.dam.moveyourschool.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.services.FireBaseAuthentication;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class SignUpStep2 extends AppCompatActivity {
    private FireBaseAuthentication fireBaseAuthentication;
    private FireDBUsuarios serviceDBUsuarios;
    private TextView etxEmail;
    private TextView etxPassword;
    private boolean matchPattern = false;
    private Dialog alert;
    private int tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);

        //Obtiene la clave del intent del activity antecesor
        tipo = getIntent().getIntExtra(getString(R.string.KEY_TYPE), 1);

        //Inicializa el servicio de la base de datos de usuarios
        loadDatabaseUsuarios();

        //Inicialización de los atributos
        etxEmail = findViewById(R.id.etxEmail);
        etxPassword = findViewById(R.id.etxPassword);

        //Inicialización del Dialogo de alerta con el progressbar
        alert = new Dialog(this, R.style.Theme_AppCompat_Dialog);
        alert.setContentView(R.layout.progressbar);

        // Inicialización del servicio de autenticación
        // Listener que proporciona el resultado del intento de alta de usuario
        loadAuthenticateService();

        //Modifica de manera dinámica el campo de texto del correo electrónico
        dynamicTextListener();
    }

    //Metodo que registra un usuario en el Auth si se cumplen las reglas y genera el nodo en la database
    public void registrar(View view) {
        if (checkFields()){
            alert.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fireBaseAuthentication.signUp(etxEmail.getText().toString(), etxPassword.getText().toString(), tipo);
            }
        },1000);

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.TXT_DEBE_RELLENAR_CORRECTAMENTE), Toast.LENGTH_LONG).show();
        }
    }

    //Metodo que comprueba las reglas
    private boolean checkFields() {
        if (etxEmail.toString().isEmpty() || etxPassword.toString().isEmpty() || !matchPattern) {
            return false;
        } else {
            return true;
        }
    }

    private void loadAuthenticateService() {
        fireBaseAuthentication = new FireBaseAuthentication() {
            @Override
            public void callBackSignUp(boolean result) {
                alert.cancel();

                if(result) {
                    startActivity(new Intent(SignUpStep2.this, SignUpSuccess.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra(getString(R.string.KEY_MAIL), etxEmail.getText().toString()));
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.TXT_NO_SE_HA_PODIDO_REGISTRAR), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void callBackLogin(boolean result) {

            }
        };
    }

    private void dynamicTextListener() {
        etxEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {
                    matchPattern = true;
                    etxEmail.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getDrawable(R.drawable.ic_person_black), null, getApplicationContext().getDrawable(R.drawable.ic_check_black), null);
                } else {
                    matchPattern = false;
                    etxEmail.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getDrawable(R.drawable.ic_person_black), null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadDatabaseUsuarios() {
        serviceDBUsuarios = new FireDBUsuarios() {
            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
}
