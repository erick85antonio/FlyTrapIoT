package com.example.flytrapiot;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    private EditText etCodigo, etTipo, etNivel, etObservaciones;
    private Button btnGuardar, btnEliminar;
    private DBHelper dbHelper;
    private int trampaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etCodigo = findViewById(R.id.etCodigo);
        etTipo = findViewById(R.id.etTipo);
        etNivel = findViewById(R.id.etNivel);
        etObservaciones = findViewById(R.id.etObservaciones);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);

        dbHelper = new DBHelper(this);

        if (getIntent().hasExtra("TRAMPA_ID")) {
            trampaId = getIntent().getIntExtra("TRAMPA_ID", -1);
            cargarDatosTrampa(trampaId);
            btnEliminar.setVisibility(View.VISIBLE);
        } else {
            btnEliminar.setVisibility(View.GONE);
        }

        btnGuardar.setOnClickListener(v -> guardarDatos());

        btnEliminar.setOnClickListener(v -> {
            if (trampaId != -1) {
                dbHelper.eliminarTrampa(trampaId);
                Toast.makeText(this, "Trampa eliminada con éxito", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void cargarDatosTrampa(int id) {
        Cursor cursor = dbHelper.obtenerTrampaPorId(id);
        if (cursor != null && cursor.moveToFirst()) {
            etCodigo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CODIGO)));
            etTipo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TIPO)));
            etNivel.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NIVEL))));
            etObservaciones.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_OBS)));
            cursor.close();
        }
    }

    private void guardarDatos() {
        String codigo = etCodigo.getText().toString().trim();
        String tipo = etTipo.getText().toString().trim();
        String nivelStr = etNivel.getText().toString().trim();
        String obs = etObservaciones.getText().toString().trim();

        if (codigo.isEmpty() || tipo.isEmpty() || nivelStr.isEmpty()) {
            Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int nivel = Integer.parseInt(nivelStr);

        if (trampaId == -1) {
            long result = dbHelper.insertarTrampa(codigo, tipo, nivel, obs);
            if (result != -1) {
                Toast.makeText(this, "Trampa guardada con éxito", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            int result = dbHelper.actualizarTrampa(trampaId, codigo, tipo, nivel, obs);
            if (result > 0) {
                Toast.makeText(this, "Trampa actualizada con éxito", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}