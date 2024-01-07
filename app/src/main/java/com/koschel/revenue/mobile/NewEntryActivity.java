package com.koschel.revenue.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;

import com.koschel.revenue.mobile.adapter.TagAdapter;
import com.koschel.revenue.mobile.model.TagModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        try {
            SharedPreferences preferences = getSharedPreferences("revenue", MODE_PRIVATE);
            JSONArray tags = new JSONArray(preferences.getString("tags", "[]"));

            TagModel[] tagModels = new TagModel[tags.length()];
            for (int i = 0; i < tags.length(); i++) {
                JSONObject tag = tags.getJSONObject(i);
                tagModels[i] = new TagModel(tag.getInt("id"), tag.getString("name"), tag.getBoolean("income"));
            }

            GridView gridView = findViewById(R.id.tag_grid);

            TagAdapter adapter = new TagAdapter(this, tagModels);

            gridView.setAdapter(adapter);
        } catch (Exception e) {

        }

    }
}