package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Gabriela on 30/05/2016.
 */
public class Enviar_SMS extends AsyncTask <String,String,String>{
    Activity actividad;
    String mensaje;
    String celular;



    public Enviar_SMS(Activity activity, String sms, String numero){
        actividad = activity;
        mensaje = sms;
        celular = numero;
    }


    @Override
    protected String doInBackground(String... params) {



        return null;
    }

    protected  void onPostExecute (String res){

        SmsManager enviarSMS = SmsManager.getDefault();

        enviarSMS.sendTextMessage(celular,null,mensaje,null,null);
        Toast.makeText(actividad,"Se envio el SMS",Toast.LENGTH_SHORT).show();
    }
}
