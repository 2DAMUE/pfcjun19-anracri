package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dam.moveyourschool.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    public void comenzar(View View) {
        startActivity(new Intent(this, Actividades.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    public void signUp(View view) {
        startActivity(new Intent(this, SignUpStep1.class));
    }
}
