# Praktikum 08 — Kotlin Coroutines dan Flow

**Nama:** Ozora Nathalnael Purba  
**NIM:** 245150207111102  
**Kelas:** TIF D

## Deskripsi

Praktikum ini melanjutkan pengembangan aplikasi NavLab dari Modul 07 dengan mengganti mekanisme callback Retrofit menjadi Kotlin Coroutines. Pengambilan data dari DummyJSON menggunakan suspend function, sedangkan state tampilan dikelola melalui StateFlow dan ditampilkan menggunakan Jetpack Compose.

Pencarian profil dikembangkan menjadi reaktif menggunakan operator Flow. Pengguna cukup mengetik kata kunci, lalu pencarian berjalan otomatis setelah berhenti mengetik selama sekitar 500 milidetik. Praktikum ini juga menambahkan tombol **Refresh** untuk memuat ulang data sesuai kata kunci yang sedang aktif.

## Fitur

- Menampilkan 10 profil awal dari REST API pada halaman Home.
- Mencari profil secara otomatis setelah debounce 500 milidetik.
- Menghindari pengulangan pencarian untuk kata kunci yang sama menggunakan `distinctUntilChanged`.
- Membatalkan pekerjaan sebelumnya ketika permintaan baru masuk menggunakan `collectLatest`.
- Mengembalikan daftar awal ketika kata kunci kosong atau hanya berisi spasi.
- Memuat ulang daftar awal atau hasil pencarian melalui tombol **Refresh**.
- Menampilkan nama, email, dan universitas pada halaman Detail.
- Menangani loading, hasil kosong, dan error dengan tombol **Coba Lagi**.
- Mempertahankan kata kunci dan hasil pencarian setelah kembali dari halaman Detail.

## Arsitektur

Alur data aplikasi:

UI → ViewModel → Repository → Remote Data Source → Retrofit → REST API

Retrofit service, remote data source, dan repository menggunakan `suspend function` untuk pengambilan data. Repository memetakan DTO dari API menjadi model `Student` yang digunakan oleh presentation layer.

ViewModel menjalankan operasi asinkron melalui `viewModelScope` dan menyimpan state menggunakan `MutableStateFlow` yang bersifat private. UI menerima `StateFlow` read-only dan mengoleksinya melalui `collectAsStateWithLifecycle()` agar pengamatan state mengikuti lifecycle layar.

HomeScreen meneruskan event pencarian dan Refresh ke ViewModel. Pemilihan request, pemanggilan repository, serta pembaruan state loading dan error dilakukan di ViewModel.

Endpoint yang digunakan:

- `GET /users?limit=10` untuk daftar awal.
- `GET /users/search?q={kata_kunci}` untuk pencarian.
- `GET /users/{id}` untuk detail profil.

## Mekanisme Refresh

Tombol **Refresh** memanggil fungsi `refresh()` pada HomeViewModel melalui `viewModelScope.launch`.

- Jika kata kunci kosong, aplikasi mengambil ulang daftar awal.
- Jika kata kunci aktif, aplikasi mengulang pencarian dengan kata kunci tersebut.
- Saat request dimulai, state loading diaktifkan dan pesan error sebelumnya dibersihkan.
- Setelah request selesai, aplikasi menampilkan hasil atau pesan error.
- Setelah Refresh berhasil, pengguna tetap dapat membuka Detail dan kembali ke Home.

## Cara Menjalankan

1. Ekstrak ZIP, kemudian buka folder **NavLab** di Android Studio.
2. Siapkan Android SDK Platform 37 dan JDK 25.
3. Lakukan **Gradle Sync**.
4. Jalankan aplikasi pada emulator atau perangkat Android API 24 ke atas dengan koneksi internet.
5. Ketik kata kunci pada kolom pencarian dan tunggu hasil muncul secara otomatis.
6. Tekan **Refresh** untuk memuat ulang data, lalu pilih profil untuk membuka halaman Detail.
