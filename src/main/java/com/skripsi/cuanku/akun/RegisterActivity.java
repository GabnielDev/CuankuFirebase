package com.skripsi.cuanku.akun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skripsi.cuanku.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText registnamadepan;
    private EditText registnamabelakang;
    private EditText registEmail;
    private EditText registPassword;
    FirebaseAuth auth;
    private Button btnRegister;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initiation
        auth = FirebaseAuth.getInstance();

//        registnamadepan = findViewById(R.id.edtRegistNamaDepan);
//        registnamabelakang = findViewById(R.id.edtRegistNamaBelakang);
        registEmail = findViewById(R.id.edtEmailRegist);
        registPassword = findViewById(R.id.edtPasswordRegist);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        //proses di btn register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtemail = registEmail.getText().toString().trim();
                String txtpassword = registPassword.getText().toString().trim();
//                String txtnamadepan = registnamadepan.getText().toString().trim();
//                String txtnamabelakang = registnamadepan.getText().toString().trim();
                //validate
                if (!Patterns.EMAIL_ADDRESS.matcher(txtemail).matches()) {
                    //set error and focuss to email edittex
                    registEmail.setError("Email Tidak Valid");
                    registEmail.setFocusable(true);
                }
                else if (txtpassword.length() <6 ) {
                    //set error and focuss to password edittext
                    registPassword.setError("Password tidak boleh kurang dari 6 karakter");
                    registPassword.setFocusable(true);
                }
                else {
                    registerUser(txtemail, txtpassword);
                }
            }
        });

    }

    private void registerUser(String email, String password) {
        //pendaftaran berhasil menampilkan progress dialog dan memulai register user
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in berhasil
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Registrasi Berhasil...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, EditProfilActivity.class));
                            finish();
                        }
                        else {
                            //sign in gagal
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registrasi Gagal...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error, menghilangkan progressdialog dan menampilkan error message
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
