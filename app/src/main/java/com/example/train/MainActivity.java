package com.example.train;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etDepart, etArrivee;
    private Button btnRechercher;
    private RecyclerView recyclerView;
    private HoraireAdapter adapter;
    private List<HorairesTrain> listeHoraires;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDepart = findViewById(R.id.etDepart);
        etArrivee = findViewById(R.id.etArrivee);
        btnRechercher = findViewById(R.id.btnRechercher);
        recyclerView = findViewById(R.id.recyclerViewHoraires);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listeHoraires = new ArrayList<>();

        adapter = new HoraireAdapter(listeHoraires);
        recyclerView.setAdapter(adapter);

        btnRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String depart = etDepart.getText().toString().trim();
                String arrivee = etArrivee.getText().toString().trim();

                if (depart.isEmpty() || arrivee.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez saisir les villes", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Simuler des horaires de train
                listeHoraires.clear();

                if (depart.equals("Paris") && arrivee.equals("Lyon")) {
                    listeHoraires.add(new HorairesTrain("08:00", "10:00", "Paris", "Lyon"));
                    listeHoraires.add(new HorairesTrain("10:00", "12:00", "Paris", "Lyon"));
                    listeHoraires.add(new HorairesTrain("12:00", "14:00", "Paris", "Lyon"));
                } else if (depart.equals("Montpellier") && arrivee.equals("Nice")) {
                    listeHoraires.add(new HorairesTrain("14:00", "16:00", "Montpellier", "Nice"));
                    listeHoraires.add(new HorairesTrain("16:00", "18:00", "Montpellier", "Nice"));
                } else if (depart.equals("Montpellier") && arrivee.equals("Béziers")) {
                    listeHoraires.add(new HorairesTrain("18:00", "20:00", "Monpellier", "Béziers"));
                } else {
                    Toast.makeText(MainActivity.this, "Aucun horaire trouvé", Toast.LENGTH_SHORT).show();
                }



                adapter.notifyDataSetChanged();
            }
        });
    }
}
