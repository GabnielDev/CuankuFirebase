package com.skripsi.cuanku.catatan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.skripsi.cuanku.model.Catatan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TambahCatatanActivity extends AppCompatActivity {
    private SimpleDateFormat dateFormat;
    EditText edtTCjudul;
    EditText edtTCjumlah;
    EditText txtsetTanggal;
    Button btntcBatal, btntcSimpan;
    Spinner spinner;
    ImageView pilihtanggal;
    String mdepo, mtabung;
    TextView mdepodompet;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_catatan);

        mdepodompet = findViewById(R.id.depodompet);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("perencanaan");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mdepo = bundle.getString("depodompet");
            assert mdepo != null;
            mtabung = mdepo.toString().trim();

            mdepodompet.setText(mdepo);
        }


        //method
        init();
        batal();
        simpan();


        //proses tanggal
        pilihtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void saveDepo() {

        if (spinner.getSelectedItem().toString().equals("Pemasukan")) {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("perencanaan/catatan/deposit");

            Query query = reference1.orderByChild("nama").equalTo(mUser.getDisplayName());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer hasil;
                    hasil = Integer.valueOf(mtabung) + Integer.valueOf(edtTCjumlah.getText().toString());
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().child("hasil").setValue(hasil.toString());
                            ds.getRef().child("nama").setValue(mUser.getDisplayName());
                        }
                    } else {
                        submithasil(new Catatan(hasil.toString(), mUser.getDisplayName()));
                        Toast.makeText(TambahCatatanActivity.this, "Awal", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TambahCatatanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (spinner.getSelectedItem().toString().equals("Pengeluaran")) {
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("perencanaan/catatan/deposit");

            Query query = reference2.orderByChild("nama").equalTo(mUser.getDisplayName());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer hasil;
                    hasil = Integer.valueOf(mtabung) - Integer.valueOf(edtTCjumlah.getText().toString());
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().child("hasil").setValue(hasil.toString());
                            ds.getRef().child("nama").setValue(mUser.getDisplayName());
                        }
                    } else {
                        Toast.makeText(TambahCatatanActivity.this, "Data masih kosong ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TambahCatatanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void simpan() {
        final String muser = mUser.getDisplayName().toString();
        final Catatan catatan = (Catatan) getIntent().getSerializableExtra("data");

        if (catatan != null) {
            edtTCjudul.setText(catatan.getKeterangan());
            txtsetTanggal.setText(catatan.getTanggal());
            edtTCjumlah.setText(catatan.getJumlah());
            btntcSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catatan.setKeterangan(edtTCjudul.getText().toString());
                    catatan.setTanggal("0");
                    catatan.setTipe(spinner.getSelectedItem().toString());
                    catatan.setJumlah("0");
                    catatan.setNama(muser);
                }
            });
        } else {
            btntcSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    saveDepo();
                    if (spinner.getSelectedItem().toString().equals("Pemasukan")) {
                        Integer hasil;
                        hasil = Integer.valueOf(mtabung) + Integer.valueOf(edtTCjumlah.getText().toString());
                        if (!isEmpty(edtTCjudul.getText().toString()) &&
                                !isEmpty(txtsetTanggal.getText().toString()) &&
                                !isEmpty(spinner.getSelectedItem().toString()) &&
                                !isEmpty(edtTCjumlah.getText().toString())) {
                            submitCatatan(new Catatan(edtTCjudul.getText().toString(),
                                    txtsetTanggal.getText().toString(),
                                    spinner.getSelectedItem().toString(),
                                    edtTCjumlah.getText().toString(),
                                    hasil.toString(), muser));
//                            submithasil(new Catatan(hasil.toString(), muser));
                        } else {
                            Snackbar.make(findViewById(R.id.btntcSimpan), "Data Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(edtTCjudul.getWindowToken(), 0);
                        }
                    } else {
                        Integer hasil;
                        hasil = Integer.valueOf(mtabung) - Integer.valueOf(edtTCjumlah.getText().toString());
                        if (!isEmpty(edtTCjudul.getText().toString()) &&
                                !isEmpty(txtsetTanggal.getText().toString()) &&
                                !isEmpty(spinner.getSelectedItem().toString()) &&
                                !isEmpty(edtTCjumlah.getText().toString())) {
                            submitCatatan(new Catatan(edtTCjudul.getText().toString(),
                                    txtsetTanggal.getText().toString(),
                                    spinner.getSelectedItem().toString(),
                                    edtTCjumlah.getText().toString(),
                                    hasil.toString(), muser));

//                            submithasil(new Catatan(hasil.toString(), muser));
                        } else {
                            Snackbar.make(findViewById(R.id.btntcSimpan), "Data Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(edtTCjudul.getWindowToken(), 0);
                        }
                    }

                }

            });
        }
    }

    private void submithasil(Catatan catatan) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("perencanaan");
        databaseReference.child("catatan/deposit").push().setValue(catatan).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    private void submitCatatan(Catatan catatan) {
        reference.child("catatan/history").push().setValue(catatan).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                edtTCjudul.setText("");
                txtsetTanggal.setText("");
                edtTCjumlah.setText("");
                Snackbar.make(findViewById(R.id.btntcSimpan), "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    private void batal() {
        btntcBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahCatatanActivity.this.finish();
            }
        });
    }

    private void init() {
        btntcBatal = findViewById(R.id.btntcBatal);
        btntcSimpan = findViewById(R.id.btntcSimpan);
        spinner = findViewById(R.id.spinner);
        txtsetTanggal = findViewById(R.id.txtsettanggal);
        pilihtanggal = findViewById(R.id.pilihtanggal);
        edtTCjudul = findViewById(R.id.edtJudul);
        edtTCjumlah = findViewById(R.id.edtJumlah);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    }

    private void input(EditText txt, String s) {
        txt.setError(s + " " + "Tidak Boleh Kosong");
        txt.requestFocus();
    }

    private void showDialog() {
        //calendar untuk mendapatkan tanggal
        Calendar newCalendar = Calendar.getInstance();

        //initiate date picker
        //method ini dipanggil saat kita selesai memilih tanggal didate picker
        //set calendar untuk menampung tanggal yang dipilih
        //update textview dengan tanggal yang kita pilih
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //method ini dipanggil saat kita selesai memilih tanggal didate picker
                //set calendar untuk menampung tanggal yang dipilih

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                //update textview dengan tanggal yang kita pilih
                txtsetTanggal.setText("" + dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

//    private void getCatatan() {
//        reference = database.getReference("perencanaan/catatan");
//
//        Query query = reference.orderByChild("nama").equalTo(mUser.getDisplayName());
//
//        options = new FirebaseRecyclerOptions.Builder<Catatan>()
//                .setQuery(query, Catatan.class).build();
//
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Catatan, ViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Catatan catatan) {
//                viewHolder.setCatatan(getApplicationContext(), catatan.getKeterangan(), catatan.getJumlah(), catatan.getTanggal(), catatan.getDeposit());
//
//                cvdeposit.setText(catatan.getDeposit());
//            }
//
//            @NonNull
//            @Override
//            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.cardview_lihatcatatan,parent, false);
//
//                ViewHolder  viewHolder = new ViewHolder(itemView);
//
//                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
////                        String mketerangancatatan = getItem(position).getKeterangan();
////                        String mjumlahcatatan = getItem(position).getJumlah();
////                        String mtanggalcatatan = getItem(position).getTanggal();
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//
//                    }
//                });
//
//                return viewHolder;
//            }
//        };
//        recyclerView.setLayoutManager(layoutManager);
//        firebaseRecyclerAdapter.startListening();
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//
//    }


}
