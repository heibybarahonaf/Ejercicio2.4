package com.example.ejercicio24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ejercicio24.Configuraciones.SQLiteConexion;
import com.example.ejercicio24.Configuraciones.Signatures;
import com.example.ejercicio24.Configuraciones.Transacciones;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ActivityListar extends AppCompatActivity {

    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
    String sql = "SELECT * FROM Signatures";
    Button btnAtras;
    ArrayList<Signatures> listaFirmas = new ArrayList<Signatures>();
    ImageView imageView;
    TextView txtDesFirma;
    ListView list;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        SQLiteDatabase db = conexion.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {});

        while (cursor.moveToNext()){
            listaFirmas.add(new Signatures(cursor.getInt(0),cursor.getString(1) , cursor.getBlob(2)));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        AdaptadorFirmas adaptador = new AdaptadorFirmas(this);
        list = findViewById(R.id.lista);
        list.setAdapter(adaptador);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                obtenerFoto(i);
            }
        });

        btnAtras = (Button) findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void obtenerFoto( int id) {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Signatures lista_Firmas = null;
        listaFirmas = new ArrayList<Signatures>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.TablaSignatures,null);

        while (cursor.moveToNext())
        {
            lista_Firmas = new Signatures();
            lista_Firmas.setDescripcion(cursor.getString(0));
            listaFirmas.add(lista_Firmas);
        }
        cursor.close();
        Signatures signatures = listaFirmas.get(id);
    }


    class AdaptadorFirmas extends ArrayAdapter<Signatures> {

        AppCompatActivity appCompatActivity;

        AdaptadorFirmas(AppCompatActivity context) {
            super(context, R.layout.firma, listaFirmas);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.firma, null);
            SQLiteDatabase db = conexion.getWritableDatabase();

            imageView = item.findViewById(R.id.sImgFirma);
            txtDesFirma = item.findViewById(R.id.sDescripcionFirma);

            Cursor cursor = db.rawQuery(sql, new String[] {});
            if (cursor.moveToNext()){
                txtDesFirma.setText(listaFirmas.get(position).getDescripcion());
                byte[] blob = listaFirmas.get(position).getImage();
                ByteArrayInputStream bits = new ByteArrayInputStream(blob);
                bitmap = BitmapFactory.decodeStream(bits);
                imageView.setImageBitmap(bitmap);
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();

            return(item);
        }
    }
}