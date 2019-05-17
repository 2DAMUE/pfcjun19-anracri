package com.dam.moveyourschool.services;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public abstract class FireBaseStorage {
    private StorageReference mStorageRef;
    private static final String UPLOAD_PATH = "IMAGENES_USUARIO";
    private String url;

    public FireBaseStorage() {
        this.mStorageRef = FirebaseStorage.getInstance().getReference().child(UPLOAD_PATH);
    }

    public void uploadPhoto(Uri uri) {
        url = "";
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        //Si el usuario esta loggeado procedemos a realizar la operacion de subida de fotos
        if (userAuth != null) {

            final StorageReference nodoImagenes = mStorageRef.child(userAuth.getUid() + uri.getLastPathSegment());
            UploadTask uploadTask = nodoImagenes.putFile(uri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return nodoImagenes.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        getUrl(downloadUri.toString());
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        //Sino esta loggeado no debería de haber podido acceder a este lugar
        } else {

        }
    }
    public abstract void getUrl(String url);
}
