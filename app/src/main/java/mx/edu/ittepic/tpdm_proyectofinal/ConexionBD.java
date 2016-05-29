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
        db.execSQL("CREATE TABLE Categoria(idCategoria INTEGER PRIMARY KEY, nombre VARCHAR(80))");

        db.execSQL("CREATE TABLE DetalleOrden(idDetalleOrden INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idOrden INTEGER, idItem INTEGER, cantidad INTEGER," +
                "FOREIGN KEY (idOrden) REFERENCES Orden(idOrden)," +
                "FOREIGN KEY (idItem) REFERENCES Menu(idItem))");

        db.execSQL("CREATE TABLE DetallePago(idDetallePago INTEGER PRIMARY KEY, TipoDetallePago VARCHAR(60))");
        db.execSQL("CREATE TABLE Menu (idItem INTEGER PRIMARY KEY, idCategoria INTEGER, nombre VARCHAR(60), " +
                "descripcion VARCHAR(200), precio DECIMAL(13,2), disponibilidad CHAR(1)," +
                "FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria))");
        db.execSQL("CREATE TABLE Mesa(idMesa INTEGER PRIAMRY KEY, capacidad INTEGER, disponibilidad CHAR(1))");
        db.execSQL("CREATE TABLE Orden (idOrden INTEGER PRIMARY KEY AUTOINCREMENT, idMesa INTEGER, idUsuario INTEGER, " +
                "fechaPedido DATE, horaPedido TIME, status CHAR(1),totalPagar DECIMAL(13,2)," +
                "FOREIGN KEY (idMesa) REFERENCES Mesa(idMesa)," +
                "FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario))");
        db.execSQL("CREATE TABLE Pago(idPago INTEGER PRIMARY KEY AUTOINCREMENT, idOrden INTEGER, idDetallePago INTEGER, nombre VARCHAR(150),noIFE CHAR(13), " +
                "FOREIGN KEY (idOrden) REFERENCES Orden(idOrden)," +
                "FOREIGN KEY (idDetallePago) REFERENCES DetallePago(idDetallePago))");
        db.execSQL("CREATE TABLE Usuario (idUsuario CHAR(10) PRIMARY KEY, nombre VARCHAR(80), apellidoPaterno VARCHAR(50), apellidoMaterno VARCHAR(50)," +
                "password VARCHAR(16),puesto VARCHAR(60), numCelular VARCHAR(10), tipoUsuario CHAR(1))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
