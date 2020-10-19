package com.skripsi.cuanku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.cuanku.R;
import com.skripsi.cuanku.model.Catatan;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CatatanHarian extends RecyclerView.Adapter<CatatanHarian.ListViewHolder> {
    ArrayList<Catatan> listCatatan;
    Context context;

    public CatatanHarian(Context cont, ArrayList<Catatan> list) {
        context = cont;
        listCatatan = list;
     }

    @NonNull
    @Override
    public CatatanHarian.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_lihatdatacatatan, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatatanHarian.ListViewHolder holder, int position) {
        Catatan catatan = listCatatan.get(position);
        holder.cvTanggal.setText(catatan.getTanggal());
    }

    @Override
    public int getItemCount() {
        return listCatatan.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView cvTanggal, cvPemasukan, cvPengeluaran;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            cvTanggal = itemView.findViewById(R.id.cvTanggal);
            cvPemasukan = itemView.findViewById(R.id.cvPemasukan);
            cvPengeluaran = itemView.findViewById(R.id.cvPengeluaran);
        }
    }
}
