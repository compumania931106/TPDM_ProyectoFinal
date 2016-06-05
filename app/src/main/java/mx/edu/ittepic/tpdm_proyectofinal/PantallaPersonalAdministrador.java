package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class PantallaPersonalAdministrador extends AppCompatActivity {
    ListView listaPersonal;
    String contenido;
    ConexionBD conexion;
    String[][] mPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_personal_administrador);
        this.setTitle("");
        listaPersonal = (ListView) findViewById(R.id.listView2);
        conexion = new ConexionBD(PantallaPersonalAdministrador.this,"Restaurant",null,1);
        llenarLista();

        listaPersonal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String[] opciones = {"Editar", "Eliminar", "Cancelar"};
                AlertDialog.Builder alert = new AlertDialog.Builder(PantallaPersonalAdministrador.this);
                alert.setTitle("Opciones");
                //alert.setMessage("Seleccione la opcion que desea realizar");
                alert.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent pantallaadd = new Intent(PantallaPersonalAdministrador.this, PantallaAddPersonal.class);
                                String dato = mPersonal[position][0] + "," + mPersonal[position][1] + "," + mPersonal[position][2] + "," + mPersonal[position][3] + "," + mPersonal[position][4] + "," + mPersonal[position][5] + "," + mPersonal[position][6];
                                pantallaadd.putExtra("dato", dato);
                                startActivity(pantallaadd);
                                break;
                            case 1:
                                final AlertDialog.Builder eliminiar = new AlertDialog.Builder(PantallaPersonalAdministrador.this);
                                eliminiar.setTitle("Atencion!!");
                                eliminiar.setMessage("¿Desea elimiar a esta persona?");
                                eliminiar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            SQLiteDatabase base = conexion.getWritableDatabase();
                                            base.execSQL("UPDATE Usuario SET tipoUsuario = 'I' WHERE idUsuario = '" + mPersonal[position][0] + "'");
                                            llenarLista();
                                            Toast.makeText(PantallaPersonalAdministrador.this, "Registro eliminado exitasamente", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(PantallaPersonalAdministrador.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                eliminiar.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                eliminiar.show();
                                break;
                            default:
                        }
                    }
                }).create().show();
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu m){
        this.getMenuInflater().inflate(R.menu.menuagregarpersonal, m);
        return super.onCreateOptionsMenu(m);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch(mi.getItemId()){
            case R.id.add:
                Intent add = new Intent(PantallaPersonalAdministrador.this, PantallaAddPersonal.class);
                add.putExtra("dato","");
                startActivity(add);
                break;
        }
        return true;
    }

    private void llenarLista(){
        mPersonal = buscarPersonal();
        if(mPersonal == null){
            Toast.makeText(PantallaPersonalAdministrador.this, "No hay personal disponible",Toast.LENGTH_SHORT).show();
        }else{
            String[] vector = new String[mPersonal.length];

            for(int i = 0; i<vector.length; i++){
                vector[i] =  mPersonal[i][1] + " " + mPersonal[i][2] + " " + mPersonal[i][3] + " (" + mPersonal[i][5] + ")";
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    PantallaPersonalAdministrador.this,android.R.layout.simple_list_item_1,vector
            );
            listaPersonal.setAdapter(adapter);
        }
    }

    private String[][] buscarPersonal(){
        String[][] matriz = null;
        try{
            SQLiteDatabase base = conexion.getReadableDatabase();
            Cursor c = base.rawQuery("SELECT * FROM Usuario WHERE tipoUsuario = 'A'",null);

            if(!c.moveToFirst()){
                return null;
            }else{
                matriz = new String[c.getCount()][8];
                int contador = 0;
                do{
                    matriz[contador][0] = c.getString(0);
                    matriz[contador][1] = c.getString(1);
                    matriz[contador][2] = c.getString(2);
                    matriz[contador][3] = c.getString(3);
                    matriz[contador][4] = c.getString(4);
                    matriz[contador][5] = c.getString(5);
                    matriz[contador][6] = c.getString(6);
                    matriz[contador][7] = c.getString(7);
                    contador ++;
                }while(c.moveToNext());
                base.close();
            }
        }catch(Exception e){
            Toast.makeText(PantallaPersonalAdministrador.this,e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }
        return matriz;
    }

    private String obtenerTablaUsuario(){
        String contenido = "";
        SQLiteDatabase base = conexion.getReadableDatabase();
        Cursor c = base.rawQuery("SELECT * FROM Usuario",null);

        if(!c.moveToFirst()){
            return null;
        }else{
            do{
                contenido = contenido + "'" +c.getString(0) + "','" + c.getString(1) + "','" + c.getString(2) + "','" +
                         c.getString(3) + "','" + c.getString(4) + "','" + c.getString(5) + "','" + c.getString(6) + "','" +
                         c.getString(7) + "';";
            }while(c.moveToNext());
            base.close();
        }
        contenido = contenido.substring(0,contenido.length()-1);
        return contenido;
    }

    public void onBackPressed() {
        ConexionWeb con = new ConexionWeb(PantallaPersonalAdministrador.this,3);
        //Toast.makeText(PantallaPersonalAdministrador.this, obtenerTablaUsuario(), Toast.LENGTH_LONG).show();
        con.agregarVariables("dat", obtenerTablaUsuario());
        try {
            con.execute(new URL("http://ittepic-tpdm.6te.net/proyectofinal/exportarusuario.php"));
        }catch(MalformedURLException e){
            Toast.makeText(PantallaPersonalAdministrador.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Intent regreso = new Intent(PantallaPersonalAdministrador.this,PantallaPrinAdministrador.class);
        startActivity(regreso);
    }
}
