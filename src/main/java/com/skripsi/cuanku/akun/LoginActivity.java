package com.skripsi.cuanku.akun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skripsi.cuanku.MainActivity;
import com.skripsi.cuanku.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button btnLogin;
    private TextView txtRegister;
    TextView txtlupaPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inisialisasi();
        belumpunyaakun();




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String email = loginEmail.getText().toString();
               String password = loginPassword.getText().toString().trim();
               if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                   //invalid email
                   loginEmail.setError("Email Tidak Sah");
                   loginEmail.setFocusable(true);
               }
               else {
                   //valid email
                   loginUser(email, password);
               }
            }
        });
        //lupa password
        txtlupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lupapassword();
            }
        });
        progressDialog = new ProgressDialog(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }



    private void lupapassword() {
        //alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set in dialog
        final EditText emailRecover = new EditText(this);
        emailRecover.setHint("Email");
        emailRecover.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailRecover.setMinEms(10);

        linearLayout.addView(emailRecover);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String email = emailRecover.getText().toString().trim();
                mulaiRecovery(email);

            }
        });
        //buttons recover
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });
        //show dialog
        builder.create().show();
    }

    private void mulaiRecovery(String email) {
        //show progress dialog
        progressDialog.setMessage("Sending Email...");
        progressDialog.show();
       auth.sendPasswordResetEmail(email)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       progressDialog.dismiss();
                       if (task.isSuccessful()) {
                           Toast.makeText(LoginActivity.this, "Email Dikirim...", Toast.LENGTH_SHORT).show();
                       }
                       else {
                           Toast.makeText(LoginActivity.this, "Gagal...", Toast.LENGTH_SHORT).show();
                       }

                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               //get and show proper error message
               Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }


    private void loginUser(String email, String password) {

        progressDialog.setMessage("Masuk...");
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            progressDialog.show();
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void inisialisasi() {
        //inisialisasi
        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.edtEmailLogin);
        loginPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtlupaPassword = findViewById(R.id.txtlupaPassword);
    }

    private void belumpunyaakun() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
