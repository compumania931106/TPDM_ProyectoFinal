package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class PantallaPrinAdministrador extends AppCompatActivity {
    ListView menuAdmin;
    TextView titulo;
    Archivos archivos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("");
        setContentView(R.layout.activity_pantalla_prin_administrador);

        archivos = new Archivos(PantallaPrinAdministrador.this);
        titulo = (TextView) findViewById(R.id.textView19);
        titulo.setText(archivos.llenarTitulo());

        menuAdmin = (ListView) findViewById(R.id.listView);

        menuAdmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent personal = new Intent(PantallaPrinAdministrador.this, PantallaPersonalAdministrador.class);
                        startActivity(personal);
                        break;
                    case 1:
                        Intent abrirMenu2 = new Intent(PantallaPrinAdministrador.this, PantallaMenu.class);
                        abrirMenu2.putExtra("usu", 1);
                        startActivity(abrirMenu2);
                        break;
                    default:
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu m){
        this.getMenuInflater().inflate(R.menu.menuadm, m);
        return super.onCreateOptionsMenu(m);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()){
            case R.id.logout:
                Intent login = new Intent(PantallaPrinAdministrador.this,PantallaPrincipal.class);
                startActivity(login);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Debe cerrar sesion primero.",Toast.LENGTH_SHORT).show();
    }



}
