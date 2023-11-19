package com.example.ejercicio24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ejercicio24.Configuraciones.FirmaImag;
import com.example.ejercicio24.Configuraciones.SQLiteConexion;
import com.example.ejercicio24.Configuraciones.Transacciones;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirmaImag firmaImag;
    EditText Descripcion;
    Button btnGuardar,btnListarFirmas;
    LinearLayout fContent;
    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Descripcion = ( EditText) findViewById(R.id.txtDescripcion);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnListarFirmas = (Button) findViewById(R.id.btnMostrarFirmas);
        fContent = (LinearLayout) findViewById(R.id.firmaLayout);
        firmaImag = new FirmaImag(this, null);
        fContent.addView(firmaImag, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Descripcion.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese una descripcion", Toast.LENGTH_SHORT).show();
                } else {
                    guardarDatos();
                }
            }
        });


        btnListarFirmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ActivityListar.class);
                startActivity(intent);
            }
        });
    }

    private void guardarDatos() {
        try {
            firmas(firmaImag.getBitmap());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName , "yourDescription");

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            LimpiarPantalla();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al guardar Datos ",Toast.LENGTH_LONG).show();
        }


    }

    private void firmas( Bitmap bitmap) {

        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] ArrayFoto  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.descripcion,Descripcion.getText().toString());
        valores.put(String.valueOf(Transacciones.firma),ArrayFoto);

        Long resultado = db.insert(Transacciones.TablaSignatures, null, valores);

        Toast.makeText(getApplicationContext(), "Registro ingreso con exito: " + resultado.toString()
                ,Toast.LENGTH_LONG).show();

        db.close();
    }



    private void LimpiarPantalla()
    {
        Descripcion.setText("");
        firmaImag.ClearCanvas();

    }
}