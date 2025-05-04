package com.example.crudmahasiswa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private List<JSONObject> mahasiswaList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(JSONObject item);
        void onEditClick(JSONObject item);
        void onDeleteClick(JSONObject item);
    }

    public MahasiswaAdapter(List<JSONObject> mahasiswaList, OnItemClickListener listener) {
        this.mahasiswaList = mahasiswaList;
        this.listener = listener;
    }

    public void updateData(List<JSONObject> newList) {
        this.mahasiswaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject item = mahasiswaList.get(position);
            holder.bind(item, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mahasiswaList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgMahasiswa;
        private TextView tvNama, tvNim, tvJurusan;
        private ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMahasiswa = itemView.findViewById(R.id.imgMahasiswa);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNim = itemView.findViewById(R.id.tvNim);
            tvJurusan = itemView.findViewById(R.id.tvJurusan);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(final JSONObject item, final OnItemClickListener listener) throws JSONException {
            String nama = item.getString("nama");
            String nim = item.getString("nim");
            String jurusan = item.getString("jurusan");

            tvNama.setText(nama);
            tvNim.setText(nim);
            tvJurusan.setText(jurusan);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditClick(item);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(item);
                }
            });
        }
    }
}