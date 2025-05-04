package com.example.crudmahasiswa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AddMahasiswaActivity extends AppCompatActivity {

    private TextInputEditText etNama, etNim, etJurusan;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mahasiswa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        etJurusan = findViewById(R.id.etJurusan);
        btnSimpan = findViewById(R.id.btnSimpan);

        setupBottomNavigation();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_add);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    startActivity(new Intent(AddMahasiswaActivity.this, MainActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.navigation_add) {
                    return true;
                } else if (id == R.id.navigation_about) {
                    Toast.makeText(AddMahasiswaActivity.this, R.string.about_description, Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void saveData() {
        String nama = etNama.getText().toString().trim();
        String nim = etNim.getText().toString().trim();
        String jurusan = etJurusan.getText().toString().trim();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(nim) || TextUtils.isEmpty(jurusan)) {
            Toast.makeText(this, R.string.msg_data_tidak_lengkap, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = new File(getFilesDir(), "mahasiswa.json");
            JSONArray jsonArray;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                jsonArray = new JSONArray(sb.toString());
            } else {
                jsonArray = new JSONArray();
            }

            // Check if NIM already exists
            boolean nimExists = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("nim").equals(nim)) {
                    nimExists = true;
                    break;
                }
            }

            if (nimExists) {
                Toast.makeText(this, "NIM sudah terdaftar", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject newData = new JSONObject();
            newData.put("nama", nama);
            newData.put("nim", nim);
            newData.put("jurusan", jurusan);

            jsonArray.put(newData);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonArray.toString().getBytes());
            fos.close();

            Toast.makeText(this, R.string.msg_sukses_tambah, Toast.LENGTH_SHORT).show();

            etNama.setText("");
            etNim.setText("");
            etJurusan.setText("");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.msg_gagal, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}