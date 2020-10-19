package com.skripsi.cuanku.tabungan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.skripsi.cuanku.model.Target;

import java.text.NumberFormat;
import java.util.Locale;


public class
TambahTargetActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("perencanaan");

    EditText edtNamaTarget;
    EditText edtNominalTarget;
    EditText edtDurasiTarget;
    Button btnSimpanTarget;

    TextView txttargetharian, txttargetbulanan;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String cnamaTar, cnominal, cdurasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_target);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatrupiah = NumberFormat.getCurrencyInstance(localeID);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            cnamaTar = bundle.getString("editnamatarget");
            cnominal = bundle.getString("editharga");
            cdurasi = bundle.getString("editjangkawaktu");

            edtNamaTarget.setText(cnamaTar);
            edtNominalTarget.setText(cnominal);
            edtDurasiTarget.setText(cdurasi);
            btnSimpanTarget.setText("update");

        }

        //method
        initiation();
        prosessimpan();

    }

    private void initiation() {
        edtNamaTarget = findViewById(R.id.edtNamaTarget);
        edtNominalTarget = findViewById(R.id.edtNominalTarget);
        edtDurasiTarget = findViewById(R.id.edtDurasiTarget);
        btnSimpanTarget = findViewById(R.id.btnSaveTarget);
        txttargetharian = findViewById(R.id.txttabunganharian);
        txttargetbulanan = findViewById(R.id.txttabunganbulanan);
    }

    private void prosessimpan() {
        final String muser = user.getDisplayName().toString();
        final String uid = user.getUid();
        final String sisatarget = new Target().getSisatarget();
        final Target target= (Target) getIntent().getSerializableExtra("data");

        if (target != null) {
            edtNamaTarget.setText(target.getNamatarget());
            edtNominalTarget.setText(target.getNominaltarget());
            edtDurasiTarget.setText(target.getDurasitarget());
            btnSimpanTarget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    target.setNamatarget(edtNamaTarget.getText().toString());
                    target.setNominaltarget("0");
                    target.setDurasitarget("0");
                    target.setNama(muser);
                }
            });
        } else {
            btnSimpanTarget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perhitunganharian();
                    perhitunganbulanan();
                    if (!isEmpty(edtDurasiTarget.getText().toString()) &&
                            !isEmpty(edtNamaTarget.getText().toString()) &&
                            !isEmpty(edtNominalTarget.getText().toString()) &&
                            !isEmpty(txttargetharian.getText().toString()) &&
                            !isEmpty(txttargetbulanan.getText().toString())) {
                        submitTarget(new Target(edtNamaTarget.getText().toString(),
                                edtNominalTarget.getText().toString(),
                                edtDurasiTarget.getText().toString(),
                                txttargetharian.getText().toString(),
                                txttargetbulanan.getText().toString(),
                                muser, edtNominalTarget.getText().toString())
                                );
                    } else {
                        Snackbar.make(findViewById(R.id.btnSaveTarget), "Data Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(edtNamaTarget.getWindowToken(), 0);
                    }
                }
            });
        }
    }

    private void perhitunganbulanan() {
        Locale locale = new Locale("in", "ID");
        NumberFormat formatrupiah = NumberFormat.getCurrencyInstance(locale);

        int nominal = Integer.parseInt(edtNominalTarget.getText().toString().trim());
        int durasi = Integer.parseInt(edtDurasiTarget.getText().toString().trim());
        int nm = nominal / durasi;

        txttargetbulanan.setText(Integer.toString(nm));
    }

    private void perhitunganharian() {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatrupiah = NumberFormat.getCurrencyInstance(localeID);

        int day = 30;
        int nominal = Integer.parseInt(edtNominalTarget.getText().toString().trim());
        int durasi = Integer.parseInt(edtDurasiTarget.getText().toString().trim());
        int target = durasi * day;
        int nm = nominal / target;

        txttargetharian.setText(Integer.toString(nm));
    }

    private void updateTarget() {
        final String editnamatarget = edtNamaTarget.getText().toString();
        final String editnominal = edtNominalTarget.getText().toString();
        final String editdurasi = edtDurasiTarget.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("perencanaan/target");

        Query query = reference.orderByChild("namatarget").equalTo(cnamaTar);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().child("namatarget").setValue(editnamatarget);
                    ds.getRef().child("nominal").setValue(editnominal);
                    ds.getRef().child("durasitarget").setValue(editdurasi);
                }
                startActivity(new Intent(TambahTargetActivity.this, LihatSemuaTargetActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void submitTarget(Target target) {
        String sisa = "";
        databaseReference.child("target").push().setValue(target).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                edtNamaTarget.setText("");
                edtNominalTarget.setText("");
                edtDurasiTarget.setText("");
                txttargetharian.setText("");
                txttargetbulanan.setText("");
                Snackbar.make(findViewById(R.id.btnSaveTarget), "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

}
