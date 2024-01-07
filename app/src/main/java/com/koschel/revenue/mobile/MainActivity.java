package com.koschel.revenue.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.button.MaterialButton;
import com.koschel.revenue.mobile.api.Requester;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("revenue", MODE_PRIVATE);
        if (
                !preferences.contains("token") ||
                        !preferences.contains("publicPEM") ||
                        !preferences.contains("revision") ||
                        !preferences.contains("tags") ||
                        !preferences.contains("registry")
        ) {
            Intent intent = new Intent(this, IntegrateActivity.class);
            startActivity(intent);
        } else {
            Requester.INSTANCE.bindToken(preferences.getString("token", null));
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}