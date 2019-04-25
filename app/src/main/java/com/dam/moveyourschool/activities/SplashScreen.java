package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dam.moveyourschool.R;
import com.dam.moveyourschool.services.FireBaseAuthentication;
import com.dam.moveyourschool.views.CustomDialog;
import com.dam.moveyourschool.views.DialogLogin;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private DialogLogin dialogLogin;
    private FireBaseAuthentication fireBaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        dialogLogin = new DialogLogin(this);
        dialogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!dialogLogin.comprobarCamposVacios()) {
                    fireBaseAuthentication.login(dialogLogin.getEtxLoginNombre().getText().toString(), dialogLogin.getEtxLoginPassword().getText().toString());
                } else {
                    new CustomDialog(SplashScreen.this, R.string.TXT_DEBE_RELLENAR_CORRECTAMENTE).show();
                }
            }
        });

        fireBaseAuthentication = new FireBaseAuthentication() {
            @Override
            public void callBackSignUp(boolean result) {

            }

            @Override
            public void callBackLogin(boolean result) {
                if (result == true && FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(SplashScreen.this, Actividades.class)
                            .putExtra(getString(R.string.KEY_USER), FirebaseAuth.getInstance().getCurrentUser())
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    startActivity(new Intent(SplashScreen.this, Actividades.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        };
    }

    public void comenzar(View View) {
        dialogLogin.show();
    }

    public void signUp(View view) {
        startActivity(new Intent(this, SignUpStep1.class));
    }
}
