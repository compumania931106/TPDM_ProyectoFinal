package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Splash extends Activity {
    Lienzo lienzo;


    CountDownTimer timer;
    int x=0, y=0,alpha=0 , xr=0, yr=0,contador=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        lienzo = new Lienzo(this);
        setContentView(lienzo);

        contador = 0;
        timer = new CountDownTimer(6000,200) {
            @Override
            public void onTick(long millisUntilFinished) {
                contador++;

                if(contador == 1 | contador == 19){
                    lienzo.puntos = BitmapFactory.decodeResource(getResources(),R.drawable.low);
                    lienzo.invalidate();
                }

                if(contador== 7 | contador == 25){
                    lienzo.puntos = BitmapFactory.decodeResource(getResources(),R.drawable.medium);
                    lienzo.invalidate();
                }

                if(contador == 13| contador == 30){
                    lienzo.puntos = BitmapFactory.decodeResource(getResources(),R.drawable.end);
                    lienzo.invalidate();
                }

                if(alpha<255){
                    alpha+=8;
                    lienzo.invalidate();
                }



            }

            @Override
            public void onFinish() {
                timer.cancel();

                if (!verificaConexion(Splash.this)) {
                    AlertDialog.Builder mensaje = new AlertDialog.Builder(Splash.this);
                    mensaje.setTitle("ERROR").setMessage("Conexión a Internet fallida, ¿Desea continuar?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent abrir = new Intent(Splash.this,PantallaPrincipal.class);
                                    abrir.putExtra("inter",0);
                                    startActivity(abrir);
                                    dialogInterface.dismiss();
                                }
                            }).
                            setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onFinish();
                                    dialog.cancel();
                                }
                            })
                            .show();

                }else{
                    Intent abrir = new Intent(Splash.this,PantallaPrincipal.class);
                    abrir.putExtra("inter",1);
                    startActivity(abrir);
                }

            }
        };

        timer.start();

    }



    public class Lienzo extends View {

        Bitmap logo,puntos;

        public Lienzo(Context context) {
            super(context);

            logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            puntos = BitmapFactory.decodeResource(getResources(),R.drawable.low);



        }

        public void onDraw(Canvas canvas) {
            Paint p = new Paint();
            canvas.drawColor(Color.argb(202, 233, 218, 171));

            x= (canvas.getWidth() / 2) - (logo.getWidth() / 2);
            y= (canvas.getHeight() / 2) - (logo.getHeight() / 2);

            xr = (canvas.getWidth()/2) - (puntos.getWidth() / 2);
            yr = (canvas.getHeight()/2) - (puntos.getHeight() / 2) + (logo.getHeight() / 2) + 20;

            p.setTextSize(30);
            canvas.drawText(contador+"",78,90,p);
            canvas.drawBitmap(puntos,xr,yr,p);
            p.setAlpha(alpha);
            canvas.drawBitmap(logo, x, y, p);





        }

    }

    public void onPause(){
        super.onPause();
    }

    public void onResume(){
        super.onResume();
    }

    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
    }


    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }
}

