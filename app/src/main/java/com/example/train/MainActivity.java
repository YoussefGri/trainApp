package com.example.train;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnDateAller, btnSelectTime;
    private ImageButton btnDeleteAll, btnSwap;
    private EditText tvDepart, tvArrivee;
    private String selectedTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        btnDateAller = findViewById(R.id.btnDateAller);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        btnSwap = findViewById(R.id.btnSwap);
        tvDepart = findViewById(R.id.tvDepart);
        tvArrivee = findViewById(R.id.tvArrivee);

        // Sélection de la date de départ
        btnDateAller.setOnClickListener(v -> afficherDatePicker(btnDateAller));

        // Sélection de l'heure de départ
        btnSelectTime.setOnClickListener(v -> showTimePicker());

        // Bouton Swap : échange les valeurs des champs Départ et Arrivée
        btnSwap.setOnClickListener(v -> swapDestinations());

        // Bouton Delete All : réinitialise les champs de texte et de date
        btnDeleteAll.setOnClickListener(v -> resetFields());

        // Lancer l'activité ResultActivity
        findViewById(R.id.btnRechercher).setOnClickListener(v -> rechercherTrains());
    }

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

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedTime = String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, minute);
                    btnSelectTime.setText(getString(R.string.departure_after) +" : " + selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void swapDestinations() {
        String depart = tvDepart.getText().toString();
        String arrivee = tvArrivee.getText().toString();

        if (depart.isEmpty() && arrivee.isEmpty()) {
            Toast.makeText(this, getString(R.string.fields_already_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        tvDepart.setText(arrivee);
        tvArrivee.setText(depart);
    }

    private void resetFields() {
        tvDepart.setText("");
        tvArrivee.setText("");
        btnDateAller.setText(getString(R.string.select_depart_date));
        btnSelectTime.setText(R.string.select_departure_time);
        selectedTime = null;
        Toast.makeText(this,getString(R.string.field_reinitialization), Toast.LENGTH_SHORT).show();
    }

    private void rechercherTrains() {
        String dateAller = btnDateAller.getText().toString();

        if (dateAller.equals(getString(R.string.select_depart_date))) {
            Toast.makeText(this, getString(R.string.select_departure_date), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("dateAller", dateAller);
        intent.putExtra("tvDepart", tvDepart.getText().toString());
        intent.putExtra("tvArrivee", tvArrivee.getText().toString());

        if (selectedTime != null) {
            intent.putExtra("selectedTime", selectedTime);
        }

        startActivity(intent);
    }
}
