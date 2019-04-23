package com.dam.moveyourschool.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dam.moveyourschool.bean.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public abstract class FireBaseAuthentication {
    private static final String USUARIO_EMPRESA = "EMPRESA";
    private static final String USUARIO_EDUCACION = "EDUCACION";
    private FirebaseAuth firebaseAuth;
    private FireDBUsuarios dbUsuarios;

    public FireBaseAuthentication(){
        firebaseAuth = FirebaseAuth.getInstance();
        dbUsuarios = new FireDBUsuarios() {
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

    public void login(String username, String password){

    }

    //Metodo que agrega el usuario al FirebaseAuth y a la Database con su Tipo de Usuario
    public void signUp(String username, String password, final int tipo) {

        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Usuario usuario = new Usuario();
                    usuario.setEmail(firebaseAuth.getCurrentUser().getEmail());
                    usuario.setUid(firebaseAuth.getCurrentUser().getUid());

                    if (tipo == 1) {
                        usuario.setTipo(USUARIO_EDUCACION);
                    } else {
                        usuario.setTipo(USUARIO_EMPRESA);
                    }

                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    dbUsuarios.agregarUsuarioById(usuario);

                    callBackSignUp(true);

                } else {
                    callBackSignUp(false);
                }
            }
        });
    }

    public abstract void callBackSignUp(boolean result);
}
