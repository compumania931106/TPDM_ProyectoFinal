package mx.edu.ittepic.tpdm_proyectofinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PantallaAddPersonal extends AppCompatActivity {
    String datos;
    EditText id, nombre,apaterno,amaterno,pass,num;
    Spinner puesto;
    Button aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_add_personal);

        id = (EditText) findViewById(R.id.editText6);
        nombre = (EditText) findViewById(R.id.editText7);
        apaterno = (EditText) findViewById(R.id.editText8);
        amaterno = (EditText) findViewById(R.id.editText9);
        pass = (EditText) findViewById(R.id.editText10);
        puesto = (Spinner) findViewById(R.id.spinner2);
        num = (EditText) findViewById(R.id.editText11);
        aceptar = (Button) findViewById(R.id.button4);

        datos = getIntent().getStringExtra("dato");

        if(!(datos.equals(""))){
            String[] datoRecibido = datos.split(",");
            id.setText(datoRecibido[0]);
            nombre.setText(datoRecibido[1]);
            apaterno.setText(datoRecibido[2]);
            amaterno.setText(datoRecibido[3]);
            pass.setText(datoRecibido[4]);
        }
    }
}
