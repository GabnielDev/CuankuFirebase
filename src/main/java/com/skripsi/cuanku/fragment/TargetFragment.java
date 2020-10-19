package com.skripsi.cuanku.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skripsi.cuanku.Add;
import com.skripsi.cuanku.R;
import com.skripsi.cuanku.reminder.ReminderActivity;
import com.skripsi.cuanku.tabungan.LihatSemuaTargetActivity;
import com.skripsi.cuanku.tabungan.TambahTargetActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TargetFragment extends Fragment {
    FirebaseAuth mauth;
    DatabaseReference lihattargetRef;


    TextView txtNamaPengguna;
    RecyclerView rvlihatdatatarget;
    Button btnAturReminder;


    public TargetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_target, container, false);
        // Inflate the layout for this fragment


        //inisialisasi
        lihattargetRef = FirebaseDatabase.getInstance().getReference().child("perencanaan/target");
        mauth = FirebaseAuth.getInstance();

        txtNamaPengguna = view.findViewById(R.id.txtNamaPengguna);
        btnAturReminder = view.findViewById(R.id.btnAturReminder);
//        rvlihatdatatarget = view.findViewById(R.id.rvlihatdatatarget);
//        rvlihatdatatarget.setLayoutManager(new LinearLayoutManager(getContext()));
        RelativeLayout rlTarget = view.findViewById(R.id.etTarget);
        RelativeLayout rlTabungan = view.findViewById(R.id.etTabungan);
        LinearLayout txtlihatsemuatarget = view.findViewById(R.id.txtlihatsemuatarget);


        //proses klik
        txtlihatsemuatarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LihatSemuaTargetActivity.class);
                startActivity(intent);
            }
        });

        rlTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahTargetActivity.class);
                startActivity(intent);
            }
        });

        rlTabungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Add.class);
                startActivity(intent);
            }
        });

        btnAturReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReminderActivity.class);
                startActivity(intent);
            }
        });


        //method
        checkUserStatus();

        return view;
    }


    private void init() {

    }

    private void checkUserStatus() {
        //getCurrentuser
        FirebaseUser user = mauth.getCurrentUser();
        assert user != null;
        if (user.getDisplayName() != null) {
            txtNamaPengguna.setText(user.getDisplayName());
        }
    }

    @Override
    public void onStart() {
        checkUserStatus();
        super.onStart();
    }

}

