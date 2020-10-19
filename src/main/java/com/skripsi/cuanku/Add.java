package com.skripsi.cuanku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.DateFormat;
import java.util.Calendar;

public class Add extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgCalendar;
    private EditText etSetDate;
    private EditText etNominal;
    private EditText etName;
    private Button btnAdd;

    String durasi, bulanan, harian, nama, nominal, sisa;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseRecyclerAdapter<Target, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Target> options;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tabungan);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("perencanaan");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        init();
        getDate();
        recyclerView();
        getTarget();

    }

    public void getDate() {
        Calendar calendar = Calendar.getInstance();
        String currentCalendar = DateFormat.getDateInstance(DateFormat.FULL)
                .format(calendar.getTime());

        etSetDate.setText(currentCalendar);
    }

    public void init() {
        imgCalendar = findViewById(R.id.pilihtanggalmenabung);
        etSetDate = findViewById(R.id.txtsettanggalmenabung);
        etNominal = findViewById(R.id.edtNominalTabungan);
        etName = findViewById(R.id.edtNama);
        recyclerView = findViewById(R.id.rvTarget);
        btnAdd = findViewById(R.id.btnTambahTabungan);

        btnAdd.setOnClickListener(this);
    }

    public void recyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
    }

    public void getTarget() {
        reference = database.getReference("perencanaan/target");

        Query query = reference.orderByChild("nama").equalTo(mUser.getDisplayName());

        options = new FirebaseRecyclerOptions.Builder<Target>()
                .setQuery(query, Target.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Target, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Target target) {
                viewHolder.setNamaTarget(getApplicationContext(), target.getNamatarget(), target.getNominaltarget());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_namatarget, parent, false);

                ViewHolder viewHolder = new ViewHolder(itemView);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        etName.setText(getItem(position).getNamatarget());
                        nominal = getItem(position).getNominaltarget();
                        sisa = getItem(position).getSisatarget();
                        durasi = getItem(position).getDurasitarget();
                        bulanan = getItem(position).getBulanantarget();
                        harian = getItem(position).getHariantarget();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolder;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void addEarns() {

        final String money = etNominal.getText().toString();
        final String name = etName.getText().toString();
        final long result = Integer.valueOf(sisa) - Integer.valueOf(money);

        reference = database.getReference("perencanaan/target");

        Query query = reference.orderByChild("namatarget").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().child("namatarget").setValue(name);
                    ds.getRef().child("durasitarget").setValue(durasi);
                    ds.getRef().child("bulanantarget").setValue(bulanan);
                    ds.getRef().child("hariantarget").setValue(harian);
                    ds.getRef().child("nama").setValue(mUser.getDisplayName());
                    ds.getRef().child("nominaltarget").setValue(nominal);
                    ds.getRef().child("sisatarget").setValue(Long.toString(result));
                }
                etNominal.setText(null);
                etName.setText(null);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Add.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTambahTabungan:
                addEarns();
                break;
        }
    }
}
