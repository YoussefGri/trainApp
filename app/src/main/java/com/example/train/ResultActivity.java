
package com.example.train;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HoraireAdapter adapter;
    private List<HorairesTrain> listeHoraires;
    private String depart, arrivee, dateAller;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Récupération des données envoyées
        depart = getIntent().getStringExtra("tvDepart");
        arrivee = getIntent().getStringExtra("tvArrivee");
        String dateAllerRecu = getIntent().getStringExtra("dateAller");
        dateAller = convertirFormatDate(dateAllerRecu);

        // Vérification des valeurs nulles
        if (depart == null || arrivee == null || dateAller == null) {
            Toast.makeText(this, "Données manquantes. Retour au formulaire.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Affichage de l'itinéraire
        TextView tvItineraire = findViewById(R.id.tvItineraire);
        tvItineraire.setText(String.format("%s → %s\n%s", depart, arrivee, dateAller));

        // Initialisation de la RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTrains);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Charger les horaires de train
        chargerHoraires();

        // Bouton retour
        ImageView btnRetourForm = findViewById(R.id.btnRetourForm);
        btnRetourForm.setOnClickListener(v -> retournerAuFormulaire());
    }

    /**
     * Charge des horaires de train fictifs en fonction des villes et de la date choisie.
     */
    private void chargerHoraires() {
        // Base de données fictive des trains
        Map<String, List<HorairesTrain>> trains = new HashMap<>();
        trains.put("Paris → Lyon", Arrays.asList(
                new HorairesTrain("06:30", "09:00", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("10:00", "12:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("15:00", "17:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("18:00", "20:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("21:00", "23:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("23:59", "02:30", "Paris", "Lyon", "15/02/2025")
        ));
        trains.put("Lyon → Marseille", Arrays.asList(
                new HorairesTrain("07:00", "09:30", "Lyon", "Marseille", "15/02/2025"),
                new HorairesTrain("13:30", "16:00", "Lyon", "Marseille", "15/02/2025")
        ));
        trains.put("Bordeaux → Toulouse", Arrays.asList(
                new HorairesTrain("08:00", "09:40", "Bordeaux", "Toulouse", "15/02/2025"),
                new HorairesTrain("14:30", "16:10", "Bordeaux", "Toulouse", "15/02/2025")
        ));

        listeHoraires = new ArrayList<>();
        String trajet = depart + " → " + arrivee;

        if (trains.containsKey(trajet)) {
            for (HorairesTrain train : trains.get(trajet)) {
                if (train.getDate().equals(dateAller)) {
                    listeHoraires.add(train);
                }
            }
        }

        if (listeHoraires.isEmpty()) {
            Toast.makeText(this, "Aucun train trouvé pour cet itinéraire et cette date. " + dateAller, Toast.LENGTH_LONG).show();
        }

        adapter = new HoraireAdapter(listeHoraires);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Retourne à l'écran du formulaire.
     */
    private void retournerAuFormulaire() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Convertit une date au format "sam.. 15 févr.., 06h" en "15/02/2025".
     */
    private String convertirFormatDate(String dateAller) {
        if (dateAller == null || dateAller.isEmpty()) return null;

        try {
            // Nettoyer la date en supprimant les ".."
            String dateNettoyee = dateAller.replace("..", "").trim();

            // Récupérer l'année actuelle
            Calendar calendar = Calendar.getInstance();
            int anneeActuelle = calendar.get(Calendar.YEAR);

            // Ajouter l'année actuelle à la date reçue
            String dateAvecAnnee = dateNettoyee + " " + anneeActuelle;

            // Définir le format d'entrée avec l'année ajoutée
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE dd MMM, HH'h' yyyy", Locale.FRANCE);

            // Définir le format de sortie souhaité
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

            // Parser et reformater
            Date date = inputFormat.parse(dateAvecAnnee);
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Retourne null en cas d'erreur
        }
    }



}



/*

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

 */
