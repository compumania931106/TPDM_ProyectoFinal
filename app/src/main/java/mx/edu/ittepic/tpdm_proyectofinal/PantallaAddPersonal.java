package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaAddPersonal extends AppCompatActivity {
    String datos;
    TextView titulo;
    EditText id, nombre,apaterno,amaterno,pass,num;
    Spinner puesto;
    Button aceptar;
    Boolean decision;
    ConexionBD conexion;
    Archivos archivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_add_personal);

        archivos = new Archivos(PantallaAddPersonal.this);
        titulo = (TextView) findViewById(R.id.textView9);
        titulo.setText(archivos.llenarTitulo());

        conexion = new ConexionBD(this, "Restaurant",null,1);

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
            decision = true;
            id.setEnabled(false);
            String[] datoRecibido = datos.split(",");
            id.setText(datoRecibido[0]);
            nombre.setText(datoRecibido[1]);
            apaterno.setText(datoRecibido[2]);
            amaterno.setText(datoRecibido[3]);
            pass.setText(datoRecibido[4]);
            switch(datoRecibido[5]){
                case "ADMINISTRADOR":
                    puesto.setSelection(0);
                    break;
                case "MESERO":
                    puesto.setSelection(1);
                    break;
                case "COCINERO":
                    puesto.setSelection(2);
                    break;
                default:
                    puesto.setSelection(3);
            }
            num.setText(datoRecibido[6]);
        }
        else{
            decision = false;
            pass.setEnabled(false);
            id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    id.setText(id.getText().toString().toUpperCase());
                    pass.setText(id.getText().toString().toUpperCase());
                }
            });
        }

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cid = id.getText().toString().toUpperCase();
                final String cnombre = nombre.getText().toString().toUpperCase();
                final String capaterno = apaterno.getText().toString().toUpperCase();
                final String camaterno = amaterno.getText().toString().toUpperCase();
                final String cpass = pass.getText().toString().toUpperCase();
                final String cpuesto = puesto.getSelectedItem().toString().toUpperCase();
                final String cnum = num.getText().toString().toUpperCase();

                if(cid.equals("") || cnombre.equals("") || capaterno.equals("") || camaterno.equals("") || cpass.equals("") || cnum.equals("")){
                    Toast.makeText(PantallaAddPersonal.this, "Por favor capture los datos que se le pide.", Toast.LENGTH_SHORT).show();
                }else if(cnum.length() != 10){
                    Toast.makeText(PantallaAddPersonal.this, "Por favor capture los datos que se le pide.", Toast.LENGTH_SHORT).show();
                }else {
                    if (decision){
                        AlertDialog.Builder confirmacion = new AlertDialog.Builder(PantallaAddPersonal.this);
                        confirmacion.setTitle("Confirme la contraseña");
                        confirmacion.setMessage("Ingrese de nuevo la contraseña");
                        final EditText contra = new EditText(PantallaAddPersonal.this);
                        contra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        confirmacion.setView(contra);
                        confirmacion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (contra.getText().toString().equals(cpass)) {

                                    try {
                                        SQLiteDatabase base = conexion.getWritableDatabase();
                                        base.execSQL("UPDATE Usuario SET nombre = '" + cnombre + "', " +
                                                "apellidoPaterno = '" + capaterno + "', apellidoMaterno = '" + camaterno + "', password = " +
                                                "'" + cpass + "', puesto = '" + cpuesto + "', numCelular = '" + cnum + "' WHERE idUsuario = '" + cid + "'");
                                        Toast.makeText(PantallaAddPersonal.this, "Registro actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(PantallaAddPersonal.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
                        confirmacion.show();
                    }
                    else{
                        if(cid.length() != 10){
                            Toast.makeText(PantallaAddPersonal.this, "Ingrese una clave valida", Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                SQLiteDatabase base = conexion.getWritableDatabase();
                                base.execSQL("INSERT INTO Usuario VALUES ('"+ cid +"', '"+ cnombre +"', '"+ capaterno +"', '"+ camaterno +"'," +
                                        "'"+ cpass +"', '"+ cpuesto +"', '"+ cnum +"','A')");
                                Toast.makeText(PantallaAddPersonal.this, "Registro ingresado correctamente", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                Toast.makeText(PantallaAddPersonal.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }}
        });
    }

    public void onBackPressed() {
        Intent regreso = new Intent(PantallaAddPersonal.this,PantallaPersonalAdministrador.class);
        startActivity(regreso);
    }
}
