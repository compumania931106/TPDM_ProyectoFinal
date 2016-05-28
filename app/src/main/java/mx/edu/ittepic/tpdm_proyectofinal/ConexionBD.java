package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gabriela on 27/05/2016.
 */
public class ConexionBD extends SQLiteOpenHelper {

    public ConexionBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//activity,nombre,,version

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Orden(" +
                "   idOrden INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   idMesa INTEGER," +
                "   idUsuario CHAR(10)," +
                "   fechaPedido DATE," +
                "   horaPedido  TIME," +
                "   status  CHAR(1)," +
                "   totalPagar DECIMAL(13,2)");

        db.execSQL("CREATE TABLE Categoria(" +
                "   idCategoria INT PRIMARY KEY AUTOINCREMENT," +
                "   nombre VARCHAR(80))");

        db.execSQL("CREATE TABLE Menu(" +
                "   idItem PRIMARY KEY AUTOINCREMENT," +
                "   idCategoria INTEGER)," +
                "   nombre VARCHAR (60)," +
                "   descripcion VARCHAR(200)," +
                "   precio DECIMAL (13,2)," +
                "   disponibilidad CHAR(1)," +
                "   FOREING KEY (idCategoria) REFERENCES Categoria (idCategoria)");

        db.execSQL("CREATE TABLE DetalleOrden(" +
                "   idDetalleOrden INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   idOrden INTEGER," +
                "   idItem INTEGER," +
                "   FOREING KEY (idOrden) REFERENCES Orden (idOrden)," +
                "   FOREING KEY (idItem) REFERENCES Menu(idItem) )");

        db.execSQL("CREATE TABLE DetallePago(" +
                "   idDetallePago INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   tipoDetallePago VARCHAR (60))");

        db.execSQL("CREATE TABLE Pago(" +
                "   idPago INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   idOrden INTEJER," +
                "   idDetallePago INTEGER," +
                "   nombre VARCHAR(150)," +
                "   noIFE CHAR(13)," +
                "   FOREING KEY (idOrden) REFERENCES Orden (idOrden)," +
                "   FOREING KEY (idDetallePago) REFERENCES DetallePago (idDetallePago)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
