package com.skripsi.cuanku.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.skripsi.cuanku.ViewHolder;
import com.skripsi.cuanku.catatan.LihatDataCatatanActivity;
import com.skripsi.cuanku.R;
import com.skripsi.cuanku.catatan.TambahCatatanActivity;
import com.skripsi.cuanku.model.Catatan;
import com.skripsi.cuanku.model.Target;
import com.skripsi.cuanku.tabungan.LihatSemuaTargetActivity;
import com.skripsi.cuanku.tabungan.TambahTargetActivity;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatatanFragment extends Fragment {
    Button btnTambahCatatan, btnLihatDataCatatan;
    TextView txtTanggalSekarang, cvdeposit;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    FirebaseRecyclerAdapter<Catatan, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Catatan> options;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String depo;

    public CatatanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catatan, container, false);
        //initiation
        recyclerView = view.findViewById(R.id.rvfragmentcatatan);
        btnTambahCatatan = view.findViewById(R.id.btnTambahCatatan);
        btnLihatDataCatatan = view.findViewById(R.id.btnTampilCatatan);
        cvdeposit = view.findViewById(R.id.cvdeposit);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("catatan");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Calendar
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        txtTanggalSekarang = view.findViewById(R.id.txtTglSekarang);
        txtTanggalSekarang.setText(currentDate);


        //method
//        getCatatan();
//        recyclerView();

        //proses
        btnTambahCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (depo == null) {
                    depo = "0";
                } else {
                    depo = cvdeposit.getText().toString();
                }
                Intent intent = new Intent(getActivity(), TambahCatatanActivity.class);
                intent.putExtra("depodompet", cvdeposit.getText().toString().trim());
                startActivity(intent);
            }
        });

        btnLihatDataCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LihatDataCatatanActivity.class);
                startActivity(intent);
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);

        getCatatan();
        getDepo();

        // Inflate the layout for this fragment
        return view;
    }

    private void getCatatan() {
        reference = database.getReference("perencanaan/catatan/history");

        Query query = reference.orderByChild("nama").equalTo(mUser.getDisplayName());

        options = new FirebaseRecyclerOptions.Builder<Catatan>()
                .setQuery(query, Catatan.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Catatan, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Catatan catatan) {
                viewHolder.setCatatan(getContext(), catatan.getKeterangan(), catatan.getJumlah(), catatan.getTanggal(), catatan.getDeposit());

//                cvdeposit.setText(catatan.getDeposit());
//                depo = catatan.getDeposit();
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_lihatcatatan, parent, false);

                ViewHolder viewHolder = new ViewHolder(itemView);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        String mketerangancatatan = getItem(position).getKeterangan();
//                        String mjumlahcatatan = getItem(position).getJumlah();
//                        String mtanggalcatatan = getItem(position).getTanggal();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        final String editketerangan = getItem(position).getKeterangan();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        String[] options = {
                                "Update",
                                "Delete"
                        };
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
//                                    Intent intent = new Intent(getContext(), TambahTargetActivity.class);
//                                    intent.putExtra("editnamatarget", editdtnamatarget);
//                                    intent.putExtra("editnominaltarget", editdtnominal);
//                                    intent.putExtra("editdurasitarget", editdtdurasi);
//                                    startActivity(intent);
                                } else if (which == 1) {
                                    showDeleteDialog(editketerangan);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });

                return viewHolder;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void showDeleteDialog(final String currentKeterangan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Yakin ingin Menghapus ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference referenceket;
                referenceket = database.getReference("perencanaan/catatan/history");
                Query query = referenceket.orderByChild("keterangan").equalTo(currentKeterangan);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(getContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.create().show();
    }

    private void getDepo() {
        reference = FirebaseDatabase.getInstance().getReference("perencanaan/catatan/deposit");

        Query query = reference.orderByChild("nama").equalTo(mUser.getDisplayName());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Catatan catatan = snapshot.getValue(Catatan.class);
                cvdeposit.setText(catatan.getHasil());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Catatan catatan = snapshot.getValue(Catatan.class);
                cvdeposit.setText(catatan.getHasil());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
