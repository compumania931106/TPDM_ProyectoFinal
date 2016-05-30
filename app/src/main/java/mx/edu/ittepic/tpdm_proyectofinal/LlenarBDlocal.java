package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VictorManuel on 29/05/2016.
 */
public class LlenarBDlocal extends AsyncTask<URL,String,String>{
    List<String []> variables;
    Activity puntero;
    ConexionBD conexion;
    String ventana;
    ProgressDialog progressDialog;

    public LlenarBDlocal(Activity p, String ventana){
        variables = new ArrayList<String[]>();
        puntero = p;
        this.ventana = ventana;
    }

    public void agregarVariables(String identificador,String dato){
        String [] temp= {identificador,dato};
        variables.add(temp);
    }

    @Override
    protected void onPreExecute() {
        conexion = new ConexionBD(puntero, "Restaurant",null,1);
        progressDialog = new ProgressDialog(puntero);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Obteniendo los datos desde el servidor");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(URL... params) {
        HttpURLConnection conexion= null;
        String datosPost="";
        String respuesta = "";

        try{
            //Creación de cadena clave=valor&clave=valor...etc
            for(int i =0; i< variables.size();i++){
                String[] temp = variables.get(i);
                datosPost+=temp[0]+"="+ URLEncoder.encode(temp[1], "UTF-8")+" ";//enconce(Cadena, formato)
            }
            datosPost = datosPost.trim();
            datosPost = datosPost.replaceAll(" ", "&");

            conexion = (HttpURLConnection) params [0].openConnection();//conexion

            conexion.setDoOutput(true);
            conexion.setFixedLengthStreamingMode(datosPost.length());//tamaño del datagrama/cantidad de datos a enviar
            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            //Flujos
            OutputStream flujoEnvioDatos = new BufferedOutputStream(conexion.getOutputStream());

            flujoEnvioDatos.write(datosPost.getBytes());//puede generar IOException
            flujoEnvioDatos.flush();//Forzador de Envio de datos
            flujoEnvioDatos.close();

            if(conexion.getResponseCode() == 200){ //el 200 significa que si recibió el host los datos enviados y procesó respuesta
                InputStreamReader flujoLectura = new InputStreamReader(conexion.getInputStream(),"UTF-8");//verdadero flujo de uno por uno
                BufferedReader lector = new BufferedReader(flujoLectura);//linea a linea

                String linea = "";
                do{
                    linea = lector.readLine();
                    if(linea!=null){
                        respuesta += linea;
                    }
                }while(linea!=null);
            }else{
                respuesta="Error_404";
            }
        }catch (UnknownHostException uke){//cuando no encuentra el Url
            return  "Error_404_1";
        }catch (IOException ioe){//para el envío de datos
            return  "Error_404_2";
        }finally {
            //liberar recurso
            if(conexion!=null){
                conexion.disconnect();
            }
        }
        return respuesta;

    }

    protected void onPostExecute(String res){
        if(res.length() < 15){
            Toast.makeText(puntero,"Problemas con la conexion",Toast.LENGTH_SHORT).show();
        }else {
            SQLiteDatabase base = conexion.getWritableDatabase();
            base.execSQL("DELETE FROM Categoria");
            base.execSQL("DELETE FROM Menu");
            base.execSQL("DELETE FROM DetallePago");
            base.execSQL("DELETE FROM Usuario");

            String[] tablas = res.split("%");

            progressDialog.setMessage("Exportando los datos a su base de datos local");
            for(int i = 0; i<tablas.length;i++){
                String[] datos = tablas[i].split(";");
                for(int j = 0; j < datos.length; j++){
                    switch(i){
                        case 0:
                            base.execSQL("INSERT INTO Categoria VALUES (" + datos[j].replaceAll("-", ",") + ")");
                            //Toast.makeText(puntero,"Tabla 0 " + datos[j].replaceAll("-",","),Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            base.execSQL("INSERT INTO Menu VALUES (" + datos[j].replaceAll("-", ",") + ")");
                            //Toast.makeText(puntero,"Tabla 1 " + datos[j].replaceAll("-",","),Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            base.execSQL("INSERT INTO DetallePago VALUES (" + datos[j].replaceAll("-", ",") + ")");
                            //Toast.makeText(puntero,"Tabla 2 " + datos[j].replaceAll("-",","),Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            base.execSQL("INSERT INTO Usuario VALUES (" + datos[j].replaceAll("-", ",") + ")");
                            //Toast.makeText(puntero,"Tabla 3 " + datos[j].replaceAll("-",","),Toast.LENGTH_SHORT).show();
                    }

                }
            }
            progressDialog.dismiss();

            switch (ventana){
                case "ADMINISTRADOR":
                    Intent adm = new Intent(puntero,PantallaPrinAdministrador.class);
                    puntero.startActivity(adm);
                    break;
                case "CAJERO":
                    Intent adc = new Intent(puntero,PantallaPrinCajero.class);
                    puntero.startActivity(adc);
                    break;
                case "MESERO":
                    Intent admm = new Intent(puntero,PantallaPrinMesero.class);
                    puntero.startActivity(admm);
                    break;
                default:
                    Intent adcoc = new Intent(puntero,PantallaPrinCocinero.class);
                    puntero.startActivity(adcoc);
            }
        }


    }
}
