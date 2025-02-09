package com.example.train;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HoraireAdapter adapter;
    private List<HorairesTrain> listeHoraires;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Récupérer les données envoyées
        String depart = getIntent().getStringExtra("tvDepart");
        String arrivee = getIntent().getStringExtra("tvArrivee");
        String dateAller = getIntent().getStringExtra("dateAller");
        String dateRetour = getIntent().getStringExtra("dateRetour");

        // Afficher les détails de l'itinéraire
        TextView tvItineraire = findViewById(R.id.tvItineraire);
        tvItineraire.setText(depart + " → " + arrivee + "\n" + dateAller +
                (dateRetour.equals(getString(R.string.select_retour_date)) ? "" : "\nRetour : " + dateRetour));

        // Initialisation de la RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTrains);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Simuler les horaires de train
        listeHoraires = new ArrayList<>();
        listeHoraires.add(new HorairesTrain("08:00", "10:00", depart, arrivee));
        listeHoraires.add(new HorairesTrain("10:30", "12:30", depart, arrivee));
        listeHoraires.add(new HorairesTrain("14:00", "16:00", depart, arrivee));

        adapter = new HoraireAdapter(listeHoraires);
        recyclerView.setAdapter(adapter);

        // Bouton retour
        ImageView btnRetourForm = findViewById(R.id.btnRetourForm);
        btnRetourForm.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }
}
