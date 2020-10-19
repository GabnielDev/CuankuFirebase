package com.skripsi.cuanku.tabungan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.skripsi.cuanku.R;
import com.skripsi.cuanku.ViewHolder;
import com.skripsi.cuanku.model.Target;


public class LihatSemuaTargetActivity extends AppCompatActivity {

    private RecyclerView rvlihatsemuatarget;

    private DatabaseReference reference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Target, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Target> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_semua_target);

        reference = FirebaseDatabase.getInstance().getReference("perencanaan/target");
        reference.keepSynced(true);

        rvlihatsemuatarget = findViewById(R.id.rvlihatsemuatarget);
        layoutManager = new LinearLayoutManager(this);

        rvlihatsemuatarget.setHasFixedSize(true);

        //method
        tampilsemuaTarget();

    }

    private void tampilsemuaTarget() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("perencanaan/target");

        Query query = databaseReference.orderByChild("nama").equalTo(user.getDisplayName());

        options = new FirebaseRecyclerOptions.Builder<Target>()
                .setQuery(query, Target.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Target, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Target target) {
                viewHolder.setTarget(getApplicationContext(), target.getNamatarget(), target.getNominaltarget(),
                        target.getDurasitarget(), target.getHariantarget());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_lihattarget, parent, false);

                ViewHolder viewHolder = new ViewHolder(itemView);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String mnamatarget = getItem(position).getNamatarget();
                        String mnominaltarget = getItem(position).getNominaltarget();
                        String mdurasitarget = getItem(position).getDurasitarget();
                        String mhariantarget = getItem(position).getHariantarget();
                        String mbulanantarget = getItem(position).getBulanantarget();
                        String mSisaTarget = getItem(position).getSisatarget();

                        Intent intent = new Intent(LihatSemuaTargetActivity.this, DetailTargetActivity.class);
                        intent.putExtra("detailnamatarget", mnamatarget);
                        intent.putExtra("detailnominaltarget", mnominaltarget);
                        intent.putExtra("detaildurasitarget", mdurasitarget);
                        intent.putExtra("detailhariantarget", mhariantarget);
                        intent.putExtra("detailbulanantarget", mbulanantarget);
                        intent.putExtra("detailsisatarget", mSisaTarget);

                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        final String editdtnamatarget = getItem(position).getNamatarget();
                        final String editdtnominal = getItem(position).getNominaltarget();
                        final String editdtdurasi = getItem(position).getDurasitarget();

                        AlertDialog.Builder builder = new AlertDialog.Builder(LihatSemuaTargetActivity.this);
                        String[] options = {
                                "Update",
                                "Delete"
                        };
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(LihatSemuaTargetActivity.this, TambahTargetActivity.class);
                                    intent.putExtra("editnamatarget", editdtnamatarget);
                                    intent.putExtra("editnominaltarget", editdtnominal);
                                    intent.putExtra("editdurasitarget", editdtdurasi);
                                    startActivity(intent);
                                } else if (which == 1) {
                                    showDeleteDialog(editdtnamatarget);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });
                return viewHolder;
            }
        };
        rvlihatsemuatarget.setLayoutManager(layoutManager);
        firebaseRecyclerAdapter.startListening();
        rvlihatsemuatarget.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDeleteDialog(final String currentNamaTarget) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Yakin ingin Menghapus ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = reference.orderByChild("namatarget").equalTo(currentNamaTarget);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(LihatSemuaTargetActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LihatSemuaTargetActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

