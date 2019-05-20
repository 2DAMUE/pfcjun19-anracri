package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.dam.moveyourschool.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpStep1 extends AppCompatActivity {
    private FirebaseAuth serviceAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step1);
        overridePendingTransition(R.anim.anim_left, R.anim.anim_right);
    }

    public void signUpInstitucionEducativa(View view) {
        Intent i = new Intent(this, SignUpStep2.class);
        i.putExtra(getString(R.string.KEY_TYPE),1 );
        startActivity(i);

    }

    public void signUpEmpresaActividades(View view) {
        Intent i = new Intent(this, SignUpStep2.class);
        i.putExtra(getString(R.string.KEY_TYPE),2 );
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_left, R.anim.anim_right);
    }
}
