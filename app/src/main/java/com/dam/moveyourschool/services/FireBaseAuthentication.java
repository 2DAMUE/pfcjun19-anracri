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
    //Constantes Excepciones de Autenticación
    private static final String EXCEPT_INVALID_USER = "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.";
    private static final String EXCEPT_INVALID_PASSWORD = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.";
    private static final String EXCEPT_SERVER_TIMEOUT = "com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred.";
    private static final String EXCEPT_INVALID_PASSWORD_STRENGTH = "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]";
    private static final String EXCEPT_ACCOUNT_ALREADY_EXISTS = "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.";

    //Constantes Alta Tipo de Usuario en Database
    private static final String USUARIO_EMPRESA = "EMPRESA";
    private static final String USUARIO_EDUCACION = "EDUCACION";

    private FirebaseAuth firebaseAuth;
    private FireDBUsuarios dbUsuarios;

    public FireBaseAuthentication() {
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

    public void login(String username, String password) {
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        }

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    callBackLogin(0);

                } else {
                    if (task.getException().toString().equals(EXCEPT_INVALID_USER)) {
                        callBackLogin(1);

                    } else if (task.getException().toString().equals(EXCEPT_INVALID_PASSWORD)) {
                        callBackLogin(2);

                    } else if (task.getException().toString().equals(EXCEPT_SERVER_TIMEOUT)) {
                        callBackLogin(3);
                    }
                }
            }
        });
    }

    //Metodo que agrega el usuario al FirebaseAuth y a la Database con su Tipo de Usuario
    public void signUp(String username, String password, final int tipo) {

        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
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

                    callBackSignUp(0);

                } else {

                    if (task.getException().toString().equals(EXCEPT_INVALID_PASSWORD_STRENGTH)) {
                        callBackSignUp(1);

                    } else if (task.getException().toString().equals(EXCEPT_ACCOUNT_ALREADY_EXISTS)) {
                        callBackSignUp(2);

                    } else if (task.getException().toString().equals(EXCEPT_SERVER_TIMEOUT)) {
                        callBackSignUp(3);

                    }
                }
            }
        });
    }

    public abstract void callBackSignUp(int result);

    public abstract void callBackLogin(int result);
}
