package com.skripsi.cuanku.catatan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.skripsi.cuanku.R;
import com.skripsi.cuanku.ViewHolder;
import com.skripsi.cuanku.model.Catatan;

public class LihatDataCatatanActivity extends AppCompatActivity {
    TextView cvdeposit;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    FirebaseRecyclerAdapter<Catatan, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Catatan> options;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data_catatan);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("catatan");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //method
        init();
        recyclerView();
        getCatatan();
    }


    private void getCatatan() {
        reference = database.getReference("perencanaan/catatan");

        Query query = reference.orderByChild("nama").equalTo(mUser.getDisplayName());

        options = new FirebaseRecyclerOptions.Builder<Catatan>()
                .setQuery(query, Catatan.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Catatan, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Catatan catatan) {
                viewHolder.setCatatan(getApplicationContext(), catatan.getKeterangan(), catatan.getJumlah(), catatan.getTanggal(), catatan.getDeposit());

                cvdeposit.setText(catatan.getDeposit());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_lihatcatatan,parent, false);

                ViewHolder  viewHolder = new ViewHolder(itemView);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        String mketerangancatatan = getItem(position).getKeterangan();
//                        String mjumlahcatatan = getItem(position).getJumlah();
//                        String mtanggalcatatan = getItem(position).getTanggal();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void recyclerView() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(false);
        recyclerView.setHasFixedSize(true);
    }

    private void init() {
        recyclerView = findViewById(R.id.rvLihatDataCatatan);
        cvdeposit = findViewById(R.id.cvdeposit);
    }

}
