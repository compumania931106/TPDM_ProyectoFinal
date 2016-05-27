package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Splash extends AppCompatActivity {
    Lienzo lienzo;

    int contador;
    CountDownTimer timer;
    int x=0, y=0;
    int cx1 = -300, cy1 = -400, cx2 = -300, cy2 = -400,cx3 = -300, cy3 = -400,cx4 = -300, cy4 = -400, cx5 = -300, cy5 = -400,
            cx6 = -300, cy6 = -400, cx7 = -300, cy7 = -400, cx8 = -300, cy8 = -400, cx9 = -300, cy9 = -400;
    int alpha=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setTitle("");
        lienzo = new Lienzo(this);
        setContentView(lienzo);

        contador = 0;
        timer = new CountDownTimer(18000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(alpha<255){
                    alpha+=80;
                    lienzo.invalidate();
                }else{
                    alpha-=50;
                    lienzo.invalidate();
                }


            }

            @Override
            public void onFinish() {

                timer.cancel();
                Intent abrir = new Intent(Splash.this,PantallaPrincipal.class);
                startActivity(abrir);
            }
        };

        timer.start();

    }



    public class Lienzo extends View {

        Bitmap logo;

        public Lienzo(Context context) {
            super(context);

            logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);



        }

        public void onDraw(Canvas canvas) {
            Paint p = new Paint();
            canvas.drawColor(Color.argb(202, 233, 218, 171));

            x= (canvas.getWidth() / 2) - (logo.getWidth() / 2);
            y= (canvas.getHeight() / 2) - (logo.getHeight() / 2);

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
}

