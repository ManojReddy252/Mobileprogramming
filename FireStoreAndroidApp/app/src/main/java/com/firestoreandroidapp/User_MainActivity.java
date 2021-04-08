package com.firestoreandroidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class User_MainActivity extends AppCompatActivity {
    Button loginBtnFirestore, regBtnFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_main_one);


        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {

                Fragment fragment = null;
                if (view == findViewById(R.id.loginBtnFirestore)) {
                    fragment = new User_Login();
                } else {
                    fragment = new User_Registration();
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frameLayout, fragment);
                transaction.commit();
            }
        };


        loginBtnFirestore = (Button) findViewById(R.id.loginBtnFirestore);
        loginBtnFirestore.setOnClickListener(listener);

        regBtnFirestore = (Button) findViewById(R.id.regBtnFirestore);
        regBtnFirestore.setOnClickListener(listener);
    }
}