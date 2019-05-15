package com.dam.moveyourschool.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.fragments.Actividad_Form_Step2_Frag;
import com.dam.moveyourschool.fragments.Actividad_Form_Step_1;
import com.dam.moveyourschool.fragments.Actividad_Form_Step_3_Frag;
import com.dam.moveyourschool.fragments.Actividad_Form_Step_4;
import com.dam.moveyourschool.services.FireDBActividades;
import com.dam.moveyourschool.utils.Constantes;
import com.dam.moveyourschool.views.CustomDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ActividadForm extends BaseActivity {
    private FrameLayout fragmentContainer;
    private Actividad actividad;
    private FireDBActividades serviceDBActividades;
    private Button btnNext;
    private boolean primeraVez = true;
    private boolean modificar = false;
    private String selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializamos el contenedor de fragments
        fragmentContainer = findViewById(R.id.fragmentContainer);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setVisibility(View.GONE);

        modificar = getIntent().getBooleanExtra(getString(R.string.KEY_MOD_ACTIVITY), false);

        //Si no vamos a modificar
        if (!modificar) {

            //Inicializamos el Activity con el atributo actividad en null asociado al Contexto Custom
            if (primeraVez) {
                ((CustomContext) (getApplicationContext())).setActividad(null);
                actividad = ((CustomContext) (getApplicationContext())).getActividad();
            }

            //Cargamos el primer fragmento de la actividad sin agregarlo al backstack
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new Actividad_Form_Step_1())
                    .commit();

            //Inicializamos el servicio de la database con sus listeners
            listenService();

        } else {
            //Obtenemos un booleano que nos define si vamos a modificar o no una actividad
            selectedFragment = getIntent().getStringExtra(getString(R.string.KEY_SELECTED_FRAGMENT));
            actividad = (Actividad) getIntent().getSerializableExtra(getString(R.string.KEY_ACTIVIDAD));

            //Activamos el servicio de Firebase
            listenService();

            //Cargamos el fragment que haya seleccionado el usuario
            cargarFragmentModificar();
        }

    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_actividad_form;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void nextStep(View view) {

        //Si vamos a crear una nueva actividad
        if (!modificar) {

            //Obtenemos el fragmento que se muestra en cada momento
            Fragment fragmentoActual = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            //Si es el primer fragmento, invocamos el metodo registrar y pasamos al siguiente fragmento
            if (fragmentoActual instanceof Actividad_Form_Step_1) {

                if (actividad != null && actividad.getCategoria() != null && !actividad.getCategoria().equals("")) {
                    changeFragment(new Actividad_Form_Step2_Frag());
                    btnNext.setVisibility(View.VISIBLE);

                } else {
                    new CustomDialog(this, R.string.EXCEPT_UNKNOWN_ERROR).show();
                    recreate();
                }
            }

            else if (fragmentoActual instanceof Actividad_Form_Step2_Frag) {

                boolean success = ((Actividad_Form_Step2_Frag) fragmentoActual).registrar();

                if (success) {
                    changeFragment(new Actividad_Form_Step_3_Frag());
                }


            } else if (fragmentoActual instanceof Actividad_Form_Step_3_Frag) {
                boolean success = ((Actividad_Form_Step_3_Frag) fragmentoActual).registrar();

                if (success) {
                    changeFragment(new Actividad_Form_Step_4());
                }


            } else if (fragmentoActual instanceof Actividad_Form_Step_4) {
                boolean success = ((Actividad_Form_Step_4) fragmentoActual).registrar();

                if (success) {
                    startActivity(new Intent(this, Actividades.class));
                    listenService();
                    serviceDBActividades.agregarActividad(actividad);
                }
            }

            //Si vamos a modificar una actividad ya existente seleccionada por el usuario
        } else {

            Fragment fragmentoActual = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            if (fragmentoActual instanceof Actividad_Form_Step_1) {
                serviceDBActividades.modificarActividad(actividad);
            }


            if (fragmentoActual instanceof  Actividad_Form_Step2_Frag) {

                boolean success = ((Actividad_Form_Step2_Frag) fragmentoActual).modificar();

                if (success) {
                    serviceDBActividades.modificarActividad(actividad);
                }
            }

            if (fragmentoActual instanceof  Actividad_Form_Step_4) {
                boolean success = ((Actividad_Form_Step_4) fragmentoActual).registrar();

                if (success) {
                    serviceDBActividades.modificarActividad(actividad);
                }
            }

        }

    }

    private void cargarFragmentModificar() {
        btnNext.setText("Guardar Cambios");
        btnNext.setVisibility(View.VISIBLE);

        if (selectedFragment.equals(Constantes.fragmentCategoria)) {
            btnNext.setVisibility(View.GONE);
            changeFragmentNoBackStack(new Actividad_Form_Step_1());

        } else if (selectedFragment.equals(Constantes.fragmentGeneral)) {
            changeFragmentNoBackStack(new Actividad_Form_Step2_Frag());


        } else if (selectedFragment.equals(Constantes.fragmentLocalizacion)) {
            changeFragmentNoBackStack(new Actividad_Form_Step_3_Frag());

        } else if (selectedFragment.equals(Constantes.fragmentDetalles)) {
            changeFragmentNoBackStack(new Actividad_Form_Step_4());
        }
    }

    private void changeFragment(Fragment fragmento) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmento)
                .addToBackStack(null)
                .commit();
    }

    private void changeFragmentNoBackStack(Fragment fragmento) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmento)
                .commit();
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad (Actividad actividad){
        this.actividad = actividad;
    }

    private void listenService() {
        serviceDBActividades = new FireDBActividades() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void getKey(String key) {

                if (key != null && !key.equals("")) {
                    startActivity(new Intent(ActividadForm.this, ActividadFormSuccess.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    new CustomDialog(ActividadForm.this, R.string.EXCEPT_UNKNOWN_ERROR).show();
                }
            }

            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //Cuando se modifica el nodo mostramos un snackbar que confirma que se ha realizado con éxito y regresamos al activity anterior
                for (DataSnapshot aux : dataSnapshot.getChildren()) {
                    if (aux.getKey().equals(actividad.getUid_actividad())) {
                        View view = findViewById(R.id.rlAcForm);
                        Snackbar snack = Snackbar.make(view, getString(R.string.VAL_MODIF_ACTIVIDAD), Snackbar.LENGTH_SHORT);
                        snack.addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                finish();
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {

                            }
                        });
                        snack.show();
                    }
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void categoriaSeleccionada(View view) {

        if (!modificar) {
            actividad = new Actividad();
        }

        String id = getResources().getResourceEntryName(view.getId());
        Log.e("EL ID ES", id);
        switch (id) {
            case "linearArte":
                actividad.setCategoria(getString(R.string.TXT_ACTIVIDAD_ARTISTICA));
                break;
            case "linearCiudad":
                actividad.setCategoria(getString(R.string.TXT_VISITA_CIUDAD));
                break;
            case "linearDeporte":
                actividad.setCategoria(getString(R.string.TXT_ACTIVIDAD_DEPORTIVA));
                break;
            case "linearFabrica":
                actividad.setCategoria(getString(R.string.TXT_VISITA_FABRICA));
                break;
            case "linearFiesta":
                actividad.setCategoria(getString(R.string.TXT_FIESTA_POPULAR));
                break;
            case "linearReligion":
                actividad.setCategoria(getString(R.string.TXT_ACTIVIDAD_RELIGIOSA));
                break;
            case "linearJuego":
                actividad.setCategoria(getString(R.string.TXT_JUEGO_EDUCATIVO));
                break;
            case "linearMonumental":
                actividad.setCategoria(getString(R.string.TXT_VISITA_MONUMENTAL));
                break;
            case "linearMuseo":
                actividad.setCategoria(getString(R.string.TXT_VISITA_MUSEO));
                break;
            case "linearMusica":
                actividad.setCategoria(getString(R.string.TXT_ACTIVIDAD_MUSICAL));
                break;
            case "linearNaturaleza":
                actividad.setCategoria(getString(R.string.TXT_ACTIVIDAD_NATURALEZA));
                break;
            case "linearTecnologia":
                actividad.setCategoria(getString(R.string.TXT_ACTIVIDAD_TECNOLOGÍA));
                break;
            case "linearTaller":
                actividad.setCategoria(getString(R.string.TXT_TALLER_EDUCATIVO));
                break;
            case "linearTeatro":
                actividad.setCategoria(getString(R.string.TXT_VISITA_TEATRO));
                break;
            case "linearOtros":
                actividad.setCategoria(getString(R.string.TXT_OTRAS_ACTIVIDADES));
                break;
        }
        Log.e("actividad", actividad.getCategoria().toString());
        nextStep(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        //fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    public Button getBtnNext() {
        return btnNext;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(getString(R.string.KEY_ACTIVIDAD), actividad);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        actividad = (Actividad) savedInstanceState.get(getString(R.string.KEY_ACTIVIDAD));
        ((CustomContext) (getApplicationContext())).setActividad(actividad);
        primeraVez = false;
    }

    public boolean getModificar() {
        return modificar;
    }

    public Actividad getActividadModificar() {
        return actividad;
    }
}
