package com.skripsi.cuanku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skripsi.cuanku.fragment.CatatanFragment;
import com.skripsi.cuanku.fragment.ProfileFragment;
import com.skripsi.cuanku.fragment.TargetFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set default di targetfragemnt
        loadFragment(new TargetFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    //mengganti tampilan sesuai dengan fragment yang dipilih
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }

    //pemilihan navbar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.target_menu:
                fragment = new TargetFragment();
                break;
            case R.id.catatan_menu:
                fragment = new CatatanFragment();
                break;
            case R.id.profile_menu:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);
    }

//    @Override
//    protected void onStart() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//
//        super.onStart();
//        if (user.getDisplayName() == null) {
//            startActivity(new Intent(MainActivity.this, EditProfilActivity.class));
//            finish();
//        }
//    }
}
