package com.example.train;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnDateAller, btnDateRetour;
    private ImageButton btnDeleteAll, btnSwap;
    private EditText tvDepart, tvArrivee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        btnDateAller = findViewById(R.id.btnDateAller);
        btnDateRetour = findViewById(R.id.btnDateRetour);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        btnSwap = findViewById(R.id.btnSwap);
        tvDepart = findViewById(R.id.tvDepart);
        tvArrivee = findViewById(R.id.tvArrivee);

        // Sélection de la date de départ
        btnDateAller.setOnClickListener(v -> afficherDatePicker(btnDateAller));

        // Sélection de la date de retour
        btnDateRetour.setOnClickListener(v -> afficherDatePicker(btnDateRetour));

        // Bouton Swap : échange les valeurs des champs Départ et Arrivée
        btnSwap.setOnClickListener(v -> swapDestinations());

        // Bouton Delete All : réinitialise les champs de texte et de date
        btnDeleteAll.setOnClickListener(v -> resetFields());

        // Lancer l'activité ResultActivity
        findViewById(R.id.btnRechercher).setOnClickListener(v -> rechercherTrains());
    }

    // Fonction pour afficher le DatePickerDialog
    private void afficherDatePicker(MaterialButton button) {
        final Calendar calendar = Calendar.getInstance();
        Context context = new ContextThemeWrapper(this, R.style.CustomDatePickerDialog);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
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


    // Fonction pour échanger les valeurs des champs Départ et Arrivée
    private void swapDestinations() {
        String depart = tvDepart.getText().toString();
        String arrivee = tvArrivee.getText().toString();

        if (depart.isEmpty() && arrivee.isEmpty()) {
            Toast.makeText(this, "Les champs sont vides, rien à échanger", Toast.LENGTH_SHORT).show();
            return;
        }

        tvDepart.setText(arrivee);
        tvArrivee.setText(depart);
    }

    // Fonction pour réinitialiser tous les champs (Départ, Arrivée, Dates)
    private void resetFields() {
        tvDepart.setText("");
        tvArrivee.setText("");
        btnDateAller.setText(getString(R.string.select_depart_date));
        btnDateRetour.setText(getString(R.string.select_retour_date));

        Toast.makeText(this, "Champs réinitialisés", Toast.LENGTH_SHORT).show();
    }

    // Fonction pour lancer ResultActivity avec les données sélectionnées
    private void rechercherTrains() {
        String dateAller = btnDateAller.getText().toString();
        String dateRetour = btnDateRetour.getText().toString();

        if (dateAller.equals(getString(R.string.select_depart_date))) {
            Toast.makeText(this, "Veuillez sélectionner une date de départ", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("dateAller", dateAller);
        intent.putExtra("dateRetour", dateRetour);
        startActivity(intent);
    }
}
