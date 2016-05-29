package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class PantallaMenu extends AppCompatActivity {
    ConexionBD conexion;
    Spinner categorias;
    String Platillos[] [];
    String[][] matriz = null;

    Button aceptar;
    ListView lista;

    final String[] datos =
            new String[]{"Entradas","Platos fuertes","Comida rápida"," Bebidas"," Postres"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_menu);

        categorias = (Spinner) findViewById(R.id.spinner);
        aceptar = (Button) findViewById(R.id.button3);
        conexion = new ConexionBD(this, "Restaurant",null,1);
        lista = (ListView) findViewById(R.id.listView3);


        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, datos);

        categorias.setAdapter(adaptador);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder item = new AlertDialog.Builder(PantallaMenu.this);

                switch (categorias.getSelectedItemPosition()){
                    case 0:
                        llenarLista(1);
                        break;
                    case 1:
                       llenarLista(2);
                        break;
                    case 2:
                        llenarLista(3);
                        break;
                    case 3:
                        llenarLista(4);
                        break;
                    default:
                        llenarLista(5);
                }
            }
        });

    }

    public void llenarLista(int num){

        Platillos = buscarMenu(num);
        if(Platillos == null){
            Toast.makeText(PantallaMenu.this,"No hay categorias",Toast.LENGTH_SHORT).show();
        }else{

            String[] vector = new String[Platillos.length];

            for(int i = 0; i<vector.length; i++){

                vector[i] = matriz[i][2]+"\n "+matriz[i][3]+"    $"+ matriz [i][4];
            }

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                    PantallaMenu.this,android.R.layout.simple_list_item_1,vector
            );

            lista.setAdapter(adapter2);
        }
    }


    private String[][] buscarMenu(int id){

        try{
            SQLiteDatabase base = conexion.getReadableDatabase();
            Cursor c = base.rawQuery("SELECT * FROM Menu WHERE idCategoria = "+id,null);

            if(!c.moveToFirst()){
                return null;
            }else{
                matriz = new String[c.getCount()][6];
                int contador = 0;
                do{
                    matriz[contador][0] = String.valueOf(c.getInt(0));//idItem
                    matriz[contador][1] = String.valueOf(c.getInt(1));//idCateg
                    matriz[contador][2] = String.valueOf(c.getString(2));//nombre
                    matriz[contador][3] = String.valueOf(c.getString(3));//desc
                    matriz[contador][4] = String.valueOf(c.getFloat(4));//precio
                    matriz[contador][5] = String.valueOf(c.getString(5));//disponi

                    contador++;
                }while(c.moveToNext());
                base.close();
            }

        }catch(Exception e){
            AlertDialog.Builder m = new AlertDialog.Builder(PantallaMenu.this);
            m.setMessage(e.getMessage()).show();
            //Toast.makeText(PantallaMenu.this, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
        return matriz;
    }


}
