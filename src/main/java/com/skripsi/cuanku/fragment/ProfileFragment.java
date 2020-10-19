package com.skripsi.cuanku.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skripsi.cuanku.akun.EditProfilActivity;
import com.skripsi.cuanku.akun.LoginActivity;
import com.skripsi.cuanku.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final int CHOOSE_IMAGE = 101;

    private ImageView imgFotoProfil;
    FirebaseAuth firebaseAuth;
    ImageButton btnLogout;
    ImageButton btnEdit;
    TextView userEmail;
    ProgressDialog progressDialog;
    TextView txtProfilNama;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init
        progressDialog = new ProgressDialog(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        userEmail = view.findViewById(R.id.txtprofileEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEdit = view.findViewById(R.id.btnEdit);
        imgFotoProfil = view.findViewById(R.id.imgFotoProfil);
        txtProfilNama = view.findViewById(R.id.txtProfilNama);

        //proses klik
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUserStatus();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfilActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


//    private void loadUserInformation() {
//         FirebaseUser user = auth.getCurrentUser();
//
//        if (user != null) {
//
//            if (user.getDisplayName() != null) {
//                txtProfilNama.setText(user.getEmail());
//            }
//        }
//    }

//    private void showDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setCancelable(false);
//        builder.setMessage("Ingin Keluar Aplikasi ?");
//        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                getActivity().finish();
//            }
//        });
//        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
////        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
////
////        //set title dialog
////        alertDialogBuilder.setTitle("Keluar Dari Aplikasi ?");
////        //set pesan dari dialog
////        alertDialogBuilder
////                .setMessage("Klik Ya untuk Keluar")
////                .setIcon(R.mipmap.ic_launcher)
////                .setCancelable(false)
////                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        //jika button diklik maka aplikasi akan ditutup
////                        startActivity(new Intent(getActivity(), SplashScreenActivity.class));
////                        getActivity().finish();
//////                        ProfileFragment.this.getActivity().finish();
////                    }
////                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                //jika button diklik maka akan menutup dialog
////                dialog.cancel();
////            }
////        });
////
////
////
////        //membuat aler dialog dari builder
////        AlertDialog alertDialog = alertDialogBuilder.create();
////
////        //menampilkan alert dialog
////        alertDialog.show();
//    }

    private void checkUserStatus() {
        //getCurrentuser
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //user is signed in stay here
            userEmail.setText(user.getEmail());
            if (user.getDisplayName() != null) {
                txtProfilNama.setText(user.getDisplayName());
            }
            if (user.getPhotoUrl() != null) {
                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(user.getPhotoUrl().toString())
                        .into(imgFotoProfil);
            }

        }
        else {
            //user not signed in, go to main activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @Override
    public void onStart() {
        //check on start app
        super.onStart();
        checkUserStatus();
    }
}
