package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.services.FireBaseAuthentication;
import com.dam.moveyourschool.views.CustomDialog;
import com.dam.moveyourschool.views.DialogLogin;
import com.dam.moveyourschool.views.ProgressBarAlert;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private DialogLogin dialogLogin;
    private FireBaseAuthentication fireBaseAuthentication;
    private ProgressBarAlert progressBarAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        progressBarAlert = new ProgressBarAlert(this);

        dialogLogin = new DialogLogin(this);
        dialogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!dialogLogin.comprobarCamposVacios()) {
                    progressBarAlert.show();
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
                    progressBarAlert.cancel();
                    startActivity(new Intent(SplashScreen.this, Actividades.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    progressBarAlert.cancel();

                    //startActivity(new Intent(SplashScreen.this, Actividades.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        };
    }

    public void comenzar(View View) {
        startActivity(new Intent(this, Actividades.class));
    }

    public void signUp(View view) {
        startActivity(new Intent(this, SignUpStep1.class));
    }

    public void signIn(View view) {
        dialogLogin.show();
    }
}
