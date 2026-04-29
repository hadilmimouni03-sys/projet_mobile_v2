package com.livraison.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.livraison.R;
import com.livraison.model.Livraison;

import java.util.ArrayList;
import java.util.List;

public class LivraisonAdapter extends RecyclerView.Adapter<LivraisonAdapter.ViewHolder> {

    public interface OnItemClick { void onClick(Livraison l); }

    private List<Livraison> data = new ArrayList<>();
    private final OnItemClick listener;

    public LivraisonAdapter(OnItemClick listener) {
        this.listener = listener;
    }

    public void setData(List<Livraison> newData) {
        data = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livraison, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Livraison l = data.get(pos);
        h.tvNocde.setText("Commande #" + l.nocde);
        h.tvClient.setText(l.getNomCompletClient());
        h.tvVille.setText(l.villeClient != null ? l.villeClient : "");
        h.tvTel.setText(l.telClient != null ? l.telClient : "");
        h.tvEtat.setText(l.getEtatLabel());
        h.tvDate.setText(l.dateliv != null ? l.dateliv : "N/A");
        if (l.montantTotal != null)
            h.tvMontant.setText(String.format("%.2f €", l.montantTotal));
        else
            h.tvMontant.setText("N/A");

        // Couleur selon état
        int color;
        switch (l.etatliv != null ? l.etatliv : "") {
            case "LIVRE":      color = Color.parseColor("#E8F5E9"); break;
            case "NON_LIVRE":  color = Color.parseColor("#FFEBEE"); break;
            case "EN_COURS":   color = Color.parseColor("#E3F2FD"); break;
            case "EN_ATTENTE": color = Color.parseColor("#FFF9C4"); break;
            default:           color = Color.WHITE;
        }
        h.card.setCardBackgroundColor(color);
        h.itemView.setOnClickListener(v -> listener.onClick(l));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvNocde, tvClient, tvVille, tvTel, tvEtat, tvDate, tvMontant;

        ViewHolder(View v) {
            super(v);
            card      = v.findViewById(R.id.card);
            tvNocde   = v.findViewById(R.id.tvNocde);
            tvClient  = v.findViewById(R.id.tvClient);
            tvVille   = v.findViewById(R.id.tvVille);
            tvTel     = v.findViewById(R.id.tvTel);
            tvEtat    = v.findViewById(R.id.tvEtat);
            tvDate    = v.findViewById(R.id.tvDate);
            tvMontant = v.findViewById(R.id.tvMontant);
        }
    }
}
