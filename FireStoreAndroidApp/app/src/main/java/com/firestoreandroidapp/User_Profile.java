package com.firestoreandroidapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class User_Profile extends AppCompatActivity {
    EditText etEmail, etpassword, etName, etDob, etCity;
    Button btnRegister;
    TextView tvLogin;
    FirebaseFirestore db;
    ProgressDialog loadingBar;
    FirebaseAuth fAuth;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setTitle("Profile Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        etName = (EditText) findViewById(R.id.etName);
        etDob = (EditText) findViewById(R.id.etDob);
        etCity = (EditText) findViewById(R.id.etCity);

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = db.collection("Db_Users_Details").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    etEmail.setText(documentSnapshot.getString("password_user"));
                    etName.setText(documentSnapshot.getString("name_user"));
                    etpassword.setText(documentSnapshot.getString("email_user"));
                    etCity.setText(documentSnapshot.getString("city_user"));
                    etDob.setText(documentSnapshot.getString("birthdate_user"));
                    if(documentSnapshot.getString("gender_user").equals("Male")){
                        radioMale.setChecked(true);
                    }
                    else {
                        radioFemale.setChecked(true);
                    }


                }else {
                    Toast.makeText(User_Profile.this, "Somethig went wrong please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}