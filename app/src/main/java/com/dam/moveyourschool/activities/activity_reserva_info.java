package com.dam.moveyourschool.activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Reserva;
import com.dam.moveyourschool.bean.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_reserva_info extends BaseActivity {

    TextView tituloRes;
    TextInputEditText fecha;
    TextInputEditText hora;
    TextInputEditText precioFinal;
    ImageView imgRes;
    private DatabaseReference dbR;

    private DatabaseReference dbRUsers;
    private ChildEventListener cel;
    private ValueEventListener vel;
    ArrayList<Usuario> users = new ArrayList<>();

    Button acept;
    Button rech;
    Reserva res;
    FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        tituloRes = findViewById(R.id.txtTituloResInfo);
        fecha = findViewById(R.id.fechaInforReserva);
        hora = findViewById(R.id.horaInforReserva);
        precioFinal = findViewById(R.id.precioFinalInforReserva);
        imgRes = findViewById(R.id.imgResInfo);
        acept = findViewById(R.id.btnAceptarPresupuesto);
        rech = findViewById(R.id.btnRechazarPresupuesto);

        user  = FirebaseAuth.getInstance().getCurrentUser();

        res = getIntent().getParcelableExtra(getString(R.string.KEY_RESERVA_INFO));

        tituloRes.setText(res.getTituloActiRef());
        fecha.setText(res.getFecha());
        hora.setText(res.getHora());

        if(res.getUrlFotoAct() != null){
            Glide.with(this).load(res.getUrlFotoAct()).into(imgRes);
        }

        dbR = FirebaseDatabase.getInstance().getReference().child("reservas");
        dbRUsers = FirebaseDatabase.getInstance().getReference().child("usuario");
        addChildEvent();

        double precioF = res.getNumPersonas() * res.getPrecioFinal();
        precioFinal.setText(String.valueOf(precioF));

        tituloRes.setEnabled(false);
        fecha.setEnabled(false);
        hora.setEnabled(false);
        precioFinal.setEnabled(false);

    }
    //SE PODRIA O DESACTIVAR O PONER EN INVISIBLE
    private void desactivarBotones() {

        if(res.getEstado().equals("ACEPTADA") || res.getEstado().equals("RECHAZADA") || buscarUsuario()){
            acept.setEnabled(false);
            rech.setEnabled(false);
        }

    }

    private boolean buscarUsuario() {

        boolean encontrado = false;

        for (int i = 0; i<users.size();i++){

            if(users.get(i).getUid().equals(user.getUid())){

                if (users.get(i).getTipo().equals("EMPRESA")){
                    encontrado = true;
                }

            }

        }
        return encontrado;
    }

    private void addChildEvent() {
        if(cel == null) {
            cel = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Usuario us = dataSnapshot.getValue(Usuario.class);

                    users.add(us);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dbRUsers.addChildEventListener(cel);
        }

        if(vel == null){
            vel = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    desactivarBotones();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dbRUsers.addListenerForSingleValueEvent(vel);
        }
    }


    public void aceptarPres(View v){

        new AlertDialog.Builder(this)
                .setTitle("Aceptar presupuesto")
                .setMessage("¿Estas seguro que deseas aceptar el presupuesto?.Esta accion es irreversible")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        res.setEstado("ACEPTADA");

                        dbR.child(res.getIdReserva()).setValue(res).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast toast2 = Toast.makeText(getApplicationContext(), getString(R.string.reserva_aceptada), Toast.LENGTH_SHORT);
                                toast2.show();
                                finish();
                            }
                        });
                    }
                })

                .setNegativeButton("Cancelar", null)
                .setIcon(R.drawable.ic_aceptado)
                .show();


    }
    public void rechazarPres(View v){

        new AlertDialog.Builder(this)
                .setTitle("Rechazar presupuesto")
                .setMessage("¿Estas seguro que deseas rechazar el presupuesto?.Esta accion es irreversible")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        res.setEstado("RECHAZADA");

                        dbR.child(res.getIdReserva()).setValue(res).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast toast2 = Toast.makeText(getApplicationContext(), getString(R.string.reserva_rechazada), Toast.LENGTH_SHORT);
                                toast2.show();
                                finish();
                            }
                        });
                    }
                })

                .setNegativeButton("Cancelar", null)
                .setIcon(R.drawable.ic_rechazado)
                .show();

    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_reserva_info;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }
}
