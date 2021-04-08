package com.firestoreandroidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class User_Dashboard extends AppCompatActivity {

    Button btnMyprofile;
    TextView tvWelcome;

    private ActionBarDrawerToggle t;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);

        getSupportActionBar().setTitle("Dashboard");
        sideMenu();

        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvWelcome.setText(getIntent().getStringExtra("welcommsg"));
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {
                Fragment fragment = null;
                if (view == findViewById(R.id.btnMyProfile)) {
                    fragment = new User_Profile_Fragment();
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frameLayout, fragment);
                transaction.commit();
            }
        };

        Button btnMyProfile = (Button) findViewById(R.id.btnMyProfile);
        btnMyProfile.setOnClickListener(listener);


    }

    private void sideMenu() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        t = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.user_dashboard:
                        Intent intent = new Intent(getApplicationContext(), User_Dashboard.class);
                        intent.putExtra("welcommsg",getIntent().getStringExtra("welcommsg"));
                        startActivity(intent);
                        break;
                    case R.id.userProfile:
                        Intent view_jobs = new Intent(getApplicationContext(), User_Profile.class);
                        startActivity(view_jobs);
                        break;

                    case R.id.logout:
                        Intent logout = new Intent(getApplicationContext(), User_MainActivity.class);
                        startActivity(logout);
                        finish();
                        break;

                    default:
                        return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            diolougebox();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void diolougebox() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(User_Dashboard.this);
        builder1.setMessage("Do you want to Exit.");  //message we want to show the end user
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); //cancle the alert dialog box
                        finish();//finish the process
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
