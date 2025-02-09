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
        HorairesTrain train = horaires.get(position);

        holder.tvHeureDepart.setText(train.getHeureDepart());
        holder.tvHeureArrivee.setText(train.getHeureArrivee());
        holder.tvVilleDepart.setText(train.getVilleDepart());
        holder.tvVilleArrivee.setText(train.getVilleArrivee());
        holder.tvDate.setText("Date : " + train.getDate());

        int duree = calculerDuree(train.getHeureDepart(), train.getHeureArrivee());
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
        TextView tvHeureDepart, tvHeureArrivee, tvDuree, tvVilleDepart, tvVilleArrivee, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeureDepart = itemView.findViewById(R.id.tvHeureDepart);
            tvHeureArrivee = itemView.findViewById(R.id.tvHeureArrivee);
            tvDuree = itemView.findViewById(R.id.tvDuree);
            tvVilleDepart = itemView.findViewById(R.id.tvVilleDepart);
            tvVilleArrivee = itemView.findViewById(R.id.tvVilleArrivee);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
