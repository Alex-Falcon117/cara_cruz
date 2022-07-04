package com.idevapp.cara_cruz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    //Views
    private TextView mtv;
    private Button mbtn;
    private ImageView miv, miv1, miv2, miv3, miv4, miv5, miv6;
    private MaterialToolbar mToolBar;
    private BottomSheetDialog mBSheetDialog;
    private SwitchCompat mSwitch_sonido;
    private Spinner mSpinner;

    //Dialog
    private Dialog dialogCara, dialogCruz;

    //Giroscopio
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;


    String moneda[]={"CARA","CRUZ"};
    Random random = new Random();

    int moneda_selec;
    int opcionSpinner;
    byte cara = 0;
    byte cruz = 0;
    boolean valor = false;
    boolean valor2 = false;
    boolean estadoSW_sonido = false;
    boolean uno = false;
    boolean dos = false;
    boolean tres = true;
    String[] partidas = {"3", "2", "1"};
    ArrayAdapter<String> arrayPartidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2000);
            setTheme(R.style.Theme_Cara_cruz);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-------------------------------------------------------------------------------------
        //Referencia a las vistas
        mToolBar=findViewById(R.id.toolAppBar);

        mbtn=findViewById(R.id.btn_tirar);
        miv=findViewById(R.id.iv_moneda);
        mtv=findViewById(R.id.tv_texto);

        miv1=findViewById(R.id.iv_1);
        miv2=findViewById(R.id.iv_2);
        miv3=findViewById(R.id.iv_3);
        miv4=findViewById(R.id.iv_4);
        miv5=findViewById(R.id.iv_5);
        miv6=findViewById(R.id.iv_6);

        arrayPartidas = new ArrayAdapter<String>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, partidas);

        //Sensores
       // sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
       // sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
       // sensores();

        //Evento botonTirar
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarea();
            }
        });

        //Mebu / ButtonSheet
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu){
                    mBSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);
                    View sheetView = LayoutInflater.from(MainActivity.this).inflate(R.layout.bs_menu, null);


                    sheetView.findViewById(R.id.tv_info).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Funciona", Toast.LENGTH_SHORT).show();
                        }
                    });

                    mSwitch_sonido=sheetView.findViewById(R.id.sw_sonido);
                    mSpinner= sheetView.findViewById(R.id.spinner);

                    mSpinner.setAdapter(arrayPartidas);

                 // Spinner
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            opcionSpinner = i;

                            if (cara == 2 || cruz == 2 || cara == 1 || cruz == 1) {
                                if (i == 0){

                                }else {
                                    Toast.makeText(MainActivity.this, "Finalice esta partida primero", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                            switch (i) {
                                case 0:
                                    uno = false;
                                    dos = false;
                                    tres = true;

                                    miv2.setVisibility(View.VISIBLE);
                                    miv3.setVisibility(View.VISIBLE);

                                    miv5.setVisibility(View.VISIBLE);
                                    miv6.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    uno = false;
                                    dos = true;
                                    tres = false;
                                    miv3.setVisibility(View.INVISIBLE);
                                    miv6.setVisibility(View.INVISIBLE);
                                    break;
                                default:
                                    uno = true;
                                    dos = false;
                                    tres = false;
                                    miv2.setVisibility(View.INVISIBLE);
                                    miv3.setVisibility(View.INVISIBLE);

                                    miv5.setVisibility(View.INVISIBLE);
                                    miv6.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    mSwitch_sonido.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mSwitch_sonido.isChecked()){
                                //DO
                                estadoSW_sonido=true;
                            }else {
                                //DO
                                estadoSW_sonido=false;
                            }
                        }
                    });
                    if (mSwitch_sonido.isChecked()==false && estadoSW_sonido==true){
                        mSwitch_sonido.setChecked(true);
                    }

                    mBSheetDialog.setContentView(sheetView);
                    mBSheetDialog.show();
                    return true;
                }else{
                    return false;
                }
            }
        });
    }//Fin onCreate---------------------------------------------------------------------------------

    private void pararMoneda() {
        moneda_selec=random.nextInt(2);
        mbtn.setVisibility(View.VISIBLE);
        if (tres == true) {
            partidaTres(moneda_selec);
        }else if(dos == true){
            partidaDos(moneda_selec);
        }else if(uno == true){
            partidaUno(moneda_selec);
        }

    }

    //Utiliza el giroscopio
    private void sensores() {

        if (sensor==null){
            Toast.makeText(this, "No se puede usar la funcion de inclinacion", Toast.LENGTH_LONG).show();
        }else {
            sensorEventListener=new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float x = sensorEvent.values[0];

                    if(x<-10 && valor == false){
                        valor = true;
                    } else if(x>10 && valor2==false){
                        valor2=true;

                    }else if (valor==true && valor2==true){
                        valor=false;
                        valor2=false;
                        tarea();
                    }

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
        }
    }

    private void tarea() {
        anim();
        //Da un tiempo de espera de 2 segundos
        CountDownTimer countDownTimer = new CountDownTimer(2000,2000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                pararMoneda();
            }
        }.start();
    }


    //Animacion de la moneda
    private void anim() {
        mbtn.setVisibility(View.INVISIBLE);
        mtv.setText("...");
        Glide.with(getApplicationContext()).load(R.drawable.moneda_anim).into(miv);

        if (estadoSW_sonido==true){
            sonido();
        }

    }

    private void sonido(){
                MediaPlayer mediaPlayer2 = MediaPlayer.create(this,R.raw.moneda_gira);
                mediaPlayer2.start();
    }

    //Para tres partidas
    private void partidaTres(int i) {
        if(i==1){
            mtv.setText(moneda[i]);
            miv.setImageResource(R.drawable.cruz);
            cruz ++;
            switch (cruz){
                case 1:
                    miv1.setImageResource(R.drawable.punto_relleno);
                break;
                case 2: miv2.setImageResource(R.drawable.punto_relleno);
                break;
                case 3: miv3.setImageResource(R.drawable.punto_relleno);
                dialogoCruz();
                break;
            }
        }
        else {
            miv.setImageResource(R.drawable.cara);
            mtv.setText(moneda[i]);
            cara ++;
            switch (cara){
                case 1: miv4.setImageResource(R.drawable.punto_relleno);
                    break;
                case 2: miv5.setImageResource(R.drawable.punto_relleno);
                    break;
                case 3: miv6.setImageResource(R.drawable.punto_relleno);
                    dialogoCara();
                break;
            }
        }
    }

    //Para dos partidas
    private void partidaDos(int i){
        if (i == 1){
            mtv.setText(moneda[i]);
            miv.setImageResource(R.drawable.cruz);
            cruz ++;

            switch (cruz){
                case 1:
                    miv1.setImageResource(R.drawable.punto_relleno);
                    break;
                case 2: miv2.setImageResource(R.drawable.punto_relleno);
                    dialogoCruz();
                    break;
            }
        }else {
            miv.setImageResource(R.drawable.cara);
            mtv.setText(moneda[i]);
            cara ++;
            switch (cara){
                case 1: miv4.setImageResource(R.drawable.punto_relleno);
                    break;
                case 2: miv5.setImageResource(R.drawable.punto_relleno);
                dialogoCara();
                    break;
            }
        }

    }

    //Una partida
    private void  partidaUno(int i){
        if (i == 1){
            mtv.setText(moneda[i]);
            miv.setImageResource(R.drawable.cruz);
            cruz ++;

            switch (cruz){
                case 1:
                    miv1.setImageResource(R.drawable.punto_relleno);
                    dialogoCruz();
                    break;
            }
        }else {
            miv.setImageResource(R.drawable.cara);
            mtv.setText(moneda[i]);
            cara ++;
            switch (cara){
                case 1: miv4.setImageResource(R.drawable.punto_relleno);
                    dialogoCara();
                    break;
            }
        }
    }


    //Despliega un cuadro de dialogo
    private void dialogoCara(){
        dialogCara = new Dialog(MainActivity.this);
        dialogCara.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCara.setContentView(R.layout.dialog_cara);
        dialogCara.setCancelable(false);

        dialogCara.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receterarTodo();
                dialogCara.cancel();
            }
        });
        dialogCara.show();
    }

    private void dialogoCruz(){
        dialogCruz = new Dialog(MainActivity.this);
        dialogCruz.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCruz.setContentView(R.layout.dialog_cruz);
        dialogCruz.setCancelable(false);

        dialogCruz.findViewById(R.id.btn_ok2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receterarTodo();
                dialogCruz.cancel();
            }
        });
        dialogCruz.show();
    }

    //Recetea todas las partidas
    private void receterarTodo(){
        cara=0;
        cruz=0;
        miv1.setImageResource(R.drawable.punto_linea);
        miv2.setImageResource(R.drawable.punto_linea);
        miv3.setImageResource(R.drawable.punto_linea);

        miv4.setImageResource(R.drawable.punto_linea);
        miv5.setImageResource(R.drawable.punto_linea);
        miv6.setImageResource(R.drawable.punto_linea);
    }

    //Sensores activacion
    public void iniciarSensor(){
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void pararSensor(){
        sensorManager.unregisterListener(sensorEventListener);
    }


    @Override
    protected void onPause() {
      // pararSensor();
        super.onPause();
    }

    @Override
    protected void onResume() {
     //  iniciarSensor();
        super.onResume();
    }
}