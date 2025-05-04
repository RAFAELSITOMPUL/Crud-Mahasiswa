package com.example.crudmahasiswa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DetailMahasiswaActivity extends AppCompatActivity {

    private TextView tvNama, tvNim, tvJurusan;
    private Button btnEdit, btnHapus;
    private String nim, nama, jurusan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mahasiswa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNama = findViewById(R.id.tvNama);
        tvNim = findViewById(R.id.tvNim);
        tvJurusan = findViewById(R.id.tvJurusan);
        btnEdit = findViewById(R.id.btnEdit);
        btnHapus = findViewById(R.id.btnHapus);

        loadData();
        setupBottomNavigation();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailMahasiswaActivity.this, EditMahasiswaActivity.class);
                intent.putExtra("nim", nim);
                intent.putExtra("nama", nama);
                intent.putExtra("jurusan", jurusan);
                startActivity(intent);
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
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
                    startActivity(new Intent(DetailMahasiswaActivity.this, MainActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.navigation_add) {
                    startActivity(new Intent(DetailMahasiswaActivity.this, AddMahasiswaActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.navigation_about) {
                    Toast.makeText(DetailMahasiswaActivity.this, R.string.about_description, Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadData() {
        Intent intent = getIntent();
        nim = intent.getStringExtra("nim");
        nama = intent.getStringExtra("nama");
        jurusan = intent.getStringExtra("jurusan");

        tvNim.setText(nim);
        tvNama.setText(nama);
        tvJurusan.setText(jurusan);
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.btn_hapus);
        builder.setMessage(R.string.msg_konfirmasi_hapus);
        builder.setPositiveButton(R.string.dialog_ya, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData();
            }
        });
        builder.setNegativeButton(R.string.dialog_tidak, null);
        builder.show();
    }

    private void deleteData() {
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

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (!obj.getString("nim").equals(nim)) {
                    newJsonArray.put(obj);
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(newJsonArray.toString().getBytes());
            fos.close();

            Toast.makeText(this, R.string.msg_sukses_hapus, Toast.LENGTH_SHORT).show();
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