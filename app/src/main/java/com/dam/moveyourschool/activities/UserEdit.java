package com.dam.moveyourschool.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.dam.moveyourschool.utils.Constantes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;

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
    private ArrayAdapter<String> adapter;

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

        spinnerLoc = findViewById(R.id.spinnerLoc);

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
            userFinal.setTitular(usuario.getEmail());
        }

        if (usuario.getDireccion() != null) {
            userFinal.setDireccion(usuario.getDireccion());
            etxDireccion.setText(usuario.getDireccion());
        }


        if (usuario.getLocalidad() != null) {
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
