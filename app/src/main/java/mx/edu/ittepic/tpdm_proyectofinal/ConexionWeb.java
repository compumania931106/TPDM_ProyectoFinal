package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriela on 27/04/2016.
 */

public class ConexionWeb extends AsyncTask<URL,String,String> {
    List<String []> variables;
    Activity puntero;
    int tipo;
    String usuario;


    public ConexionWeb(Activity p, int tipo){
        variables = new ArrayList<String[]>();
        this.tipo = tipo;
        puntero = p;
    }



    public void agregarVariables(String identificador,String dato){
        String [] temp= {identificador,dato};
        variables.add(temp);
    }

    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection conexion= null;
        String datosPost="";
        String respuesta = "";

        try{
            //Creación de cadena clave=valor&clave=valor...etc
            for(int i =0; i< variables.size();i++){
                String[] temp = variables.get(i);
                datosPost+=temp[0]+"="+ URLEncoder.encode(temp[1],"UTF-8")+" ";//enconce(Cadena, formato)
            }
            datosPost = datosPost.trim();
            datosPost = datosPost.replaceAll(" ", "&");

            conexion = (HttpURLConnection) urls [0].openConnection();//conexion

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
        // El dato TIPO, ventana PantallaPrincipal = 1;
        //                       PantallaCambiarPass = 2;

        if(tipo == 1){
            if(res.equals("1")){
                Intent i = new Intent(puntero,PantallaCambiarPass.class);
                puntero.startActivity(i);
            }
            if(res.equals("ADMINISTRADOR")){
                ejecutarBD("ADMINISTRADOR");
            }
            if(res.equals("CAJERO")){
                ejecutarBD("CAJERO");
            }
            if(res.equals("MESERO")){
                ejecutarBD("MESERO");
            }
            if(res.equals("COCINERO")){
                ejecutarBD("COCINERO");
            }
        }
        if(tipo == 2){
            Toast.makeText(puntero,res,Toast.LENGTH_SHORT).show();
            if(res.length() == 44){
                Intent log = new Intent(puntero,PantallaPrincipal.class);
                puntero.startActivity(log);
            }
        }
        if(tipo == 3){
            Toast.makeText(puntero, res, Toast.LENGTH_SHORT).show();
        }
    }

    private void ejecutarBD(String ventana){
        LlenarBDlocal ll = new LlenarBDlocal(puntero,ventana);
        try{
            ll.execute(new URL("http://ittepic-tpdm.6te.net/proyectofinal/recuperarcontenido.php"));
        }catch (MalformedURLException e){}
    }

}
