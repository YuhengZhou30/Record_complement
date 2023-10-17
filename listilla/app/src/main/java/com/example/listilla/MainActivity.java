package com.example.listilla;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Record randomUser() {
        String[] nombres = {"Juan", "María", "Carlos", "Laura", "Pedro", "Sofía", "Luis", "Ana", "Javier", "Elena"};
        String[] apellidos = {"Gómez", "Pérez", "López", "Fernández", "Martínez", "García", "Ruiz", "Torres", "Sánchez", "Ramírez"};
        Random random = new Random();
        int intentos= random.nextInt(15) + 3; // Entre 1 y 10 intentos
        String  nombre = nombres[random.nextInt(nombres.length)];
        String apellido = apellidos[random.nextInt(apellidos.length)];

        return new Record(intentos, nombre+ " " + apellido);
    }

    private void cambiarImagenAleatoria() {
        Random random = new Random();
        int numeroAleatorio = random.nextInt(3) + 1; // Genera un número aleatorio entre 1 y 3

        String nombreImagen = "i"+numeroAleatorio ;

        int resourceId = getResources().getIdentifier(nombreImagen, "drawable", getPackageName());
        imageView.setImageResource(resourceId);

    }

    // Model: Record (intents=puntuació, nom)
    class Record {
        public int intents;
        public String nom;

        public Record(int _intents, String _nom ) {
            intents = _intents;
            nom = _nom;
        }
    }
    // Model = Taula de records: utilitzem ArrayList
    ArrayList<Record> records;
    private ImageView imageView;
    private int imagenActual = 1;

    // ArrayAdapter serà l'intermediari amb la ListView
    ArrayAdapter<Record> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Inicialitzem model
        records = new ArrayList<Record>();
        // Afegim alguns exemples
        records.add( new Record(33,"Manolo") );
        records.add( new Record(12,"Pepe") );
        records.add( new Record(42,"Laura") );

        // Inicialitzem l'ArrayAdapter amb el layout pertinent
        adapter = new ArrayAdapter<Record>( this, R.layout.list_item, records )
        {
            @Override
            public View getView(int pos, View convertView, ViewGroup container)
            {
                // getView ens construeix el layout i hi "pinta" els valors de l'element en la posició pos
                if( convertView==null ) {
                    // inicialitzem l'element la View amb el seu layout
                    convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
                }
                // "Pintem" valors (també quan es refresca)
                ((TextView) convertView.findViewById(R.id.nom)).setText(getItem(pos).nom);
                ((TextView) convertView.findViewById(R.id.intents)).setText(Integer.toString(getItem(pos).intents));
                imageView = convertView.findViewById(R.id.imageView);
                cambiarImagenAleatoria();
                return convertView;
            }

        };


        // busquem la ListView i li endollem el ArrayAdapter
        ListView lv = (ListView) findViewById(R.id.recordsView);
        lv.setAdapter(adapter);

        // botó per afegir entrades a la ListView
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                for (int i=0;i<3;i++) {
                    records.add(randomUser());
                }
                // notificar l'adapter dels canvis al model
                adapter.notifyDataSetChanged();
            }
        });

        Button btnOrdenar = findViewById(R.id.btnOrdenar);
        btnOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Collections.sort(records, new Comparator<Record>() {
                    @Override
                    public int compare(Record record1, Record record2) {
                        return Integer.compare(record1.intents, record2.intents);
                    }
                });

                // Notifica al ArrayAdapter de los cambios en la lista
                adapter.notifyDataSetChanged();
            }
        });

    }
}