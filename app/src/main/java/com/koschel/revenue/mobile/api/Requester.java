package com.koschel.revenue.mobile.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class Requester {
    public static final Requester INSTANCE = new Requester();

    private final OkHttpClient client;
    private String token;

    private Requester() {
        client = new OkHttpClient();
    }

    public void bindToken(String token) {
        this.token = token;
    }

    public void getRegistry(String id, Callback callback) {
        assert token != null;
        assert id != null;

        Request req = new Request.Builder()
                .url("https://hexa-studio.de:8443/api/select/revenue.registry/" + id)
                .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                .addHeader("Authorization", token)
                .build();
        Call call = client.newCall(req);
        call.enqueue(callback);
    }

    public void getRegistryDetails(String id, Callback callback) {
        assert token != null;
        assert id != null;

        Request req = new Request.Builder()
                .url("https://hexa-studio.de:8443/api/select/revenue.registry.detail/" + id)
                .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                .addHeader("Authorization", token)
                .build();
        Call call = client.newCall(req);
        call.enqueue(callback);
    }

    public void newEntry(String registry, String encrypted) {
        assert token != null;
        assert registry != null;
        String w = String.format("{\"data\": [{\"registry\": \"%s\", \"data\": \"%s\", \"seen\": 0}]}", registry, encrypted);

        Request req = new Request.Builder()
                .url("https://hexa-studio.de:8443/api/insert/revenue.entry")
                .addHeader("Authorization", token)
                .post(RequestBody.create(MediaType.parse("application/json"), w))
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                Log.w("REVENUE", response.body().string());
            }
        });
    }
}
