package com.example.train;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DatePagerAdapter extends RecyclerView.Adapter<DatePagerAdapter.ViewHolder> {

    private final List<String> datesDisponibles;
    private final OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(String date);
    }

    public DatePagerAdapter(List<String> dates, OnDateClickListener listener) {
        this.datesDisponibles = dates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel_date, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = datesDisponibles.get(position);
        holder.tvDate.setText(date);

        // Appliquer un effet de zoom léger sur l'élément actif
        holder.itemView.setScaleX(0.9f);
        holder.itemView.setScaleY(0.9f);
        holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();

        holder.itemView.setOnClickListener(v -> listener.onDateClick(date));
    }


    @Override
    public int getItemCount() {
        return datesDisponibles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;

        ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDateCarousel);
        }
    }
}
