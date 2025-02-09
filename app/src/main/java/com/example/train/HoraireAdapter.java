package com.example.train;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HoraireAdapter extends RecyclerView.Adapter<HoraireAdapter.ViewHolder> {

    private List<HorairesTrain> horaires;

    public HoraireAdapter(List<HorairesTrain> horaires) {
        this.horaires = horaires;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horaire, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HorairesTrain horaire = horaires.get(position);

        holder.tvHeureDepart.setText(horaire.getHeureDepart());
        holder.tvHeureArrivee.setText(horaire.getHeureArrivee());

        // 🔹 Calcul automatique de la durée en minutes
        int duree = calculerDuree(horaire.getHeureDepart(), horaire.getHeureArrivee());
        holder.tvDuree.setText(duree + " min");
    }


    private int calculerDuree(String heureDepart, String heureArrivee) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        try {
            Date depart = sdf.parse(heureDepart);
            Date arrivee = sdf.parse(heureArrivee);

            if (depart != null && arrivee != null) {
                long difference = arrivee.getTime() - depart.getTime();
                return (int) TimeUnit.MILLISECONDS.toMinutes(difference);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0; // Valeur par défaut si une erreur survient
    }



    @Override
    public int getItemCount() {
        return horaires.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeureDepart, tvHeureArrivee, tvDuree;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeureDepart = itemView.findViewById(R.id.tvHeureDepart);
            tvHeureArrivee = itemView.findViewById(R.id.tvHeureArrivee);
            tvDuree = itemView.findViewById(R.id.tvDuree);
        }
    }
}
