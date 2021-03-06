package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PantallaAddItem extends AppCompatActivity {
    Spinner categorias;
    ListView lista;
    int idCategoria = 0;
    int mesaID = 0;
    String[][] aMenu;
    Archivos archivos;
    ConexionBD conexion;
    int idOrden = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_add_item);
        archivos = new Archivos(this);
        conexion = new ConexionBD(PantallaAddItem.this,"Restaurant",null,1);
        mesaID = getIntent().getExtras().getInt("id");
        AlertDialog.Builder entrada = new AlertDialog.Builder(PantallaAddItem.this);
        entrada.setTitle("Confirme su nombre de usuario");
        entrada.setMessage("Ingrese su nombre de usuario");




        categorias = (Spinner) findViewById(R.id.spinner3);
        lista = (ListView) findViewById(R.id.listView5);

        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                llenarLista((position + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idCategoria = categorias.getSelectedItemPosition() + 1;
                final int idItem = Integer.parseInt(aMenu[position][0]);

                AlertDialog.Builder alert = new AlertDialog.Builder(PantallaAddItem.this);
                alert.setTitle("¡Atencion!");
                alert.setMessage("Ingrese la cantidad");
                final EditText cam = new EditText(PantallaAddItem.this);
                alert.setView(cam);
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idUsuario[] = archivos.leerSD().split(",");
                        Date fecha = new Date();
                        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(Calendar.getInstance().getTime());
                        String horaActual = new SimpleDateFormat("HH:MM:SS", java.util.Locale.getDefault()).format(Calendar.getInstance().getTime());
                        try {
                            SQLiteDatabase base = conexion.getWritableDatabase();
                            Cursor c = base.rawQuery("SELECT * FROM Orden WHERE idMesa = " + mesaID + " AND idUsuario = '" + idUsuario[0] + "' AND status = 'P'", null);

                            if (c.moveToLast()) {
                                base.execSQL("INSERT INTO DetalleOrden (idOrden, idItem, cantidad) VALUES ("+ c.getInt(0) +","+idItem+","+ Integer.parseInt(cam.getText().toString()) +")");
                            } else {

                                base.execSQL("INSERT INTO Orden (idMesa, idUsuario, fechaPedido, horaPedido, status) VALUES (" + mesaID + ", '" + idUsuario[0] + "', '" + fechaActual + "', '" + horaActual + "', 'P')");

                                Cursor id = base.rawQuery("SELECT idOrden FROM Orden WHERE idOrden = (SELECT MAX(idOrden) FROM Orden)", null);
                                if (id.moveToFirst()) {
                                   idOrden = id.getInt(0);
                                }


                                base.execSQL("INSERT INTO DetalleOrden (idOrden, idItem, cantidad) VALUES ("+ idOrden +","+idItem+","+ Integer.parseInt(cam.getText().toString()) +")");
                            }


                            base.close();
                            //Toast.makeText(PantallaAddItem.this, fechaActual + " : " + horaActual, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(PantallaAddItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alert.show();
            }
        });
    }

    private void llenarLista(int id){
        aMenu = obtenerItems(id);

        if(aMenu == null){
            Toast.makeText(PantallaAddItem.this,"No hay productos en este pedido",Toast.LENGTH_SHORT).show();
        }else{
            String[] vector = new String[aMenu.length];
            for(int i = 0; i < vector.length; i++){
                vector[i] = aMenu[i][1] + "($" + aMenu[i][2] + ")";
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    PantallaAddItem.this,android.R.layout.simple_list_item_1,vector
            );

            lista.setAdapter(adapter);
        }
    }

    private String[][] obtenerItems(int idCategoria){
        String[][] matriz = null;
        try{
            SQLiteDatabase base = conexion.getReadableDatabase();
            Cursor c = base.rawQuery("SELECT idItem, nombre, precio FROM Menu WHERE idCategoria = " + idCategoria + " AND disponibilidad = 'S'", null);

            if(!c.moveToFirst()){
                return null;
            }else{
                matriz = new String[c.getCount()][3];
                int contador = 0;
                do{
                    matriz[contador][0] = String.valueOf(c.getInt(0));
                    matriz[contador][1] = c.getString(1);
                    matriz[contador][2] = String.valueOf(c.getInt(2));

                    contador++;
                }while(c.moveToNext());
                base.close();
            }

        }catch(Exception e){
            Toast.makeText(PantallaAddItem.this,e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }
        return matriz;
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(PantallaAddItem.this,PantallaOrdenes.class);
        back.putExtra("id",idOrden);
        startActivity(back);
    }
}
