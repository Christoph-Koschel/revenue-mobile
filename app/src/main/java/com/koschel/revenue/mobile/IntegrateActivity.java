package com.koschel.revenue.mobile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.koschel.revenue.mobile.api.Requester;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class IntegrateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrate);

        MaterialButton button = findViewById(R.id.scan_qr);
        button.setOnClickListener(v -> {
            scanQRCode();
        });
    }

    private void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR Code");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivityPortrait.class);
        barLauncher.launch(options);
        Log.println(Log.WARN, "REVENUE", "START SCANNING");
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), res -> {
        if (res.getContents() != null) {

            String[] parts = res.getContents().split("\n");
            if (parts.length != 2) {
                return;
            }
            String token = parts[0];
            String registry = parts[1];

            final SharedPreferences preferences = getSharedPreferences("revenue", MODE_PRIVATE);

            Requester.INSTANCE.bindToken(token);
            Requester.INSTANCE.getRegistry(registry, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try {
                        assert response.body() != null;
                        JSONObject resObj = new JSONObject(response.body().string());
                        if (resObj.getInt("code") != 200) {
                            return;
                        }
                        JSONObject registryObj = resObj.getJSONArray("data").getJSONObject(0);
                        Requester.INSTANCE.getRegistryDetails(registryObj.getString("detail"), new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                try {
                                    assert response.body() != null;
                                    JSONObject resObj = new JSONObject(response.body().string());
                                    if (resObj.getInt("code") != 200) {
                                        return;
                                    }
                                    JSONObject detailObj = resObj.getJSONArray("data").getJSONObject(0);

                                    preferences
                                            .edit()
                                            .putString("token", token)
                                            .putString("registry", registry)
                                            .putString("publicPEM", detailObj.getString("publicPEM"))
                                            .putString("tags", detailObj.getJSONArray("tags").toString())
                                            .putInt("revision", registryObj.getInt("revision"))
                                            .apply();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                } catch (Exception e) {

                                }
                            }
                        });


                    } catch (Exception e) {

                    }
                }
            });

        }
    });
}