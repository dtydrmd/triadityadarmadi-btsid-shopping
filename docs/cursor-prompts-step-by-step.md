# Cursor Prompt Step by Step

Dokumen ini berisi prompt bertahap yang bisa dipakai di Cursor untuk membangun proyek **Nusaproc/Nusafina**, dengan fokus awal pada **modul PPAB**.

## Cara pakai

1. Buka workspace Cursor yang berisi dua direktori proyek:
   - direktori frontend ReactJS
   - direktori backend Golang
2. Simpan dokumen `docs/project-spec.md` di workspace yang sama agar agen bisa membaca baseline requirement.
3. Jalankan prompt **berurutan**.
4. Jangan loncat terlalu jauh sebelum fondasi step sebelumnya selesai.
5. Ganti placeholder berikut sebelum dipakai:
   - `<frontend-dir>`
   - `<backend-dir>`
   - `<gateway-info>` jika nanti gateway punya aturan khusus

Prompt di bawah ini ditulis agar bisa langsung di-copy ke Cursor.

---

## Prompt 01 - Baca spesifikasi dan buat rencana implementasi

```md
Baca file `docs/project-spec.md` lalu pahami bahwa proyek ini memiliki 2 aplikasi:
- frontend ReactJS di `<frontend-dir>`
- backend Golang di `<backend-dir>`

Fokus fase awal hanya modul PPAB.

Tugas:
1. pelajari struktur project yang sudah ada
2. identifikasi gap antara kode saat ini dan spesifikasi
3. buat rencana implementasi bertahap untuk PPAB
4. usulkan struktur folder/module yang paling cocok untuk frontend dan backend
5. jangan menulis kode dulu

Output yang saya inginkan:
- ringkasan struktur existing
- rekomendasi struktur target
- daftar pekerjaan backend
- daftar pekerjaan frontend
- urutan eksekusi yang aman
```

---

## Prompt 02 - Desain struktur backend Golang

```md
Baca `docs/project-spec.md`.

Fokus hanya di project backend `<backend-dir>`.

Saya ingin backend Golang untuk PPAB disusun dengan arsitektur yang rapi dan mudah dikembangkan ke modul PR, HPS, PO, SPP, Voucher, dan Register.

Tugas:
1. analisis struktur backend saat ini
2. tentukan pola arsitektur yang dipakai, misalnya layered atau clean architecture sederhana
3. buat struktur folder yang jelas untuk:
   - config
   - http handler / transport
   - middleware
   - domain/entity
   - usecase/service
   - repository
   - integration adapter
   - migration
   - shared utility
4. jelaskan letak modul PPAB dan integrasi eksternal
5. jika perlu, implementasikan scaffold awal folder dan file dasar tanpa langsung membangun semua endpoint

Constraint:
- jangan implementasikan fitur bisnis penuh dulu
- prioritaskan fondasi project
- dokumentasikan keputusan arsitektur secara ringkas
```

---

## Prompt 03 - Desain database PostgreSQL untuk PPAB

```md
Baca `docs/project-spec.md`.

Fokus di backend `<backend-dir>`.

Tugas:
1. rancang schema PostgreSQL 18.3 untuk modul PPAB
2. buat migration awal untuk tabel minimum berikut:
   - ppab
   - ppab_items
   - ppab_approval_steps
   - ppab_approval_actions
   - ppab_status_histories
   - ppab_change_logs
   - ppab_attachments
   - budget_achievement_ledger
3. pastikan desain mendukung:
   - nomor bayangan unik
   - nomor final saat approve final
   - pajak terpisah
   - approval berjenjang
   - reject ke step sebelumnya atau pembuat
   - approver edit dengan audit log
4. tambahkan index dan foreign key yang masuk akal
5. jelaskan asumsi desain yang kamu pakai

Jangan implementasikan endpoint dulu. Fokus pada model data dan migration.
```

---

## Prompt 04 - Buat adapter integrasi user/karyawan eksternal

```md
Baca `docs/project-spec.md`.

Fokus di backend `<backend-dir>`.

Saya butuh adapter integrasi untuk API eksternal user/karyawan yang dipakai untuk:
- login
- informasi user
- daftar divisi/bagian
- daftar unit kerja
- daftar karyawan
- jabatan karyawan

Tugas:
1. buat abstraction/interface integrasi user service eksternal
2. buat client HTTP yang rapi dan mudah dites
3. buat config berbasis environment variable, jangan hardcode secret
4. siapkan model request/response internal yang dinormalisasi
5. jika endpoint selain login belum tersedia, buat stub interface dan mock implementation sementara yang jelas
6. dokumentasikan field mana yang berasal dari sistem eksternal

Catatan:
- alur kewenangan tidak diambil dari integrasi eksternal
- fokus pada desain adapter dan batas integrasinya
```

---

## Prompt 05 - Buat adapter integrasi database anggaran eksternal

```md
Baca `docs/project-spec.md`.

Fokus di backend `<backend-dir>`.

Tugas:
1. buat adapter integrasi anggaran eksternal untuk jenis anggaran:
   - Opex
   - Capex
   - Produksi
   - Biaya Ditangguhkan
   - PMO
2. perlakukan Dana Eksternal sebagai input manual, bukan API
3. normalisasi semua response eksternal ke model internal yang seragam
4. siapkan pemetaan agar frontend cukup menerima satu bentuk response budget option
5. letakkan token/header sensitif di config environment
6. jika perlu, buat service caching atau sinkronisasi ringan untuk budget reference

Output kode yang saya inginkan:
- interface adapter
- implementation client
- mapper/normalizer
- contract model internal
```

---

## Prompt 06 - Implement domain dan workflow PPAB di backend

```md
Baca `docs/project-spec.md`.

Fokus di backend `<backend-dir>`.

Implementasikan domain PPAB terlebih dahulu tanpa mengerjakan modul lain.

Fitur yang harus ada:
1. create PPAB draft
2. update PPAB draft
3. save item multiple
4. hitung nilai dasar, nilai pajak, dan grand total
5. save approval steps dari form
6. submit PPAB
7. approve step aktif
8. reject ke approver sebelumnya atau pembuat dengan catatan
9. final approve yang menerbitkan nomor PPAB resmi
10. change log untuk setiap perubahan
11. status history
12. update capaian anggaran PPAB setelah approve final

Rule penting:
- approver boleh edit
- edit approver harus tercatat
- satu PPAB hanya satu jenis anggaran
- item boleh mengarah ke banyak referensi anggaran dalam jenis yang sama
- tanggal tidak boleh backdate
- future date boleh
- nomor bayangan unik harus dibuat sejak draft

Mohon implementasikan usecase/service secara rapi dan jangan campur logika bisnis ke handler HTTP.
```

---

## Prompt 07 - Buat endpoint backend PPAB

```md
Baca `docs/project-spec.md`.

Fokus di backend `<backend-dir>`.

Buat endpoint HTTP untuk modul PPAB yang mencakup:
- create draft
- update draft
- detail PPAB
- list PPAB
- submit
- approve
- reject
- list approval history
- list change log
- monitoring progress pembuat

Tugas:
1. rancang route yang konsisten
2. buat request/response DTO yang bersih
3. validasi input di layer yang tepat
4. tambahkan pagination/filter untuk list
5. siapkan error response yang konsisten
6. pastikan response cukup untuk kebutuhan frontend form, inbox approval, dan monitoring

Jangan menambahkan fitur di luar PPAB.
```

---

## Prompt 08 - Implement upload dokumen pendukung

```md
Baca `docs/project-spec.md`.

Fokus di backend `<backend-dir>`.

Tugas:
1. implementasikan upload attachment untuk PPAB
2. batasi ukuran file maksimal 2 MB
3. simpan metadata attachment di database
4. tentukan strategi penyimpanan file yang sesuai untuk fase awal
5. hubungkan attachment ke PPAB
6. pastikan keamanan dasar upload file

Jika penyimpanan object storage belum ada, buat solusi lokal/abstraksi yang bisa diganti nanti.
```

---

## Prompt 09 - Desain struktur frontend ReactJS

```md
Baca `docs/project-spec.md`.

Fokus hanya di project frontend `<frontend-dir>`.

Tugas:
1. analisis struktur frontend saat ini
2. usulkan struktur aplikasi React yang cocok untuk modul PPAB
3. susun folder untuk:
   - pages
   - features
   - components
   - services/api
   - hooks
   - state management
   - form model / validation
4. jika perlu, implementasikan scaffold awal
5. turunkan prinsip UI dari spesifikasi ke fondasi frontend, termasuk:
   - gaya profesional
   - layout compact, padat, tapi tetap mudah dibaca
   - dominan putih dan biru
   - warna lain hanya sebagai aksen status atau semantic state
6. jangan implementasikan semua halaman sekaligus

Tujuan step ini adalah membangun fondasi frontend yang rapi untuk form PPAB, inbox approval, dan monitoring.
```

---

## Prompt 10 - Buat form create/edit draft PPAB di frontend

```md
Baca `docs/project-spec.md`.

Fokus di frontend `<frontend-dir>`.

Implementasikan halaman create/edit PPAB dengan kebutuhan:
1. field header PPAB
2. item multiple
3. kalkulasi nilai dasar, pajak, dan grand total
4. searchable dropdown untuk:
   - divisi/bagian
   - unit kerja
   - user approver
   - anggaran
5. alur kewenangan yang bisa diurutkan
6. jabatan auto-fill tapi bisa diedit
7. upload dokumen pendukung
8. tombol simpan draft dan kirim draft

Catatan:
- jangan hardcode option yang seharusnya berasal dari API
- susun komponen form agar modular
- siapkan validasi yang sesuai rule bisnis
- gunakan tampilan profesional, compact, padat, dan dominan putih-biru
- pastikan hierarchy visual jelas untuk section header, item table, approval flow, dan summary nilai
```

---

## Prompt 11 - Buat halaman inbox approval dan action approve/reject

```md
Baca `docs/project-spec.md`.

Fokus di frontend `<frontend-dir>`.

Tugas:
1. buat halaman inbox approval untuk approver
2. tampilkan daftar PPAB yang menunggu aksi approver aktif
3. buat halaman detail approval
4. approver harus bisa:
   - melihat isi dokumen
   - mengedit dokumen
   - approve
   - reject dengan catatan
   - memilih target reject sesuai rule
5. tampilkan approval history dan change log

Pastikan UX menjelaskan dengan jelas siapa current approver dan apa dampak tiap aksi.
Gunakan visual yang profesional, ringkas, dan informatif dengan penekanan warna utama putih dan biru.
```

---

## Prompt 12 - Buat halaman monitoring pembuat

```md
Baca `docs/project-spec.md`.

Fokus di frontend `<frontend-dir>`.

Tugas:
1. buat halaman list permohonan PPAB milik pembuat
2. tampilkan status, nomor bayangan/final, total nilai, dan current approver
3. buat halaman detail monitoring
4. tampilkan timeline:
   - dibuat
   - dikirim
   - approve per step
   - reject
   - revisi
   - approve final
5. tampilkan change log dan attachment

Tujuan utama halaman ini adalah agar user bisa memonitor progres dokumennya tanpa ambigu.
Desain harus tetap compact dan mudah discan, terutama pada tabel, status badge, dan timeline.
```

---

## Prompt 13 - Integrasikan frontend ke gateway/backend

```md
Baca `docs/project-spec.md`.

Fokus pada `<frontend-dir>` dan kontrak API dari backend.

Tugas:
1. rapikan service layer untuk memanggil gateway
2. pastikan semua endpoint frontend diarahkan ke gateway, bukan langsung ke service internal lain
3. buat konfigurasi environment frontend yang jelas
4. rapikan error handling dan loading state
5. pastikan form, inbox approval, dan monitoring sudah terhubung ke API backend PPAB

Jangan ubah logic bisnis backend pada step ini kecuali ada bug integrasi yang benar-benar perlu disesuaikan.
```

---

## Prompt 14 - Tambahkan test dan verifikasi alur PPAB

```md
Baca `docs/project-spec.md`.

Fokus pada kualitas implementasi modul PPAB.

Tugas:
1. tambahkan test yang bernilai tinggi untuk backend
2. prioritaskan test untuk:
   - kalkulasi pajak
   - submit PPAB
   - approve berjenjang
   - reject ke step tertentu
   - final approve dan nomor final
   - change log
   - capaian anggaran PPAB
3. jika ada test frontend yang relevan dan ringkas, tambahkan untuk komponen penting
4. lakukan verifikasi manual flow utama
5. rangkum temuan dan gap yang masih tersisa

Hindari test yang terlalu dangkal dan fokus ke area berisiko tinggi.
```

---

## Prompt 15 - Rapikan dokumentasi implementasi

```md
Baca `docs/project-spec.md`.

Tugas:
1. dokumentasikan cara menjalankan frontend dan backend
2. dokumentasikan environment variable
3. dokumentasikan endpoint utama PPAB
4. dokumentasikan alur approval PPAB
5. dokumentasikan asumsi dan limitasi implementasi fase awal
6. buat daftar next step untuk modul PR yang akan menggunakan PPAB approved

Output akhir yang saya inginkan:
- README atau docs implementasi yang cukup untuk developer lain melanjutkan pekerjaan
```

---

## Prompt ringkas jika ingin mulai cepat

Kalau Anda ingin satu prompt awal yang cukup kuat sebelum dipecah menjadi step-step kecil, gunakan ini:

```md
Baca `docs/project-spec.md` lalu implementasikan fondasi modul PPAB untuk stack berikut:
- frontend ReactJS di `<frontend-dir>`
- backend Golang di `<backend-dir>`
- database PostgreSQL 18.3
- akses frontend melalui gateway

Mulai dari analisis struktur project, lalu buat rencana singkat, kemudian implementasikan fondasi yang diperlukan untuk modul PPAB:
- struktur backend dan frontend
- schema database
- domain PPAB
- integrasi user eksternal
- integrasi anggaran eksternal
- create/edit/save draft PPAB
- approval workflow
- change log
- monitoring progress

Kerjakan bertahap dan jelaskan keputusan penting selama implementasi.
Jangan mengerjakan modul PR, HPS, PO, SPP, Voucher, atau Register selain menyiapkan extensibility-nya.
Untuk frontend, gunakan gaya UI profesional, compact, padat namun tetap jelas dibaca, dengan dominasi warna putih dan biru serta aksen warna lain yang relevan untuk state/status.
```

---

## Saran penggunaan praktis

Urutan paling aman untuk implementasi:

1. Prompt 01
2. Prompt 02
3. Prompt 03
4. Prompt 04
5. Prompt 05
6. Prompt 06
7. Prompt 07
8. Prompt 09
9. Prompt 10
10. Prompt 11
11. Prompt 12
12. Prompt 13
13. Prompt 14
14. Prompt 15

Jika ingin lebih hemat iterasi, gabungkan step backend dan frontend per milestone, tetapi jangan mulai dari UI sebelum struktur domain dan database cukup stabil.

