package com.dam.moveyourschool.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dam.moveyourschool.R;
import com.dam.moveyourschool.adapters.AdapterSpinnerActividades;
import com.dam.moveyourschool.bean.Actividad;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class NuevaReserva extends BaseActivity{

    Spinner aSpi;
    AdapterSpinnerActividades adaptador;

    ArrayList<Actividad> listaActividades = new ArrayList<>();
    Actividad actSelec;

    String idEmpresa;
    String idUsu;
    TextInputEditText fecha;
    TextInputEditText horaTxt;
    TextView numPersonas;
    TextView precioPersona;
    private int ano,mes,dia,hora,minutos;

    private DatabaseReference dbR;
    private DatabaseReference dbR2;
    private ValueEventListener vel;

    private ChildEventListener celAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idEmpresa = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idUsu = getIntent().getStringExtra("KEY_ID_USU");


        dbR = FirebaseDatabase.getInstance().getReference().child("reservas");
        dbR2 = FirebaseDatabase.getInstance().getReference().child("actividades").child(idEmpresa);

        addChildEventUsu();

        aSpi = findViewById(R.id.spActividades);
        adaptador = new AdapterSpinnerActividades(this,listaActividades);
        aSpi.setAdapter(adaptador);

        fecha = findViewById(R.id.btnFecha);
        horaTxt = findViewById(R.id.btnHora);
        numPersonas = findViewById(R.id.txtNumPersonas);
        precioPersona = findViewById(R.id.txtPrecioPersona);



        aSpi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                actSelec = listaActividades.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void Fecha(View v){

        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, ano,mes,dia);
        datePickerDialog.show();
    }

    public void Hora(View v){
        final Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                horaTxt.setText(hourOfDay + ":" + minute);
            }
        },hora,minutos,true);
        timePickerDialog.show();

    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_nueva_reserva;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }

    //Obtenemos el listado de actividades referentes a la empresa que quiere enviar el presupuesto
    private void addChildEventUsu() {
        if (celAct == null) {
            celAct = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Actividad act = dataSnapshot.getValue(Actividad.class);

                    listaActividades.add(act);

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
            dbR2.addChildEventListener(celAct);


        }

        if(vel == null){
            vel = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adaptador = new AdapterSpinnerActividades(NuevaReserva.this,listaActividades);
                    aSpi.setAdapter(adaptador);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dbR2.addListenerForSingleValueEvent(vel);
        }


    }

    private void valueEvent(){

    }




    public void mandar(View view){
        System.out.println(actSelec);
    }
}
