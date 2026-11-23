# 🎵 NexusSpace

Aplikasi musik Android modern yang dibangun menggunakan **Jetpack Compose** dan **Deezer API**.

---

## 📱 Fitur Utama

- 🏠 **Halaman Beranda** — Menampilkan lagu trending, album populer, dan artis teratas dari Deezer Chart
- 🔍 **Pencarian** — Cari lagu, artis, dan album secara real-time
- 🎤 **Detail Artis** — Lihat profil artis beserta lagu-lagu terbaiknya
- 💿 **Detail Album** — Tampilkan cover album dan daftar lagu lengkap
- 🌙 **Dark / Light Mode** — Bisa beralih tema dari halaman Pengaturan
- 🌐 **Multi Bahasa** — Mendukung Bahasa Indonesia dan English
- 📐 **Rotasi Layar** — UI tetap stabil dan bisa di-scroll saat layar diputar

---

## 🛠️ Tech Stack

| Teknologi | Kegunaan |
|-----------|----------|
| Jetpack Compose | UI Toolkit Modern Android |
| Navigation Compose | Navigasi antar halaman |
| Retrofit + Gson | HTTP Client & JSON Parser |
| OkHttp | HTTP logging & networking |
| Coil | Memuat gambar dari URL |
| ViewModel + StateFlow | Manajemen state UI |
| Deezer API | Sumber data musik (gratis, tanpa API key) |
| AppCompat | Penggantian bahasa dinamis |

---

## 🗂️ Struktur Project

```
app/src/main/java/com/sandya/nexusspace/
├── data/
│   ├── model/          # Data class (Track, Artist, Album, dll)
│   ├── remote/         # Retrofit API service & instance
│   └── repository/     # Repository layer
├── ui/
│   ├── components/     # Reusable UI components (TrackCard, ArtistCard, dll)
│   ├── navigation/     # NavGraph & Screen routes
│   ├── screen/         # HomeScreen, SearchScreen, ArtistDetailScreen, dll
│   ├── theme/          # Color, Typography, Theme
│   ├── viewmodel/      # HomeViewModel, SearchViewModel, DetailViewModel
│   └── AppPreferences  # State holder dark mode & bahasa
└── MainActivity.kt     # Entry point + Bottom Navigation
```

---

## 🌐 API

Aplikasi ini menggunakan **[Deezer API](https://developers.deezer.com/api)** yang gratis dan tidak memerlukan API key.

Endpoint yang digunakan:
- `GET /chart` — Data trending (lagu, album, artis)
- `GET /search?q={query}` — Pencarian lagu
- `GET /artist/{id}` — Detail artis
- `GET /artist/{id}/top` — Lagu terpopuler artis
- `GET /album/{id}` — Detail album beserta tracklist

---

## 🚀 Cara Menjalankan

1. Clone repo ini:
   ```bash
   git clone https://github.com/sandygifta-ui/NexusSpace.git
   ```
2. Buka dengan **Android Studio**
3. Tunggu Gradle sync selesai
4. Jalankan di emulator atau device (min. Android 7.0 / API 24)

> Tidak memerlukan API key — langsung bisa dijalankan!

---

## 📸 Tampilan Aplikasi

| Beranda (Light) | Beranda (Dark) | Pencarian | Detail Artis |
|:-:|:-:|:-:|:-:|
| *(screenshot)* | *(screenshot)* | *(screenshot)* | *(screenshot)* |

---

## 👨‍💻 Developer

**Sandya Gifta**  
XII RPL — Tugas Projek Mobile  
November – Desember 2026

---

## 📄 Lisensi

Project ini dibuat untuk keperluan tugas sekolah.  
Data musik disediakan oleh [Deezer](https://www.deezer.com).
