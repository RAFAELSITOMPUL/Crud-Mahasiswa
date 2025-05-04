# CRUD Mahasiswa

Aplikasi Android sederhana untuk melakukan operasi CRUD (Create, Read, Update, Delete) data mahasiswa. Proyek ini dibuat menggunakan Android Studio dan Kotlin dengan struktur Gradle modern.

## 🎯 Fitur

- Tambah data mahasiswa
- Lihat daftar mahasiswa
- Ubah informasi mahasiswa
- Hapus data mahasiswa
- Antarmuka sederhana dan responsif

## 🛠 Teknologi yang Digunakan

- Android Studio
- Kotlin
- Jetpack Components (ViewModel, LiveData, Room)
- Material Design
- Gradle Kotlin DSL (`build.gradle.kts`)

## 📦 Struktur Project

app/
├── java/
│ └── com.example.crudmahasiswa/
│ ├── MainActivity.kt
│ ├── TambahActivity.kt
│ ├── Mahasiswa.kt
│ ├── MahasiswaDao.kt
│ └── DatabaseMahasiswa.kt
├── res/
│ ├── layout/
│ │ ├── activity_main.xml
│ │ ├── activity_tambah.xml
│ └── values/
│ └── strings.xml
build.gradle.kts
settings.gradle.kts


## 🚀 Cara Menjalankan

1. Clone repository ini:
   ```bash
   git clone https://github.com/RAFAELSITOMPUL/crud-mahasiswa.git
2. Buka di Android Studio.
3. Pastikan Android SDK sudah diset (local.properties otomatis terdeteksi):
4. sdk.dir=C:\\Users\\NAMAKOMPUTER\\AppData\\Local\\Android\\Sdk
5. jalankan dengan klik tombol Run atau tekan Shift + F10.


📋 Catatan
Pastikan menggunakan emulator atau perangkat dengan Android API Level minimal 21.

Database menggunakan Room, data disimpan secara lokal di device.

📋 Catatan
Pastikan menggunakan emulator atau perangkat dengan Android API Level minimal 21.

Database menggunakan Room, data disimpan secara lokal di device.

🙋 Kontribusi
Pull request terbuka untuk siapa saja yang ingin menambahkan fitur atau memperbaiki bug.


---

### Cara membuatnya di Android

Kalau kamu pakai **Acode**, **Termux**, atau **SPCK**:
1. Buat file baru `README.md`.
2. Paste isi di atas.
3. Simpan.

---

Perlu versi bahasa Indonesia penuh, atau butuh README dengan screenshot dan demo link GitHub?
