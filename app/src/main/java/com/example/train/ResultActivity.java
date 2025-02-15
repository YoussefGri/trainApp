package com.example.train;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HoraireAdapter adapter;
    private List<HorairesTrain> listeHoraires;
    private String depart, arrivee, dateAller, selectedTime;

    Map<String, List<HorairesTrain>> trains = new HashMap<>();


    private ViewPager2 viewPagerDates;

    //private TextView tvItineraire;  // Pour afficher le récapitulatif
    private static final String TAG = "ResultActivity";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Récupération des données envoyées
        depart = getIntent().getStringExtra("tvDepart");
        arrivee = getIntent().getStringExtra("tvArrivee");
        String dateAllerRecu = getIntent().getStringExtra("dateAller");
        selectedTime = getIntent().getStringExtra("selectedTime");

        dateAller = convertirFormatDate(dateAllerRecu);

        // Vérification des valeurs nulles
        if (depart == null || arrivee == null || dateAller == null) {
            Toast.makeText(this, "Données manquantes. Retour au formulaire.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialisation des vues
       // tvItineraire = findViewById(R.id.tvItineraire);

        viewPagerDates = findViewById(R.id.viewPagerDates);

        viewPagerDates.setPageTransformer((page, position) -> {
            float scaleFactor = Math.max(0.85f, 1 - Math.abs(position));
            float alphaFactor = Math.max(0.5f, 1 - Math.abs(position));

            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setAlpha(alphaFactor);
        });

        mettreAJourRecapitulatif();
        recyclerView = findViewById(R.id.recyclerViewTrains);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chargerHoraires();  // Charge les horaires pour la date initiale
        chargerDatesDisponibles();  // Charge les dates pour le carrousel



        // Bouton Modifier le trajet
        ImageButton btnModifierRecherche = findViewById(R.id.btnModifierRecherche);
        btnModifierRecherche.setOnClickListener(v -> afficherPopupModification());

        // Bouton Retour
        ImageView btnRetourForm = findViewById(R.id.btnRetourForm);
        btnRetourForm.setOnClickListener(v -> retournerAuFormulaire());
    }

    private void mettreAJourRecapitulatif() {
//        tvItineraire.setText(String.format("%s → %s\n%s%s",
//                depart, arrivee, dateAller,
//                selectedTime != null ? "\nAprès " + selectedTime : ""));
    }

    private void afficherPopupModification() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_modifier_recherche, null);
        dialog.setContentView(view);

        EditText etDepart = view.findViewById(R.id.etDepart);
        EditText etArrivee = view.findViewById(R.id.etArrivee);
        Button btnDateAller = view.findViewById(R.id.btnDateAller);
        Button btnSelectTime = view.findViewById(R.id.btnSelectTime);
        Button btnValider = view.findViewById(R.id.btnValiderModification);

        etDepart.setText(depart);
        etArrivee.setText(arrivee);

        etDepart.setTextColor(Color.LTGRAY);
        etArrivee.setTextColor(Color.LTGRAY);


        btnDateAller.setText(dateAller);
        btnSelectTime.setText(selectedTime != null ? selectedTime : "Sélectionner l'heure");

        btnDateAller.setOnClickListener(v -> afficherDatePicker(btnDateAller));
        btnSelectTime.setOnClickListener(v -> afficherTimePicker(btnSelectTime));

        btnValider.setOnClickListener(v -> {
            String newDepart = etDepart.getText().toString().trim();
            String newArrivee = etArrivee.getText().toString().trim();
            String newDateAller = btnDateAller.getText().toString().trim();
            String newTime = btnSelectTime.getText().toString().trim();

            if (newDepart.isEmpty() || newArrivee.isEmpty() || newDateAller.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            depart = newDepart;
            arrivee = newArrivee;
            dateAller = newDateAller;
            selectedTime = newTime.equals("Sélectionner l'heure") ? null : newTime;

            mettreAJourRecapitulatif();
            dialog.dismiss();
            chargerHoraires();
        });

        dialog.show();
    }

    private void chargerHoraires() {
        trains.put("Paris → Lyon", Arrays.asList(
                new HorairesTrain("06:30", "09:00", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("10:00", "12:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("15:00", "17:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("18:00", "20:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("21:00", "23:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("23:59", "02:30", "Paris", "Lyon", "15/02/2025"),
                new HorairesTrain("10:00", "12:30", "Paris", "Lyon", "16/02/2025"),
                new HorairesTrain("15:00", "17:30", "Paris", "Lyon", "16/02/2025")

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
                    if (selectedTime == null || compareHeure(train.getHeureDepart(), selectedTime)) {
                        listeHoraires.add(train);
                    }
                }
            }
        }

        adapter = new HoraireAdapter(listeHoraires);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (listeHoraires.isEmpty()) {
            Toast.makeText(this, "Aucun train trouvé après " + (selectedTime != null ? selectedTime : "00:00"), Toast.LENGTH_LONG).show();
        }
    }

    private void retournerAuFormulaire() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void afficherDatePicker(Button button) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE. dd MMM., HH'h'", Locale.FRANCE);
                    calendar.set(year, month, dayOfMonth, 6, 0);
                    button.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void afficherTimePicker(Button button) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> button.setText(String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }


    /**
     * Charge les 7 prochaines dates et les affiche dans le carrousel ViewPager2
     */
    private void chargerDatesDisponibles() {
        List<String> datesDisponibles = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

        // Vérifier quelles dates ont au moins un train disponible
        String trajet = depart + " → " + arrivee;
        if (trains.containsKey(trajet)) {
            for (HorairesTrain train : trains.get(trajet)) {
                if (!datesDisponibles.contains(train.getDate())) {
                    datesDisponibles.add(train.getDate()); // Ajouter uniquement les dates avec des trains
                }
            }
        }

        // Si aucune date trouvée, afficher un message
        if (datesDisponibles.isEmpty()) {
            Toast.makeText(this, "Aucune date disponible pour ce trajet.", Toast.LENGTH_LONG).show();
            return;
        }

        // Création et association de l'adapter
        DatePagerAdapter adapter = new DatePagerAdapter(datesDisponibles, date -> {
            dateAller = date;  // Met à jour la date sélectionnée
            mettreAJourRecapitulatif();
            chargerHoraires();
        });

        viewPagerDates.setAdapter(adapter);
    }

    /**
     * Compare l'heure d'un train avec l'heure sélectionnée par l'utilisateur.
     * Retourne `true` si l'heure du train est **postérieure** à l'heure choisie.
     */

    private boolean compareHeure(String heureTrain, String heureFiltre) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            Date dateTrain = sdf.parse(heureTrain);
            Date dateFiltre = sdf.parse(heureFiltre);
            return dateTrain != null && dateTrain.after(dateFiltre);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
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
