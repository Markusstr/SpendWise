package com.example.spendwise;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ImageButton main_url_close = findViewById(R.id.main_url_close);
        final EditText main_url_input = findViewById(R.id.main_url_input);
        final Button main_url_button = findViewById(R.id.main_url_button);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        main_url_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_url_input.setText("");
            }
        });

        main_url_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (main_url_input.getText().toString().equals("")) {
                    Snackbar.make(v, "Enter URL first!", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    //TODO: Send URL to server
                    Intent intent = new Intent(v.getContext(), ProductActivity.class);
                    startActivity(intent);
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        //TODO: Check what id is
        return true;
    }
}
