package com.example.train;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        holder.tvHeureDepart.setText("Départ: " + horaire.getHeureDepart());
        holder.tvHeureArrivee.setText("Arrivée: " + horaire.getHeureArrivee());
    }

    @Override
    public int getItemCount() {
        return horaires.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeureDepart, tvHeureArrivee;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeureDepart = itemView.findViewById(R.id.tvHeureDepart);
            tvHeureArrivee = itemView.findViewById(R.id.tvHeureArrivee);
        }
    }
}
