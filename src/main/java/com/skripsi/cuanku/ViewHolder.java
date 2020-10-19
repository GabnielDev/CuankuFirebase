package com.skripsi.cuanku;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.skripsi.cuanku.model.Target;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ViewHolder extends RecyclerView.ViewHolder {
    private DatabaseReference mReference;
    private ArrayList<Target> targets;

    View view;
    Context context;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

    }

    public void setNamaTarget(Context context, String namatarget, String valueTarget) {
        TextView txtnamatarget, tvValueTarget;

        txtnamatarget = view.findViewById(R.id.cvnamatarget);
        tvValueTarget = view.findViewById(R.id.cvvaluetarget);

        txtnamatarget.setText(namatarget);
        tvValueTarget.setText(valueTarget);
    }

    public void setTarget(Context context, String namatarget, String hargatarget, String durasitarget, String hariantarget) {
        Locale locale = new Locale("in", "ID");
        NumberFormat formatrupiah = NumberFormat.getCurrencyInstance(locale);

        TextView txtnamatarget, txthargabarang, txtdurasitarget, txthariantarget;

        txtnamatarget = view.findViewById(R.id.cvnamabarang);
        txthargabarang = view.findViewById(R.id.cvhargabarang);
        txtdurasitarget = view.findViewById(R.id.cvdurasi);
        txthariantarget = view.findViewById(R.id.cvharian);


        txtnamatarget.setText(namatarget);
        txthargabarang.setText("Rp." + hargatarget);
        txtdurasitarget.setText(durasitarget + " " + "Bulan");
        txthariantarget.setText("Rp." + hariantarget + "/hari");
    }

    public void setCatatan(Context context, String keterangancatatan, String jumlahcatatan, String tanggalcatatan, String depocatatan) {
        TextView txtketerangan, txtjumlah, txttanggal, txtdepo;

        txtketerangan = view.findViewById(R.id.cvketerangancatatan);
        txtjumlah = view.findViewById(R.id.cvjumlahcatatan);
        txttanggal = view.findViewById(R.id.cvtanggalcatatan);
        //txtdepo = view.findViewById(R.id.cvdeposit);

        txtketerangan.setText(keterangancatatan);
        txtjumlah.setText(jumlahcatatan);
        txttanggal.setText(tanggalcatatan);
        //txtdepo.setText(depocatatan);
    }


    private ViewHolder.ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        this.clickListener = clickListener;

    }
}
