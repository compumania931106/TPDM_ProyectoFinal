package mx.edu.ittepic.tpdm_proyectofinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PantallaAddPersonal extends AppCompatActivity {
String datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_add_personal);

        datos = getIntent().getStringExtra("dato");
        Toast.makeText(PantallaAddPersonal.this, datos,Toast.LENGTH_SHORT).show();
    }
}
