package com.skripsi.cuanku.tabungan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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
import com.skripsi.cuanku.model.Tabungan;
import com.skripsi.cuanku.model.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TambahTabunganActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("perencanaan");
    private ArrayList<String> arrayList = new ArrayList<>();

    private SimpleDateFormat dateFormat;
    Spinner spinnertabungan;
    ImageView pilihtanggalmenabung;
    EditText txtsetTanggalmenabung;
    EditText edtNominalMenabung;
    Button btnsavemenabung;
    RecyclerView rvtampiltarget;

    Button btn;

    TextView setnominal, setnamatarget;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Target, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Target> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tabungan);


        //inisialisasi
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        spinnertabungan = findViewById(R.id.spinnertabungan);
        pilihtanggalmenabung = findViewById(R.id.pilihtanggalmenabung);
        txtsetTanggalmenabung = findViewById(R.id.txtsettanggalmenabung);
        edtNominalMenabung = findViewById(R.id.edtNominalTabungan);
        btnsavemenabung = findViewById(R.id.btnTambahTabungan);
        rvtampiltarget = findViewById(R.id.rvTarget);


        layoutManager = new LinearLayoutManager(this);
        rvtampiltarget.setHasFixedSize(true);

//        btn = findViewById(R.id.btn);

//        setnominal = findViewById(R.id.setnominal);
//        setnamatarget = findViewById(R.id.setnamatarget);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tampilnamatarget();
//                double nominal = Integer.valueOf(setnominal.getText().toString());
//                double menabung = Integer.valueOf(edtNominalMenabung.getText().toString());
//                double hasil = nominal - menabung;
//                setnominal.setText(Double.toString(hasil));
//            }
//        });

        //method
        showDataSpinner();
        tampilnamatarget();
//        prosessimpan();

        btnsavemenabung.setOnClickListener(this);

        //proses klik
        pilihtanggalmenabung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void prosessimpan() {
        tampilnamatarget();

        double nominal = Integer.valueOf(setnominal.getText().toString());
        double menabung = Integer.valueOf(edtNominalMenabung.getText().toString());
        final double hasil = nominal - menabung;
        final String muser = user.getDisplayName();
        final Tabungan tabungan1 = new Tabungan();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("tabungan/target");

        Query query = reference.orderByChild("namatarget").equalTo(setnamatarget.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().child("namatarget").setValue(tabungan1.getNamatarget());
                    ds.getRef().child("nominal").setValue(tabungan1.getNominal());
                    ds.getRef().child("sisatarget").setValue(Double.toString(hasil));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void tampilnamatarget() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("perencanaan/target");

        Query query = databaseReference.orderByChild("nama").equalTo(user.getDisplayName());

        options = new FirebaseRecyclerOptions.Builder<Target>()
                .setQuery(query, Target.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Target, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Target target) {
//                viewHolder.setNamaTarget(getApplicationContext(), target.getNamatarget());
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
                        setnominal.setText(getItem(position).getNominaltarget());
                        setnamatarget.setText(getItem(position).getNamatarget());

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolder;
            }
        };
        rvtampiltarget.setLayoutManager(layoutManager);
        firebaseRecyclerAdapter.startListening();
        rvtampiltarget.setAdapter(firebaseRecyclerAdapter);
    }

//    private void submitTabungan(Tabungan tabungan) {
//        databaseReference.child("tabungan").push().setValue(tabungan)
//        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                txtsetTanggalmenabung.setText("");
//                edtNominalMenabung.setText("");
//
//            }
//        });
//    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    private void showDialog() {
        //Untuk mendapatkan calendar
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //method ini dipanggil saat kita selesai memilih tanggal didate picker
                //set calendar untuk menampung tanggal yang dipilih

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                //update textview dengan tanggal yang kita pilih
                txtsetTanggalmenabung.setText("" + dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showDataSpinner() {

        DatabaseReference databaseLihatTarget = FirebaseDatabase.getInstance().getReference("perencanaan/target");
        final FirebaseUser user = auth.getCurrentUser();

        Query query = databaseLihatTarget.orderByChild("nama").equalTo(user.getDisplayName());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    arrayList.add(Objects.requireNonNull(item.child("namatarget").getValue()).toString());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TambahTabunganActivity.this, R.layout.style_spinner, arrayList);
                spinnertabungan.setAdapter(arrayAdapter);

//                spinnertabungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        databaseReference.child("target").equalTo(user.getDisplayName());
//                        for (DataSnapshot item1 : dataSnapshot.getChildren()) {
//                            Target target = new Target();
//                            target.setNominaltarget(item1.child("target/nominal").getValue(target.getClass()).getNominaltarget());
//                            setnominal.setText(target.getNominaltarget());
//                        }
//
//                    }
//                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTambahTabungan:
                prosessimpan();
                break;
        }
    }
}
