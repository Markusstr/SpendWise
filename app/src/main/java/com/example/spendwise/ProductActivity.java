package com.example.spendwise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductActivity extends AppCompatActivity {

    float ean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final TextView product_name = findViewById(R.id.product_name);
        final TextView product_price = findViewById(R.id.product_price);
        final ImageView product_picture = findViewById(R.id.product_picture);
        final ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        final TextView average_time = findViewById(R.id.average_time);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        String url = "";

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Product");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    url = sharedText;
                }
            }
        } else {
            url = intent.getStringExtra("url");
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://23.101.59.215:5000/getinfo")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ProductActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar2.setVisibility(View.INVISIBLE);
                        new AlertDialog.Builder(ProductActivity.this)
                                .setTitle("Cannot connect to server")
                                .setMessage("We are having trouble connecting to the server. Try again later.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.ic_warning)
                                .show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject data = null;
                    Bitmap image = null;
                    try {
                        data = new JSONObject(response.body().string());
                        image = getImage(data.get("photo").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final JSONObject finalData = data;
                    final Bitmap finalImage = image;
                    ProductActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String productName = finalData.get("name").toString();
                                product_name.setText(productName);
                                String priceText = "Price: " + finalData.get("price").toString() + "€";
                                product_price.setText(priceText);
                                product_picture.setImageBitmap(finalImage);
                                ean = Float.parseFloat(finalData.get("ean").toString());
                                String averageText = "Average use time of " + productName + " is " + finalData.get("usage").toString();
                                average_time.setText(averageText);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressBar2.setVisibility(View.INVISIBLE);
                            Toast.makeText(ProductActivity.this, "Valmis", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public Bitmap getImage(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert response != null;
        InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
