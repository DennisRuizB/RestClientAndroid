package edu.dsa.dennis.restclientandroid_v2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {

    ArrayList<String> listDatos;
    private OnItemClickListener listener;

    // Interfaz para manejar los clics
    public interface OnItemClickListener {
        void onItemClick(Track track); // track será el objeto de la canción
    }

    // Constructor con listener
    public AdapterDatos(ArrayList<String> listDatos, OnItemClickListener listener) {
        this.listDatos = listDatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listDatos.get(position));

        // Agregar el listener de clic
        holder.itemView.setOnClickListener(v -> {
            Track track = parseTrackFromString(listDatos.get(position)); // Parsear Track desde los datos
            listener.onItemClick(track); // Llamar al listener
        });
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView dato;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            dato = itemView.findViewById(R.id.idDato);
        }

        public void asignarDatos(String datos) {
            dato.setText(datos);
        }
    }

    // Método auxiliar para convertir el string del listDatos a Track
    private Track parseTrackFromString(String datos) {
        String[] parts = datos.split("\n");
        String id = parts[0].split(": ")[1];  // Extraer el ID
        String cantante = parts[1].split(": ")[1];  // Extraer el cantante
        String cancion = parts[2].split(": ")[1];  // Extraer la canción
        return new Track(id, cancion, cantante);
    }
}
