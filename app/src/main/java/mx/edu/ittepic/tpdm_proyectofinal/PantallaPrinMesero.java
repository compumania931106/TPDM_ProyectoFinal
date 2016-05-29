package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class PantallaPrinMesero extends AppCompatActivity {
    ImageView mesa1, mesa2, mesa3, mesa4, mesa5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_prin_mesero);

        mesa1 = (ImageView) findViewById(R.id.imageView);
        mesa2 = (ImageView) findViewById(R.id.imageView2);
        mesa3 = (ImageView) findViewById(R.id.imageView3);
        mesa4 = (ImageView) findViewById(R.id.imageView4);
        mesa5 = (ImageView) findViewById(R.id.imageView5);

        mesa1.setImageResource(R.drawable.mesad);
        mesa2.setImageResource(R.drawable.mesad);
        mesa3.setImageResource(R.drawable.mesad);
        mesa4.setImageResource(R.drawable.mesad);
        mesa5.setImageResource(R.drawable.mesad);
    }

    public boolean onCreateOptionsMenu(Menu m){
        this.getMenuInflater().inflate(R.menu.menumese, m);
        return super.onCreateOptionsMenu(m);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.actmesa:
                break;
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





}
