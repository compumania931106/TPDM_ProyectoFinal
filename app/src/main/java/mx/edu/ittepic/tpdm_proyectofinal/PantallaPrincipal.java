package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class PantallaPrincipal extends Activity {
    EditText usu,pas;
    String usuario,password;
    Button entrar;
    ConexionBD conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_principal);

        usu = (EditText) findViewById(R.id.editText);
        pas = (EditText) findViewById(R.id.editText2);
        entrar = (Button) findViewById(R.id.button);
        conexion = new ConexionBD(this, "Restaurant",null,1);


        usu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                usu.setText(usu.getText().toString().toUpperCase());

            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = usu.getText().toString();
                password = pas.getText().toString();
                if(!(usuario.equals("") && password.equals(""))){
                    //Toast.makeText(PantallaPrincipal.this,usuario,Toast.LENGTH_LONG).show();
                    try{
                        ConexionWeb con = new ConexionWeb(PantallaPrincipal.this,1);
                        con.usuario = usuario;
                        con.agregarVariables("usu",usu.getText().toString());
                        con.agregarVariables("pas", pas.getText().toString());
                        con.execute(new URL("http://ittepic-tpdm.6te.net/login.php"));
                    }catch(MalformedURLException e){
                        AlertDialog.Builder alert = new AlertDialog.Builder(PantallaPrincipal.this);
                        alert.setTitle("Error!!");
                        alert.setMessage(e.getMessage());
                        alert.show();
                    }

                }else{
                    Toast.makeText(PantallaPrincipal.this,"Por favor ingrese los datos que se requirem",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    public void onBackPressed() {

    }



    public String getUsuario() {
        return usuario;
    }


}
