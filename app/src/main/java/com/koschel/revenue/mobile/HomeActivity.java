package com.koschel.revenue.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;

import com.koschel.revenue.mobile.adapter.ActionAdapter;
import com.koschel.revenue.mobile.api.Requester;
import com.koschel.revenue.mobile.model.ActionModel;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    private final ActionModel[] actions = {
            new ActionModel("Neuer Eintrag", R.drawable.baseline_library_add_24, NewEntryActivity.class),
            new ActionModel("Profil", R.drawable.baseline_person_100, ProfileActivity.class),
            new ActionModel("Info", R.drawable.baseline_error_24, InfoActivity.class)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences preferences = getSharedPreferences("revenue", MODE_PRIVATE);
        Requester.INSTANCE.getRegistry(preferences.getString("registry", null), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    assert response.body() != null;
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.getInt("code") != 200) {
                        return;
                    }
                    JSONObject registryObj = obj.getJSONArray("data").getJSONObject(0);
                    if (registryObj.getInt("revision") > preferences.getInt("revision", 0)) {
                        Requester.INSTANCE.getRegistryDetails(registryObj.getString("detail"), new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                try {
                                    assert response.body() != null;
                                    JSONObject obj = new JSONObject(response.body().string());
                                    if (obj.getInt("code") != 200) {
                                        return;
                                    }
                                    JSONObject detailObj = obj.getJSONArray("data").getJSONObject(0);
                                    preferences
                                            .edit()
                                            .putString("tags", detailObj.getJSONArray("tags").toString())
                                            .putInt("revision", registryObj.getInt("revision"))
                                            .apply();
                                } catch (Exception e) {

                                }
                            }
                        });
                    }
                } catch (Exception e) {

                }
            }
        });


        setContentView(R.layout.activity_home);

        GridView gridView = findViewById(R.id.action_grid);

        ActionAdapter adapter = new ActionAdapter(this, actions);

        gridView.setAdapter(adapter);
    }
}