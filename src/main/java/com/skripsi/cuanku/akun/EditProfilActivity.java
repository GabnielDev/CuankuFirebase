package com.skripsi.cuanku.akun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.bumptech.glide.Glide;
import com.skripsi.cuanku.MainActivity;
import com.skripsi.cuanku.R;

import java.io.IOException;
import java.util.Objects;

public class EditProfilActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;

    ProgressBar progressBar;
    ImageView imgFoto;
    EditText edtNama;
    Button btneditSave;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton btnAmbilFoto;
    Uri urImage;

    String imageUrl;

    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        //inisiasi
        imgFoto = findViewById(R.id.editFoto);
        edtNama = findViewById(R.id.editNama);
        btneditSave = findViewById(R.id.btnEditSave);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        progressBar = findViewById(R.id.progress_bar);
        auth = FirebaseAuth.getInstance();
        btnAmbilFoto = findViewById(R.id.btnAmbilFoto);

        //refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserInformation();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        loadUserInformation();

        btnAmbilFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanGambar();
            }
        });

        btneditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
                Intent intent = new Intent(EditProfilActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadUserInformation() {
        final FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imgFoto);
            }
            if (user.getDisplayName() != null) {
                edtNama.setText(user.getDisplayName());
            }
        }
    }

    private void saveUserInformation() {
        String displayName = edtNama.getText().toString();

        if (displayName.isEmpty()) {
            edtNama.setError("Name Requirred");
            edtNama.requestFocus();
            return;
        }

        user = auth.getCurrentUser();

        if (user != null && imageUrl != null) {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build();

            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfilActivity.this, "Profile Update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            urImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), urImage);
                imgFoto.setImageBitmap(bitmap);

                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference("pict/" + System.currentTimeMillis() + ".jpg");

        if (urImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            storageReference.putFile(urImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressBar.setVisibility(View.GONE);
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadUri = uriTask.getResult();


                            assert downloadUri != null;
                            imageUrl = Objects.requireNonNull(downloadUri).toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void tampilkanGambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent
                .createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);
    }
}
