package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PantallaOrdenes extends AppCompatActivity {

    ListView lista;
    int idPedido = 0;
    int idMesa = 0;
    ConexionBD conexion;
    String[][] contenido;
    String item = "";
    String cantidad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_ordenes);
        idMesa = getIntent().getExtras().getInt("id");
        lista = (ListView)findViewById(R.id.listView4);

        idPedido = getIntent().getIntExtra("id",0);

        Toast.makeText(PantallaOrdenes.this, idPedido + "", Toast.LENGTH_SHORT).show();

        conexion = new ConexionBD(PantallaOrdenes.this,"Restaurant",null,1);

        llenarLista();

    }


    public void llenarLista(){
        contenido = buscarDetallePedido();
        if(contenido == null){
            Toast.makeText(PantallaOrdenes.this,"No hay items en este pedido",Toast.LENGTH_SHORT).show();
        }else{

            String[] vector = new String[contenido.length];

            for(int i = 0; i<vector.length; i++){
                SQLiteDatabase base = conexion.getReadableDatabase();

                Cursor c = base.rawQuery("SELECT nombre FROM Menu WHERE idItem = " + contenido[i][2] + "", null);
                if(c.moveToFirst()){
                    item = c.getString(0);
                }

                cantidad = contenido[i][3];

                base.close();
                vector[i] = item + " (" + cantidad + ")";
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    PantallaOrdenes.this,android.R.layout.simple_list_item_1,vector
            );

            lista.setAdapter(adapter);
        }
    }

    private String[][] buscarDetallePedido(){
        String[][] matriz = null;
        try{
            SQLiteDatabase base = conexion.getReadableDatabase();
            Cursor c = base.rawQuery("SELECT * FROM DetalleOrden WHERE idOrden = "+ idPedido +"",null);

            if(!c.moveToFirst()){
                return null;
            }else{
                matriz = new String[c.getCount()][4];
                int contador = 0;
                do{
                    matriz[contador][0] = String.valueOf(c.getInt(0));
                    matriz[contador][1] = String.valueOf(c.getInt(1));
                    matriz[contador][2] = String.valueOf(c.getInt(2));
                    matriz[contador][3] = String.valueOf(c.getInt(3));
                    contador++;
                }while(c.moveToNext());
                base.close();
            }

        }catch(Exception e){
            Toast.makeText(PantallaOrdenes.this, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
        return matriz;
    }
    public boolean onCreateOptionsMenu(Menu m){
        this.getMenuInflater().inflate(R.menu.menumesero, m);
        return super.onCreateOptionsMenu(m);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch(mi.getItemId()){
            case R.id.additem:
                Intent ventanaItem = new Intent(PantallaOrdenes.this, PantallaAddItem.class);
                ventanaItem.putExtra("id",idMesa);
                startActivity(ventanaItem);
                break;
        }
        return true;
    }
}
