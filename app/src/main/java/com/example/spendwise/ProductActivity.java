package com.example.spendwise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final TextView product_name = findViewById(R.id.product_name);
        final TextView product_price = findViewById(R.id.product_price);
        final ImageView product_picture = findViewById(R.id.product_picture);
        final ProgressBar progressBar2 = findViewById(R.id.progressBar2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Product");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", getIntent().getStringExtra("url"));
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
                progressBar2.setVisibility(View.INVISIBLE);
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
                                product_name.setText(finalData.get("name").toString());
                                String priceText = "Price: " + finalData.get("price").toString() + "€";
                                product_price.setText(priceText);
                                product_picture.setImageBitmap(finalImage);

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
