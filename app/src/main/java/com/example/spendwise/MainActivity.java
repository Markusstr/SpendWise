package com.example.spendwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<DataClass> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ImageButton main_url_close = findViewById(R.id.main_url_close);
        final EditText main_url_input = findViewById(R.id.main_url_input);
        final Button main_url_button = findViewById(R.id.main_url_button);
        dataList = new ArrayList<>();


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
                    Intent intent = new Intent(v.getContext(), ProductActivity.class);
                    intent.putExtra("url", main_url_input.getText().toString());
                    startActivity(intent);
                }
            }
        });

        Bitmap product1 = BitmapFactory.decodeResource(getResources(), R.drawable.product1);
        Bitmap product2 = BitmapFactory.decodeResource(getResources(), R.drawable.product2);
        Bitmap product3 = BitmapFactory.decodeResource(getResources(), R.drawable.product3);
        Bitmap product4 = BitmapFactory.decodeResource(getResources(), R.drawable.product4);
        DataClass data;

        data = new DataClass(product1, "Camera", "Price: 190€");
        dataList.add(data);
        data = new DataClass(product2, "Microphone", "Price: 60€");
        dataList.add(data);
        data = new DataClass(product3, "Boat", "Price: 3000€");
        dataList.add(data);
        data = new DataClass(product4, "Handheld drill", "Price: 150€");
        dataList.add(data);

        createRecyclerView();




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

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                String product_name = data.getStringExtra("product_name");
                String product_price = data.getStringExtra("product_price");
                Bitmap product_image = data.getExtra

            }
        }
    }*/

    public void createRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new RecyclerViewAdapter(dataList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onCloseClick(int position) {
                dataList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
    }
}
