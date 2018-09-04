package com.example.uer.trabajogradofittness.Conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.uer.trabajogradofittness.BD.DatosEntreno;
import com.example.uer.trabajogradofittness.BD.RegistroEntreno;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "proyecto";

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(RegistroEntreno.CREAR_TABLA_REGISTRO_ENTRENO);
        db.execSQL(DatosEntreno.CREAR_TABLA_DATOS_ENTRENO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        /*db.execSQL("DROP TABLE IF EXISTS "+RegistroEntreno.TABLA_REGISTRO_ENTRENO);
        onCreate(db);*/
    }

    public static void BD_backup() throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        final String inFileName = "/data/data/com.example.felipe.uer.trabajogradofittness/databases/"+DATABASE_NAME;
        File dbFile = new File(inFileName);
        FileInputStream fis = null;

        fis = new FileInputStream(dbFile);

        //String directorio = obtenerDirectorioCopias();
        String directorio = "/storage/emulated/0/Download/";
        File d = new File(directorio);
        if (!d.exists()) {
            d.mkdir();
        }
        String outFileName = directorio + "/"+DATABASE_NAME + "_"+timeStamp;

        OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        fis.close();
    }
}
