package com.skripsi.cuanku.tabungan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.skripsi.cuanku.R;

public class DetailTargetActivity extends AppCompatActivity {
    private TextView detailnamatarget, detailnominal, detaildurasi, detailperbulan, detailperhari, detailsisa;

    private String mnamatarget, mnominal, mdurasi, mperbulan, mperhari, msisa;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_target);

        //inisialisasi


        //method
        initiation();
        getData();
    }

    private void initiation() {
        detailnamatarget = findViewById(R.id.dtnamatarget);
        detailnominal = findViewById(R.id.dtnominaltarget);
        detailperbulan = findViewById(R.id.dttabunganperbulan);
        detaildurasi = findViewById(R.id.dtjangkawaktu);
        detailperhari = findViewById(R.id.dttabunganperhari);
        detailsisa = findViewById(R.id.dtsisatabungan);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            mnamatarget = bundle.getString("detailnamatarget");
            mnominal = bundle.getString("detailnominaltarget");
            mdurasi = bundle.getString("detaildurasitarget");
            mperbulan = bundle.getString("detailbulanantarget");
            mperhari = bundle.getString("detailhariantarget");
            msisa = bundle.getString("detailsisatarget");

            detailnamatarget.setText(mnamatarget);
            detailnominal.setText("Rp." + mnominal);
            detaildurasi.setText(mdurasi + " " + "Bulan");
            detailperbulan.setText("Rp." + mperbulan);
            detailperhari.setText("Rp." + mperhari);
            detailsisa.setText("Rp. " + msisa);

        }
    }
}
