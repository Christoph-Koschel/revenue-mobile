package com.koschel.revenue.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.koschel.revenue.mobile.api.Requester;
import com.koschel.revenue.mobile.model.TagModel;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        SharedPreferences preferences = getSharedPreferences("revenue", MODE_PRIVATE);
        Intent intent = getIntent();
        TagModel model = intent.getSerializableExtra("tag", TagModel.class);
        assert model != null;

        TextView tag = findViewById(R.id.tag);
        EditText value = findViewById(R.id.value);
        EditText description = findViewById(R.id.description);
        MaterialButton send = findViewById(R.id.send);

        tag.setText(model.name);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _) {
                int id = model.id;
                double v = Double.parseDouble(value.getText().toString());
                String d = description.getText().toString();

                String json = String.format("{\"tag\": %s, \"value\": %s, \"description\": \"%s\"}", id, v, d);
                try {
                    final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                    MGF1ParameterSpec mgf1Spec = MGF1ParameterSpec.SHA256;
                    PSource pSource = PSource.PSpecified.DEFAULT;
                    OAEPParameterSpec oaepSpec = new OAEPParameterSpec("SHA-256", "MGF1", mgf1Spec, pSource);

                    Key key = loadPublicKey();
                    cipher.init(Cipher.ENCRYPT_MODE, key, oaepSpec);
                    String encrypted = Base64.getEncoder().encodeToString(cipher.doFinal(json.getBytes()));
                    Requester.INSTANCE.newEntry(preferences.getString("registry", null), encrypted);
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                } catch (IOException | GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            }

            public Key loadPublicKey() throws GeneralSecurityException, IOException {
                byte[] data = Base64.getDecoder().decode(preferences.getString("publicPEM", "").replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", ""));
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                EncodedKeySpec publicKey = new X509EncodedKeySpec(data);
                return keyFactory.generatePublic(publicKey);

            }
        });

    }
}