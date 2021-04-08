package com.firestoreandroidapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class User_Registration extends Fragment {
    EditText username, useremail, userdob, usercity, userpassword;
    Button btnRegister;
    TextView tvLogin;
    FirebaseFirestore db;
    ProgressDialog loadingBar;
    FirebaseAuth fAuth;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;
    int mYear, mMonth, mDay;
    String DAY, MONTH, YEAR;
    View view;

    public User_Registration() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_registration, container, false);


        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioMale = (RadioButton) view.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) view.findViewById(R.id.radioFemale);

        useremail = (EditText) view.findViewById(R.id.useremail);
        userpassword = (EditText) view.findViewById(R.id.userpassword);
        username = (EditText) view.findViewById(R.id.username);
        userdob = (EditText) view.findViewById(R.id.userdob);
        usercity = (EditText) view.findViewById(R.id.usercity);
        userdob.setFocusable(false);

        userdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(getContext());
        selectGender();
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserRegistration();
            }
        });
        return view;
    }

    private void createUserRegistration() {

        String name = username.getText().toString().trim();
        String email = useremail.getText().toString().trim();
        String password = userpassword.getText().toString().trim();
        String city = usercity.getText().toString().trim();
        String dob = userdob.getText().toString().trim();
        int selectedId = radioGroup.getCheckedRadioButtonId();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedId == -1) {
            Toast.makeText(getContext(), "Please Choose Gender", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Email should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Password should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(getContext(), "City name should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(dob)) {
            Toast.makeText(getContext(), "Date of Birth should not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(getContext(), "Password Must be >= 6 Characters", Toast.LENGTH_SHORT).show();
            return;
        } else {
            loadingBar.setTitle("Please Wait");
            loadingBar.setMessage("Please wait,  while we are adding details.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            addingUserDetails(name, email, password, city, dob);
        }

    }

    String UserID;

    private void addingUserDetails(String name, String email, String password, String city, String dob) {

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("Db_Users_Details").document(UserID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name_user", name);
                    user.put("email_user", email);
                    user.put("password_user", password);
                    user.put("city_user", city);
                    user.put("birthdate_user", dob);
                    user.put("gender_user", gender);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Details Added Succussfully.", Toast.LENGTH_SHORT).show();
                            resetFields();
                            loadingBar.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingBar.dismiss();
                            Toast.makeText(getContext(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    String gender;

    private void selectGender() {
        if (radioFemale.isChecked()) {
            gender = "Female";

        } else {
            gender = "Male";


        }
    }

    public void selectDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        DAY = dayOfMonth + "";
                        MONTH = monthOfYear + 1 + "";
                        YEAR = year + "";

                        userdob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void resetFields() {

        username.getText().clear();
        useremail.getText().clear();
        userpassword.getText().clear();
        userdob.getText().clear();
        usercity.getText().clear();
        radioMale.setChecked(false);
        radioFemale.setChecked(false);


    }

}