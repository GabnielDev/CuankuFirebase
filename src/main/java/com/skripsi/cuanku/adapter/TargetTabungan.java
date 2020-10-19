//package com.skripsi.cuanku.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.skripsi.cuanku.R;
////import com.skripsi.cuanku.model.Tabungan;
//import com.skripsi.cuanku.model.Target;
//
//import java.util.ArrayList;
//
//public class TargetTabungan extends RecyclerView.Adapter<TargetTabungan.ListViewHolder> {
//    ArrayList<Target> listTarget;
//    Context context;
//    FirebaseListener listener;
//
//    public TargetTabungan(Context cont, ArrayList<Target> list) {
//        context = cont;
//        listTarget = list;
//    }
//
//    @NonNull
//    @Override
//    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_lihattarget, parent, false);
//        return new ListViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
//        //menampilkan data pada view
//        final String namatarget = listTarget.get(position).getNamatarget();
//        System.out.println("BARANG DATA one by one " + position + listTarget.size());
////        holder.txtcvNamaBarang.setText(getNamatarget());
////        holder.txtcvHargaBarang.setText(perencanaan.getNominal());
////        holder.txtcvDurasiBarang.setText(perencanaan.getDurasitarget());
//
////        Tabungan perencanaan = listTarget.get(position);
//
////        holder.txtcvHarianBarang.setText(perencanaan.get());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return listTarget.size();
//    }
//
//    public class ListViewHolder extends RecyclerView.ViewHolder {
//        TextView txtcvNamaBarang, txtcvHargaBarang, txtcvDurasiBarang, txtcvHarianBarang;
//
//        public ListViewHolder(@NonNull View itemView) {
//            super(itemView);
//            txtcvNamaBarang = itemView.findViewById(R.id.cvnamabarang);
//            txtcvHargaBarang = itemView.findViewById(R.id.cvhargabarang);
//            txtcvDurasiBarang = itemView.findViewById(R.id.cvdurasi);
//            txtcvHarianBarang = itemView.findViewById(R.id.cvharian);
//        }
//    }
//
//    public interface FirebaseListener {
//        void onDeleteData(Target target, int position);
//    }
//
//}
