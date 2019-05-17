package com.dam.moveyourschool.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireBaseStorage;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.dam.moveyourschool.utils.Constantes;
import com.dam.moveyourschool.views.CustomDialog;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserEdit extends BaseActivity {
    private FireDBUsuarios serviceDbUsuarios;

    private EditText etxNomEmpresa;
    private EditText etxCif;
    private EditText etxTitular;
    private EditText etxMunicipio;
    private EditText etxDireccion;
    private EditText etxCodPostal;
    private EditText etxDescripcion;
    private String localidad;
    private Usuario userFinal;
    private Spinner spinnerLoc;
    private CircleImageView imgEdit;
    private ArrayAdapter<String> adapter;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicialización de los Atributos de la clase
        etxNomEmpresa = findViewById(R.id.etxNomEmpresa);
        etxCif = findViewById(R.id.etxCif);
        etxTitular = findViewById(R.id.etxTitular);
        etxDireccion = findViewById(R.id.etxDir);
        etxMunicipio = findViewById(R.id.etxMunicipio);
        etxCodPostal = findViewById(R.id.etxPostal);
        etxDescripcion = findViewById(R.id.etxDescripcion);
        imgEdit = findViewById(R.id.imgEdit);
        btnGuardar = findViewById(R.id.btnGuardar);

        spinnerLoc = findViewById(R.id.spinnerLoc);
        spinnerLoc.setPrompt(getString(R.string.VAL_ELIJA_LOCALIDAD));

        userFinal = new Usuario();

        //Listener de opción Spinner
        spinnerLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                localidad = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new Constantes(this) {
            @Override
            public void callBackGetProvincias(ArrayList<String> provincias) {
                String provinciasArrayAdater[] = new String[provincias.size()];

                for (int i = 0; i < provincias.size(); i++) {
                    provinciasArrayAdater[i] = provincias.get(i);
                }
                adapter = new ArrayAdapter<String>(UserEdit.this,
                        R.layout.item_spinner_localidad, provinciasArrayAdater);
                spinnerLoc.setAdapter(adapter);
            }
        };

        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            serviceDbUsuarios = new FireDBUsuarios() {
                @Override
                public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getKey().equals(userAuth.getUid())) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        updateFields(usuario);
                    }
                }

                @Override
                public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getKey().equals(userAuth.getUid())) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        updateFields(usuario);
                    }
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

    private void updateFields(Usuario usuario) {
        if (usuario.getUid() != null) {
            userFinal.setUid(usuario.getUid());
        }

        if (usuario.getNombre() != null) {
            userFinal.setNombre(usuario.getNombre());
            etxNomEmpresa.setText(usuario.getNombre());
        }

        if (usuario.getCif() != null) {
            userFinal.setCif(usuario.getCif());
            etxCif.setText(usuario.getCif());
        }

        if (usuario.getTitular() != null) {
            userFinal.setTitular(usuario.getTitular());
            etxTitular.setText(usuario.getTitular());
        }

        if (usuario.getEmail() != null) {
            userFinal.setEmail(usuario.getEmail());
        }

        if (usuario.getDireccion() != null) {
            userFinal.setDireccion(usuario.getDireccion());
            etxDireccion.setText(usuario.getDireccion());
        }


        if (usuario.getLocalidad() != null && !usuario.getLocalidad().equals(getString(R.string.VAL_ELIJA_LOCALIDAD))) {
            userFinal.setLocalidad(usuario.getLocalidad());
            localidad = usuario.getLocalidad();
            spinnerLoc.setSelection(adapter.getPosition(localidad));
        }

        if (usuario.getMunicipio() != null) {
            userFinal.setMunicipio(usuario.getMunicipio());
            etxMunicipio.setText(usuario.getMunicipio());
        }

        if (usuario.getCodigo_postal() != null) {
            userFinal.setCodigo_postal(usuario.getCodigo_postal());
            etxCodPostal.setText(usuario.getCodigo_postal());
        }

        if (usuario.getDescripcion() != null) {
            userFinal.setDescripcion(usuario.getDescripcion());
            etxDescripcion.setText(usuario.getDescripcion());
        }

        if (usuario.getTipo() != null) {
            userFinal.setTipo(usuario.getTipo());
        }


        if (usuario.getUrlFoto() != null) {
            userFinal.setUrlFoto(usuario.getUrlFoto());
        }
    }

    public void guardarCambios(View view) {
        userFinal.setNombre(etxNomEmpresa.getText().toString());
        userFinal.setCif(etxCif.getText().toString());
        userFinal.setTitular(etxTitular.getText().toString());
        userFinal.setDescripcion(etxDescripcion.getText().toString());
        userFinal.setDireccion(etxDireccion.getText().toString());
        userFinal.setLocalidad(localidad);
        userFinal.setMunicipio(etxMunicipio.getText().toString());
        userFinal.setCodigo_postal(etxCodPostal.getText().toString());
        userFinal.setDescripcion(etxDescripcion.getText().toString());

        serviceDbUsuarios.updateUserById(userFinal);
        startActivity(new Intent(this, UserDetail.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        btnGuardar.setEnabled(false);

        if (resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            Glide.with(this).load(fileUri).into(imgEdit);

            new FireBaseStorage() {
                @Override
                public void getUrl(String url) {
                    userFinal.setUrlFoto(url);
                    btnGuardar.setEnabled(true);

                }
            }.uploadPhoto(fileUri);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            String.format(getString(R.string.CAMERA_ERROR), ImagePicker.Companion.getError(data));
            new CustomDialog(this, R.string.CAMERA_ERROR).show();
        }
    }

    public void onPickImage(View view) {
        ImagePicker.Companion.with(this)
                //.crop(1f, 1f)       //Crop Square image(Optional)
                .compress(1024)   //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_user_edit;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }

}
