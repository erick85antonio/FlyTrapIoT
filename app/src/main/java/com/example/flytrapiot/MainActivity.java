package com.example.flytrapiot;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewTrampas;
    private Button btnAgregarTrampa;
    private DBHelper dbHelper;
    private ArrayList<String> listaFormateada;
    private ArrayList<Integer> listaIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewTrampas = findViewById(R.id.listViewTrampas);
        btnAgregarTrampa = findViewById(R.id.btnAgregarTrampa);
        dbHelper = new DBHelper(this);

        btnAgregarTrampa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        });

        listViewTrampas.setOnItemClickListener((parent, view, position, id) -> {
            int idSeleccionado = listaIds.get(position);
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            intent.putExtra("TRAMPA_ID", idSeleccionado);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTrampas();
    }

    private void cargarTrampas() {
        listaFormateada = new ArrayList<>();
        listaIds = new ArrayList<>();
        Cursor cursor = dbHelper.obtenerTodasLasTrampas();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
                String codigo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CODIGO));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TIPO));
                int nivel = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NIVEL));

                listaIds.add(id);
                listaFormateada.add("Sector: " + codigo + " | Tipo: " + tipo + " (Capturas: " + nivel + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaFormateada);
        listViewTrampas.setAdapter(adapter);
    }
}