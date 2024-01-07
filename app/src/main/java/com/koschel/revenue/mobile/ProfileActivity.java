package com.koschel.revenue.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tags = findViewById(R.id.tags);
        TextView guid = findViewById(R.id.guid);
        MaterialButton logout = findViewById(R.id.logout);

        try {
            SharedPreferences preferences = getSharedPreferences("revenue", MODE_PRIVATE);
            int tagsCount = new JSONArray(preferences.getString("tags", "[]")).length();

            tags.setText("Tag: " + tagsCount);
            guid.setText("GUID: " + preferences.getString("registry", ""));
            logout.setOnClickListener(v -> {
                preferences.edit().remove("token")
                        .remove("registry")
                        .remove("publicPEM")
                        .remove("tags")
                        .remove("revision").apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
        } catch (Exception e) {

        }
    }
}