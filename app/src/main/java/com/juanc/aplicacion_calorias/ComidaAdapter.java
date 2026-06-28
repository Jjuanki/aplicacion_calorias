package com.juanc.aplicacion_calorias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ComidaAdapter extends RecyclerView.Adapter<ComidaAdapter.ComidaHolder> {
    private List<Comida> comidas = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Comida comida);
        void onDeleteClick(Comida comida);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComidaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comida, parent, false);
        return new ComidaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidaHolder holder, int position) {
        Comida currentComida = comidas.get(position);
        holder.textViewNombre.setText(currentComida.getNombre());
        holder.textViewCalorias.setText(currentComida.getCalorias() + " kcal");
    }

    @Override
    public int getItemCount() {
        return comidas.size();
    }

    public void setComidas(List<Comida> comidas) {
        this.comidas = comidas;
        notifyDataSetChanged();
    }

    class ComidaHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombre;
        private TextView textViewCalorias;
        private ImageView btnDelete;

        public ComidaHolder(View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.tvComidaNombre);
            textViewCalorias = itemView.findViewById(R.id.tvComidaCalorias);
            btnDelete = itemView.findViewById(R.id.btnDeleteComida);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(comidas.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(comidas.get(position));
                }
            });
        }
    }
}