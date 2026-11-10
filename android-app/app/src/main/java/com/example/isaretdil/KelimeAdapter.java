package com.example.isaretdil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class KelimeAdapter extends RecyclerView.Adapter<KelimeAdapter.KelimeViewHolder> {

    private List<Kelime> kelimeListesi;
    private List<Kelime> kelimeListesiTum;
    private OnKelimeClickListener listener;

    public interface OnKelimeClickListener {
        void onKelimeClick(Kelime kelime);
    }

    public KelimeAdapter(List<Kelime> kelimeListesi, OnKelimeClickListener listener) {
        this.kelimeListesi    = new ArrayList<>(kelimeListesi);
        this.kelimeListesiTum = new ArrayList<>(kelimeListesi);
        this.listener         = listener;
    }

    @NonNull
    @Override
    public KelimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kelime, parent, false);
        return new KelimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KelimeViewHolder holder, int position) {
        Kelime kelime = kelimeListesi.get(position);

        holder.kelimeAdi.setText(kelime.getGorunenAd());
        holder.kelimeIngilizce.setText(kelime.getIngilizce());

        // Son elemanda ayırıcı çizgiyi gizle
        holder.ayirici.setVisibility(
                position < kelimeListesi.size() - 1 ? View.VISIBLE : View.GONE
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onKelimeClick(kelime);
        });
    }

    @Override
    public int getItemCount() {
        return kelimeListesi.size();
    }

    public void filtrele(String text) {
        kelimeListesi.clear();
        if (text == null || text.trim().isEmpty()) {
            kelimeListesi.addAll(kelimeListesiTum);
        } else {
            String aranan = turkceKucuk(text.trim());
            for (Kelime item : kelimeListesiTum) {
                // Hem Türkçe hem İngilizce içinde ara
                if (turkceKucuk(item.getGorunenAd()).contains(aranan)
                        || item.getIngilizce().toLowerCase().contains(aranan)) {
                    kelimeListesi.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    private String turkceKucuk(String s) {
        return s.replace("İ","i").replace("I","ı")
                .replace("Ğ","ğ").replace("Ü","ü")
                .replace("Ş","ş").replace("Ö","ö")
                .replace("Ç","ç").toLowerCase();
    }

    static class KelimeViewHolder extends RecyclerView.ViewHolder {
        TextView kelimeAdi, kelimeIngilizce;
        View ayirici;

        public KelimeViewHolder(@NonNull View itemView) {
            super(itemView);
            kelimeAdi       = itemView.findViewById(R.id.kelimeAdi);
            kelimeIngilizce = itemView.findViewById(R.id.kelimeIngilizce);
            ayirici         = itemView.findViewById(R.id.ayiriciCizgi);
        }
    }
}