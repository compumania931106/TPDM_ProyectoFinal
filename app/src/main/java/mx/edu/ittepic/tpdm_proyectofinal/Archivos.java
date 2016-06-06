package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by VictorManuel on 05/06/2016.
 */
public class Archivos {

    boolean sdDisponible;
    boolean sdAccesoEscritura;
    Context puntero;
    ConexionBD conexion;

    public Archivos(Context p) {
        conexion = new ConexionBD(p, "Restaurant",null,1);
        puntero = p;
        sdDisponible = false;
        sdAccesoEscritura = false;

        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            sdDisponible = true;
            sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            sdDisponible = true;
            sdAccesoEscritura = false;
        }
        else
        {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
    }

    public void escribirSD(String contenido){
        if(sdAccesoEscritura){
            try
            {
                File rutaSD = puntero.getExternalFilesDir(null);
                File f = new File(rutaSD.getAbsolutePath(),"info.txt");

                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write(contenido);
                fout.close();
            }
            catch (Exception ex)
            {
                Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
            }
        }else{
            Log.e("Ficheros", "tarjeta SD no disponible");
        }
    }

    public String leerSD(){
        String datos = "";
        if(sdDisponible){
            try{
                File rutaSD = puntero.getExternalFilesDir(null);
                File f = new File(rutaSD.getAbsolutePath(),"info.txt");

                BufferedReader fin =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(f)));

                datos = fin.readLine();
                fin.close();

            }catch (Exception e){
                Log.e("Ficheros", "Error al leer fichero a tarjeta SD");
            }
        }else{
            Log.e("Ficheros", "Error en la tarjeta SD, no se puede leer");
        }



        return datos;
    }

    public String llenarTitulo(){

        SQLiteDatabase base = conexion.getReadableDatabase();
        String[] contenido = leerSD().split(",");
        Cursor c = base.rawQuery("SELECT nombre, apellidoPaterno, apellidoMaterno FROM Usuario WHERE idUsuario = '"+ contenido[0] +"'",null);
        String titulo = "";

        if (c.moveToFirst()){
            titulo = c.getString(0) + " " + c.getString(1) + " " + c.getString(2);
        }
        return titulo;
    }
}
