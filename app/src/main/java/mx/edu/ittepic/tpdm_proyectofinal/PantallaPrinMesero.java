package mx.edu.ittepic.tpdm_proyectofinal;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PantallaPrinMesero extends AppCompatActivity {
    ImageView mesa1, mesa2, mesa3, mesa4, mesa5;
    String [][] matriz;
    ConexionBD conexion;
    String numeroMesa="", disponible="";
    String user = "";
    TextView titulo;
    Archivos archivos;

    BroadcastReceiver recibidorMensajsSMS = null;
    String smsTratar = "";
    String[] vector;
    Boolean ban;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_prin_mesero);

        titulo = (TextView) findViewById(R.id.textView2);
        archivos = new Archivos(PantallaPrinMesero.this);
        titulo.setText(archivos.llenarTitulo());

        user = getIntent().getStringExtra("user");
        Toast.makeText(PantallaPrinMesero.this, user, Toast.LENGTH_SHORT).show();
        mesa1 = (ImageView) findViewById(R.id.imageView);
        mesa2 = (ImageView) findViewById(R.id.imageView2);
        mesa3 = (ImageView) findViewById(R.id.imageView3);
        mesa4 = (ImageView) findViewById(R.id.imageView4);
        mesa5 = (ImageView) findViewById(R.id.imageView5);

        //conexion = new ConexionBD(this,"Restaurant",null,1);

        conexion = new ConexionBD(this,"Restaurant",null,1);

        ban = false;

        mesa1.setImageResource(R.drawable.mesad);
        mesa2.setImageResource(R.drawable.mesad);
        mesa3.setImageResource(R.drawable.mesad);
        mesa4.setImageResource(R.drawable.mesad);
        mesa5.setImageResource(R.drawable.mesad);


        mesa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultaDisponible(1);
                //Toast.makeText(PantallaPrinMesero.this, "" + disponible, Toast.LENGTH_SHORT).show();
                if (disponible.equals("S")) {
                  //  Toast.makeText(PantallaPrinMesero.this, "Entró click", Toast.LENGTH_LONG).show();
                    ocuparMesa("m1");
                } else {
                    Intent order = new Intent(PantallaPrinMesero.this, PantallaOrdenes.class);
                    order.putExtra("id",1);
                    startActivity(order);
                }
            }
        });

        mesa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultaDisponible(2);
                //Toast.makeText(PantallaPrinMesero.this, "" + disponible, Toast.LENGTH_SHORT).show();
                if (disponible.equals("S")) {
                    //Toast.makeText(PantallaPrinMesero.this, "Entró click", Toast.LENGTH_LONG).show();
                    ocuparMesa("m2");
                } else {
                    Intent order = new Intent(PantallaPrinMesero.this, PantallaOrdenes.class);
                    order.putExtra("mesa", "Mesa 2");
                    startActivity(order);
                }
            }
        });

        mesa3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultaDisponible(3);
                //Toast.makeText(PantallaPrinMesero.this, "" + disponible, Toast.LENGTH_SHORT).show();
                if (disponible.equals("S")) {
                  //  Toast.makeText(PantallaPrinMesero.this, "Entró click", Toast.LENGTH_LONG).show();
                    ocuparMesa("m3");
                } else {
                    Intent order = new Intent(PantallaPrinMesero.this, PantallaOrdenes.class);
                    order.putExtra("mesa", "Mesa 3");
                    startActivity(order);
                }
            }

        });

        mesa4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultaDisponible(4);
                //Toast.makeText(PantallaPrinMesero.this, "" + disponible, Toast.LENGTH_SHORT).show();
                if (disponible.equals("S")) {
                  //  Toast.makeText(PantallaPrinMesero.this, "Entró click", Toast.LENGTH_LONG).show();
                    ocuparMesa("m4");
                } else {
                    Intent order = new Intent(PantallaPrinMesero.this, PantallaOrdenes.class);
                    order.putExtra("mesa", "Mesa 4");
                    startActivity(order);
                }
            }
        });

        mesa5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultaDisponible(5);
                //Toast.makeText(PantallaPrinMesero.this, "" + disponible, Toast.LENGTH_SHORT).show();
                if (disponible.equals("S")) {
                  //  Toast.makeText(PantallaPrinMesero.this, "Entró click", Toast.LENGTH_LONG).show();
                    ocuparMesa("m5");
                } else {
                    Intent order = new Intent(PantallaPrinMesero.this, PantallaOrdenes.class);
                    order.putExtra("mesa", "Mesa 5");
                    startActivity(order);
                }
            }
        });


        IntentFilter evento = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        recibidorMensajsSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();

                Object mensajeObjeto[] = (Object[]) b.get("pdus");

                SmsMessage mensaje = SmsMessage.createFromPdu((byte[]) mensajeObjeto[0]); // despues de API 17 se usa el de dos parametros y lleva en el segundo "UTF-8";

                smsTratar = mensaje.getMessageBody();
                vector = smsTratar.split(",");
                ban = true;

                /*AlertDialog.Builder mostrar = new AlertDialog.Builder(PantallaPrinMesero.this);
                mostrar.setTitle("Llego desde: " + mensaje.getDisplayOriginatingAddress());
                mostrar.setMessage(smsTratar + " mesa: " + vector[0] + "satus: " + vector[1]+" xx"+ban);
                mostrar.show();*/



            }


        };

        registerReceiver(recibidorMensajsSMS, evento);

        /*AlertDialog.Builder a = new AlertDialog.Builder(PantallaPrinMesero.this);
        a.setTitle("xx").setMessage("" + ban).show();*/


        timer = new CountDownTimer(8000,200) {
            @Override
            public void onTick(long millisUntilFinished) {

                if(ban == true){
                    //Toast.makeText(PantallaPrinMesero.this,""+vector[1]+vector[0],Toast.LENGTH_LONG).show();
                    switch (vector[0]){
                        case "m1":
                            tratarMensaje(0, "Mesa 1", vector[1], 1);

                            if(vector[1].equals("o")){
                                mesa1.setImageResource(R.drawable.mesao);
                            }else {
                                mesa1.setImageResource(R.drawable.mesad);
                            }

                            ban = false;
                            break;
                        case "m2":
                            tratarMensaje(0, "Mesa 2", vector[1], 2);

                            if(vector[1].equals("o")){
                                mesa2.setImageResource(R.drawable.mesao);
                            }else {
                                mesa2.setImageResource(R.drawable.mesad);
                            }



                            ban = false;
                            break;
                        case "m3":
                            tratarMensaje(0, "Mesa 3", vector[1], 3);

                            if(vector[1].equals("o")){
                                mesa3.setImageResource(R.drawable.mesao);
                            }else {
                                mesa3.setImageResource(R.drawable.mesad);
                            }

                            ban = false;
                            break;
                        case "m4":
                            tratarMensaje(0,"Mesa 4",vector[1],4);
                            mesa4.setImageResource(R.drawable.mesao);

                            if(vector[1].equals("o")){
                                mesa4.setImageResource(R.drawable.mesao);
                            }else {
                                mesa4.setImageResource(R.drawable.mesad);
                            }

                            ban = false;
                            break;
                        case "m5":
                            tratarMensaje(0, "Mesa 5", vector[1], 5);
                            if(vector[1].equals("o")){
                                mesa5.setImageResource(R.drawable.mesao);
                            }else {
                                mesa5.setImageResource(R.drawable.mesad);
                            }


                            ban = false;
                            break;
                    }
                }
            }

            @Override
            public void onFinish() {
                timer.start();
            }
        };

        timer.start();
    }

    public boolean onCreateOptionsMenu(Menu m){
        this.getMenuInflater().inflate(R.menu.menumese, m);
        return super.onCreateOptionsMenu(m);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.menu:
                Intent abrirMenu = new Intent(PantallaPrinMesero.this,PantallaMenu.class);
                abrirMenu.putExtra("usu",0);
                startActivity(abrirMenu);
                break;
            case R.id.expord:
                //exportar ordenes
                break;
            default:
                //cerrar sesion
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Debe cerrar sesion primero.", Toast.LENGTH_SHORT).show();
    }



    public  void ocuparMesa(String mesa){

        //Toast.makeText(PantallaPrinMesero.this,"entró ocuparMesa",Toast.LENGTH_LONG);
        final String laMesa = mesa;
        String statusMesa="";

        switch (mesa){
            case "m1":
                numeroMesa="Mesa 1";
                //quitar va cuando se trate el mensaje
                break;
            case "m2":
                numeroMesa="Mesa 2";
                break;
            case "m3":
                numeroMesa="Mesa 3";
                break;
        }

        AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPrinMesero.this);
        alerta.setTitle("ATENCIÓN!")
                .setMessage("¿Está seguro que se ocupará la" + numeroMesa + "? ")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // String [] [] meseros = consultarMeseros();
                        consultarMeseros();
                        //Toast.makeText(PantallaPrinMesero.this,"meseros"+matriz.length,Toast.LENGTH_LONG).show();
                        for(int i = 0; i<matriz.length;i++){
                            Enviar_SMS sms = new Enviar_SMS(PantallaPrinMesero.this, laMesa + ",o", matriz[i][6]);
                            sms.execute();

                        }

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

    }

    public void consultarMeseros(){

        try{
            SQLiteDatabase base = conexion.getReadableDatabase();
            Cursor c = base.rawQuery("SELECT * FROM Usuario WHERE puesto = 'MESERO' AND tipoUsuario = 'A'",null);

            if(!c.moveToFirst()){

            }else{
                matriz = new String[c.getCount()][7];
                int contador = 0;
                do{
                    matriz[contador][0] = String.valueOf(c.getString(0));//idUsuario
                    matriz[contador][1] = String.valueOf(c.getString(1));//nombre
                    matriz[contador][2] = String.valueOf(c.getString(2));//Apat
                    matriz[contador][3] = String.valueOf(c.getString(3));//Amat
                    matriz[contador][4] = String.valueOf(c.getString(4));//pass
                    matriz[contador][5] = String.valueOf(c.getString(5));//puesto
                    matriz[contador][6] = String.valueOf(c.getString(6));//celular

                    //Toast.makeText(PantallaPrinMesero.this,matriz[contador] [6],Toast.LENGTH_LONG).show();

                    contador++;
                }while(c.moveToNext());
                base.close();
            }

        }catch(Exception e){
            AlertDialog.Builder m = new AlertDialog.Builder(PantallaPrinMesero.this);
            m.setMessage(e.getMessage()).show();
            //Toast.makeText(PantallaMenu.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //return null;
        }
        //return matriz;
    }


    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        unregisterReceiver(recibidorMensajsSMS);
    }


    public void onPause(){
        super.onPause();
    }

    public void onResume(){
        super.onResume();
    }

    public void tratarMensaje(int num,String mesa,String s, int numeroDMesa) {
        //Toast.makeText(PantallaPrinMesero.this, "Entró tratar sms;; num"+ num+" ese:"+s, Toast.LENGTH_SHORT).show();
        SQLiteDatabase base = conexion.getWritableDatabase();
        if (num == 0) {//significa que es mesa
            switch (s) {
                case "o":
                   /* AlertDialog.Builder a = new AlertDialog.Builder(PantallaPrinMesero.this);
                    a.setTitle("ENTRO Tratar mensaje").setMessage(vector[0] + " " + mesa + " está ocupada").show();*/

                    try{

                        base.execSQL("UPDATE Mesa SET disponibilidad='N' WHERE idMesa = "+numeroDMesa);


                       /* AlertDialog.Builder az = new AlertDialog.Builder(PantallaPrinMesero.this);
                        az.setTitle("ENTRO Tratar mensaje").setMessage(vector[0] + " " + mesa + " está ocupada").show();*/

                        AlertDialog.Builder a1 = new AlertDialog.Builder(PantallaPrinMesero.this);
                        a1.setMessage("Se actualizó").show();

                    }catch (SQLiteException e){
                        Toast.makeText(PantallaPrinMesero.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    break;
                case "d":
                    /*AlertDialog.Builder a2 = new AlertDialog.Builder(PantallaPrinMesero.this);
                    a2.setTitle("ENTRO Tratar mensaje").setMessage(vector[0] + " " + mesa + " está desocupada").show();*/
                    try{

                        base.execSQL("UPDATE Mesa SET disponibilidad='S' WHERE idMesa = " + numeroDMesa);
                        AlertDialog.Builder a4 = new AlertDialog.Builder(PantallaPrinMesero.this);
                        a4.setMessage("Se actualizó").show();


                    }catch (SQLiteException e){
                        Toast.makeText(PantallaPrinMesero.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
            base.close();

        }
    }


    public void consultaDisponible(int idMesa){
        try{
            SQLiteDatabase base = conexion.getReadableDatabase();
            Cursor c = base.rawQuery("SELECT disponibilidad FROM Mesa WHERE idMesa ="+idMesa, null);

            if(c.moveToFirst()){
                disponible = c.getString(0);
            }

            base.close();

        }catch (SQLiteException e){
            Toast.makeText(PantallaPrinMesero.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

