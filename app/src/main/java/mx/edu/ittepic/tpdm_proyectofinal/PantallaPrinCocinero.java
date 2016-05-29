package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PantallaPrinCocinero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_pantalla_prin_cocinero);
    }

    public boolean onCreateOptionsMenu(Menu m){
        this.getMenuInflater().inflate(R.menu.menucoc, m);
        return super.onCreateOptionsMenu(m);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        return true;
    }
}
