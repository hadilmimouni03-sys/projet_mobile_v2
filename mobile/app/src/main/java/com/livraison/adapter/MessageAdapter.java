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
import com.livraison.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> data = new ArrayList<>();
    private final boolean showDestinataire;

    public MessageAdapter(boolean showDestinataire) {
        this.showDestinataire = showDestinataire;
    }

    public void setData(List<Message> newData) {
        data = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Message m = data.get(pos);

        String de = "De : " + (m.nomExpediteur != null ? m.nomExpediteur : "Inconnu");
        String a = showDestinataire ? "  →  " +
                (m.nomDestinataire != null ? m.nomDestinataire : "Tous") : "";
        h.tvExpDest.setText(de + a);
        h.tvContenu.setText(m.contenu);
        h.tvDate.setText(m.dateenvoi != null ? m.dateenvoi.substring(0, 16) : "");
        h.tvType.setText("URGENCE".equals(m.typeMessage) ? "🚨 URGENCE" : "ℹ INFO");

        if (m.nocde != null) {
            h.tvNocde.setVisibility(View.VISIBLE);
            h.tvNocde.setText("Commande #" + m.nocde);
        } else {
            h.tvNocde.setVisibility(View.GONE);
        }

        // Couleur selon type et état de lecture
        int bgColor;
        if ("URGENCE".equals(m.typeMessage)) {
            bgColor = Boolean.FALSE.equals(m.lu)
                    ? Color.parseColor("#FFCDD2") : Color.parseColor("#FFEBEE");
        } else {
            bgColor = Boolean.FALSE.equals(m.lu)
                    ? Color.parseColor("#E3F2FD") : Color.WHITE;
        }
        h.card.setCardBackgroundColor(bgColor);
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvExpDest, tvContenu, tvDate, tvType, tvNocde;

        ViewHolder(View v) {
            super(v);
            card       = v.findViewById(R.id.card);
            tvExpDest  = v.findViewById(R.id.tvExpDest);
            tvContenu  = v.findViewById(R.id.tvContenu);
            tvDate     = v.findViewById(R.id.tvDate);
            tvType     = v.findViewById(R.id.tvType);
            tvNocde    = v.findViewById(R.id.tvNocde);
        }
    }
}
