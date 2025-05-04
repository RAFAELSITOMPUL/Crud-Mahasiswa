package com.example.crudmahasiswa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class EditMahasiswaActivity extends AppCompatActivity {

    private TextInputEditText etNama, etNim, etJurusan;
    private Button btnUpdate;
    private String oldNim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mahasiswa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        etJurusan = findViewById(R.id.etJurusan);
        btnUpdate = findViewById(R.id.btnUpdate);

        setupBottomNavigation();
        loadData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    startActivity(new Intent(EditMahasiswaActivity.this, MainActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.navigation_add) {
                    startActivity(new Intent(EditMahasiswaActivity.this, AddMahasiswaActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.navigation_about) {
                    Toast.makeText(EditMahasiswaActivity.this, R.string.about_description, Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadData() {
        Intent intent = getIntent();
        oldNim = intent.getStringExtra("nim");
        String nama = intent.getStringExtra("nama");
        String jurusan = intent.getStringExtra("jurusan");

        etNim.setText(oldNim);
        etNama.setText(nama);
        etJurusan.setText(jurusan);
    }

    private void updateData() {
        String nama = etNama.getText().toString().trim();
        String nim = etNim.getText().toString().trim();
        String jurusan = etJurusan.getText().toString().trim();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(nim) || TextUtils.isEmpty(jurusan)) {
            Toast.makeText(this, R.string.msg_data_tidak_lengkap, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = new File(getFilesDir(), "mahasiswa.json");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            JSONArray jsonArray = new JSONArray(sb.toString());
            JSONArray newJsonArray = new JSONArray();

            // Check if new NIM already exists (except old NIM)
            boolean nimExists = false;
            if (!nim.equals(oldNim)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (obj.getString("nim").equals(nim)) {
                        nimExists = true;
                        break;
                    }
                }
            }

            if (nimExists) {
                Toast.makeText(this, "NIM sudah terdaftar", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("nim").equals(oldNim)) {
                    // Update data
                    JSONObject updatedObj = new JSONObject();
                    updatedObj.put("nim", nim);
                    updatedObj.put("nama", nama);
                    updatedObj.put("jurusan", jurusan);
                    newJsonArray.put(updatedObj);
                } else {
                    newJsonArray.put(obj);
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(newJsonArray.toString().getBytes());
            fos.close();

            Toast.makeText(this, R.string.msg_sukses_edit, Toast.LENGTH_SHORT).show();
            finish();

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