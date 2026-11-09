package com.example.flytrapiot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "flytrap_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TRAMPAS = "trampas";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODIGO = "codigo_sector";
    public static final String COLUMN_TIPO = "tipo_trampa";
    public static final String COLUMN_NIVEL = "nivel_captura";
    public static final String COLUMN_OBS = "observaciones";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_TRAMPAS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CODIGO + " TEXT NOT NULL, "
                + COLUMN_TIPO + " TEXT NOT NULL, "
                + COLUMN_NIVEL + " INTEGER NOT NULL, "
                + COLUMN_OBS + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAMPAS);
        onCreate(db);
    }

    public long insertarTrampa(String codigo, String tipo, int nivel, String obs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, codigo);
        values.put(COLUMN_TIPO, tipo);
        values.put(COLUMN_NIVEL, nivel);
        values.put(COLUMN_OBS, obs);

        long id = db.insert(TABLE_TRAMPAS, null, values);
        db.close();
        return id;
    }

    public Cursor obtenerTodasLasTrampas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRAMPAS, null);
    }

    public Cursor obtenerTrampaPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRAMPAS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int actualizarTrampa(int id, String codigo, String tipo, int nivel, String obs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, codigo);
        values.put(COLUMN_TIPO, tipo);
        values.put(COLUMN_NIVEL, nivel);
        values.put(COLUMN_OBS, obs);

        int filasAfectadas = db.update(TABLE_TRAMPAS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas;
    }

    public int eliminarTrampa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasEliminadas = db.delete(TABLE_TRAMPAS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return filasEliminadas;
    }
}
