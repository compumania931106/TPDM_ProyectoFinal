package mx.edu.ittepic.tpdm_proyectofinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class PantallaCambiarPass extends AppCompatActivity {

    EditText passV,passN,comprobar;
    String pv,pn,pc;
    Button cambiar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_cambiar_pass);

        passV = (EditText) findViewById(R.id.editText3);
        passN = (EditText) findViewById(R.id.editText4);
        comprobar = (EditText) findViewById(R.id.editText5);
        cambiar = (Button) findViewById(R.id.button2);

        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pv = passV.getText().toString();
                pn = passN.getText().toString();
                pc = comprobar.getText().toString();

                if(!(pv.equals("") && pn.equals("") && pc.equals(""))){
                    if(pn.equals(pc)){
                        try{
                            ConexionWeb con = new ConexionWeb(PantallaCambiarPass.this,2);
                            con.agregarVariables("passV",passV.getText().toString());
                            con.agregarVariables("passN", passN.getText().toString());
                            con.execute(new URL("http://ittepic-tpdm.6te.net/change.php"));
                        }catch(MalformedURLException e){
                            Toast.makeText(PantallaCambiarPass.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
