package com.firestoreandroidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class User_Login extends Fragment {
    EditText useremail, userpassword;
    Button btnLogin;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    View view;

    public User_Login() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_fragment_login, container, false);

        useremail = (EditText)view. findViewById(R.id.useremail);
        userpassword = (EditText)view. findViewById(R.id.userpassword);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(getContext());


        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLoginAuthentication();


            }
        });
        return view;
    }

    private void userLoginAuthentication() {
        String username = useremail.getText().toString().trim();
        String password = userpassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Please enter your Username...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter your password...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password Must be >= 6 Characters", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.setTitle("Login Account");
            progressDialog.setMessage("Please wait, while we are checking the credentials.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            AllowAccessToAccount(username, password);

        }
    }

    String str1;
    private void AllowAccessToAccount(final String username, final String password) {

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    String[] str=username.split("@");
                    for(int i=0; i< str.length; i++)
                    {
                         str1=str[0];
                    }
                    Intent intent=new Intent(getContext(), User_Dashboard.class);
                    intent.putExtra("welcommsg","  Welcome :  "+str1);
                    startActivity(intent);
                    resetFields();

                } else {
                    Toast.makeText(getContext(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void resetFields(){

        useremail.getText().clear();
        userpassword.getText().clear();



    }
}