package com.example.crudmahasiswa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private List<JSONObject> mahasiswaList;
    private List<JSONObject> filteredList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchView);

        mahasiswaList = new ArrayList<>();
        filteredList = new ArrayList<>();

        setupBottomNavigation();
        loadMahasiswaData();
        setupSearchView();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    return true;
                } else if (id == R.id.navigation_add) {
                    startActivity(new Intent(MainActivity.this, AddMahasiswaActivity.class));
                    return true;
                } else if (id == R.id.navigation_about) {
                    Toast.makeText(MainActivity.this, R.string.about_description, Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMahasiswa(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMahasiswa(newText);
                return true;
            }
        });
    }

    private void filterMahasiswa(String text) {
        filteredList.clear();

        if (TextUtils.isEmpty(text)) {
            filteredList.addAll(mahasiswaList);
        } else {
            text = text.toLowerCase();
            for (JSONObject mahasiswa : mahasiswaList) {
                try {
                    String nama = mahasiswa.getString("nama").toLowerCase();
                    String nim = mahasiswa.getString("nim").toLowerCase();

                    if (nama.contains(text) || nim.contains(text)) {
                        filteredList.add(mahasiswa);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        adapter.updateData(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMahasiswaData();
    }

    private void loadMahasiswaData() {
        mahasiswaList.clear();
        filteredList.clear();

        try {
            File file = new File(getFilesDir(), "mahasiswa.json");

            if (!file.exists()) {
                // Jika file belum ada, salin dari assets
                copyFromAssets();
            }

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

            for (int i = 0; i < jsonArray.length(); i++) {
                mahasiswaList.add(jsonArray.getJSONObject(i));
            }

            filteredList.addAll(mahasiswaList);

            adapter = new MahasiswaAdapter(filteredList, new MahasiswaAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(JSONObject item) {
                    Intent intent = new Intent(MainActivity.this, DetailMahasiswaActivity.class);
                    try {
                        intent.putExtra("nim", item.getString("nim"));
                        intent.putExtra("nama", item.getString("nama"));
                        intent.putExtra("jurusan", item.getString("jurusan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }

                @Override
                public void onEditClick(JSONObject item) {
                    Intent intent = new Intent(MainActivity.this, EditMahasiswaActivity.class);
                    try {
                        intent.putExtra("nim", item.getString("nim"));
                        intent.putExtra("nama", item.getString("nama"));
                        intent.putExtra("jurusan", item.getString("jurusan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(JSONObject item) {
                    try {
                        String nim = item.getString("nim");
                        deleteData(nim);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            recyclerView.setAdapter(adapter);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.msg_gagal, Toast.LENGTH_SHORT).show();
        }
    }

    private void copyFromAssets() {
        try {
            InputStream is = getAssets().open("mahasiswa.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "mahasiswa.json"));
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteData(String nim) {
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

            loadMahasiswaData();
            Toast.makeText(this, R.string.msg_sukses_hapus, Toast.LENGTH_SHORT).show();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.msg_gagal, Toast.LENGTH_SHORT).show();
        }
    }
}