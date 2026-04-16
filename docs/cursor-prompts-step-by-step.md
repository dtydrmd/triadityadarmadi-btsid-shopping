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

## Prompt 16 - Lanjutkan implementasi: login + integrasi API nyata

```md
Lanjutkan dari implementasi project yang sudah ada saat ini.

Sebelum mulai, baca ulang dan jadikan acuan utama:
- `docs/project-spec.md`
- `docs/cursor-prompts-step-by-step.md`

Jika ada perbedaan, prioritaskan `docs/project-spec.md` sebagai source of truth.

## Fokus pekerjaan
Tutup 2 gap utama berikut sampai benar-benar berfungsi:

1. aplikasi belum terhubung dengan API eksternal yang sudah dispesifikasikan
2. aplikasi belum memiliki login yang berfungsi

## Mode kerja
Kerjakan dalam mode **full autonomous**:
- jangan berhenti di analisis saja
- audit implementasi saat ini
- identifikasi gap nyata pada backend dan frontend
- lalu langsung implementasikan sampai selesai
- lanjutkan hingga verifikasi/build/test/dokumentasi selesai
- hanya berhenti jika ada blocker nyata yang tidak bisa diputuskan secara aman dari konteks yang tersedia

## Batas keamanan wajib
Anda hanya boleh bekerja di dalam workspace/window project yang sedang terbuka.

Anda dilarang:
- membaca file di luar workspace ini
- mencari data dari laptop saya di luar direktori project yang terbuka
- membaca home directory, desktop, downloads, documents, ssh keys, browser data, credential store, atau repo lain di luar workspace
- menggunakan token/secret hardcoded dari contoh sebagai credential nyata
- keluar dari konteks project ini

Jika butuh credential:
- gunakan environment variable
- buat `.env.example` atau config example
- bila credential nyata belum tersedia, buat fallback/mock/stub yang jelas
- jangan mencoba mengambil credential dari sistem saya

## Tujuan implementasi
Saya ingin aplikasi benar-benar memiliki:
- login yang bekerja
- auth flow yang utuh
- route protection
- current user context
- integrasi nyata ke API budget yang sudah saya share
- wiring frontend/backend untuk kebutuhan PPAB terkait login dan budget lookup

---

# A. Implementasi login eksternal

Gunakan API login berikut sebagai sumber autentikasi utama:

- Method: `POST`
- URL:
  `https://apigw.rpn.co.id/index.php/login-sip/usermng-login-sip`
- Header:
  - `Content-Type: application/json`

Request body:
```json
{
  "username": "<input username>",
  "password": "<input password>"
}
```

## Tugas login
Implementasikan end-to-end:

1. Buat adapter/backend client untuk login ke API eksternal di atas.
2. Jangan hardcode username/password user.
3. Gunakan konfigurasi berbasis environment variable untuk:
   - base URL auth API
   - login path
   - timeout
   - content type bila perlu
4. Normalisasi response login eksternal ke model internal aplikasi.
5. Setelah login eksternal sukses, bangun auth/session internal aplikasi.
   Pilih pendekatan yang paling cocok dengan codebase saat ini, misalnya:
   - secure session cookie
   - atau internal JWT/session token
6. Backend tidak boleh menyimpan password plaintext.
7. Buat endpoint auth minimum:
   - login
   - logout
   - current user / me
8. Tambahkan middleware/proteksi route untuk endpoint PPAB yang butuh login.
9. Frontend wajib memiliki:
   - halaman login
   - form username/password
   - auth state/provider/store
   - route guard
   - redirect ke login jika belum authenticated
   - logout flow
10. Setelah login sukses, frontend harus bisa menampilkan current user dan mengisi field "Pembuat" dari user login.
11. Jika data dari response login belum cukup untuk kebutuhan current user, buat abstraction/interface lanjutan yang rapi, dan dokumentasikan mana yang live dan mana yang masih fallback.

---

# B. Implementasi integrasi API budget nyata

Gunakan endpoint budget berikut:

## Opex
`GET https://ebudgeting.rpn.co.id/api/rkap/get_data_by_year/{year}`

## Capex
`GET https://ebudgeting.rpn.co.id/api/rkap/getInvestasi_by_year/{year}`

## Produksi
`GET https://ebudgeting.rpn.co.id/api/rkap/getProduksi_by_year/{year}`

## Biaya Ditangguhkan
`GET https://ebudgeting.rpn.co.id/api/rkap/getDitangguhkan_by_year/{year}`

## PMO
`GET https://ebudgeting.rpn.co.id/api/rkap/getAnggaranPmo_by_year/{year}`

## Dana Eksternal
- tidak memakai API
- tetap manual input oleh user

## Tugas integrasi budget
Implementasikan end-to-end:

1. Buat adapter nyata di backend untuk semua endpoint budget di atas.
2. Jangan hardcode header/token sensitif di source code.
3. Pindahkan konfigurasi ke environment variable, minimal:
   - budget API base URL
   - timeout
   - app header
   - bearer authorization
   - cookie jika memang diperlukan
4. Jika cookie ternyata tidak wajib, jangan jadikan hard dependency.
5. Normalisasi semua response eksternal menjadi contract internal tunggal yang konsisten, misalnya:
   - `budgetCode`
   - `budgetName`
   - `budgetDescription`
   - `year`
   - `category`
   - `amount`
   - `remaining`
   - `sourceType`
   - `rawReferenceId`
6. Jika struktur response tiap endpoint berbeda, buat mapper/normalizer per kategori anggaran.
7. Sediakan endpoint backend yang dipakai frontend untuk:
   - get budget options by budget type and year
   - search/filter budget options
8. Form PPAB di frontend harus benar-benar mengambil daftar anggaran dari backend/gateway sesuai:
   - jenis anggaran yang dipilih
   - tahun yang relevan
9. Dana Eksternal harus tetap menjadi manual entry dan tidak memanggil API budget.

---

# C. Audit integrasi user master (divisi, unit kerja, approver, jabatan)

Lakukan audit implementasi saat ini untuk memastikan apakah data berikut:
- divisi/bagian
- unit kerja
- daftar nama karyawan
- jabatan

sudah berasal dari integrasi nyata atau masih dummy.

## Aturan implementasi
1. Jika ternyata masih dummy:
   - buat abstraction/interface yang benar
   - gunakan response login untuk current user jika memungkinkan
   - jangan mengarang endpoint eksternal baru
   - jangan membuat integrasi palsu yang seolah final
2. Jika endpoint nyata untuk master employee/division/unit belum tersedia dari brief:
   - buat provider interface yang rapi
   - sediakan fallback/mock provider yang terisolasi
   - dokumentasikan dengan jujur bahwa endpoint final masih pending
3. Pisahkan dengan jelas:
   - mana yang live
   - mana yang fallback/mock
4. Jangan mencampur mock provider dengan live adapter secara membingungkan.

---

# D. Requirement backend

Pastikan backend memiliki:
- auth module yang rapi
- external auth client/adapter
- budget integration client/adapter
- middleware auth
- route protection untuk modul PPAB
- config berbasis environment variable
- error mapping dari API eksternal ke error internal yang aman
- timeout/retry yang masuk akal jika perlu
- logging aman tanpa membocorkan secret
- dokumentasi env variable yang lengkap

Jangan campur auth logic dengan business logic PPAB.

---

# E. Requirement frontend

Pastikan frontend memiliki:
- login page
- auth state/provider/store
- route protection
- logout flow
- current user context
- current user display di area yang relevan
- budget dropdown yang benar-benar mengambil data dari backend
- loading state
- empty state
- error state
- handling error login yang jelas dan profesional

---

# F. UX yang wajib dipatuhi

Tetap pakai gaya UI yang sudah disepakati:
- profesional
- compact
- padat tetapi tetap jelas dibaca
- dominan putih dan biru
- warna lain hanya untuk semantic/status yang relevan

## Untuk login page
- sederhana
- profesional
- tidak dekoratif berlebihan
- fokus pada form
- pesan error jelas
- spacing rapi
- nyaman dibaca

## Untuk budget dropdown
- searchable
- cepat dipahami
- tampilkan informasi penting bila memungkinkan:
  - kode anggaran
  - uraian
  - sisa anggaran

---

# G. Environment/config yang harus ada

Tambahkan dokumentasi dan contoh konfigurasi untuk parameter berikut bila belum ada.

## Auth API
- `AUTH_API_BASE_URL`
- `AUTH_LOGIN_PATH`
- `AUTH_API_TIMEOUT`
- `AUTH_API_CONTENT_TYPE`

## Budget API
- `BUDGET_API_BASE_URL`
- `BUDGET_API_TIMEOUT`
- `BUDGET_API_APP_HEADER`
- `BUDGET_API_AUTHORIZATION`
- `BUDGET_API_COOKIE` (opsional jika benar-benar diperlukan)

## App auth/session
- `APP_SESSION_SECRET` atau yang setara
- env lain yang dibutuhkan sesuai arsitektur auth yang dipilih

Gunakan naming yang konsisten dengan pola codebase jika sudah ada.

---

# H. Scope guard

Jangan mengerjakan domain baru di luar kebutuhan untuk menutup gap berikut:
- login
- auth/session
- proteksi route
- integrasi budget API nyata
- wiring frontend/backend terkait PPAB

Jangan mengimplementasikan penuh:
- PR
- HPS
- PO
- SPP
- Voucher
- Register

---

# I. Standar implementasi

- jangan hardcode secret
- jangan simpan password plaintext
- jangan mengarang endpoint eksternal yang belum diberikan
- jangan mengklaim live integration jika sebenarnya masih mock
- prioritaskan solusi yang maintainable
- pisahkan adapter integrasi dengan rapi
- pastikan perubahan terdokumentasi
- jujur tentang gap yang masih pending

---

# J. Verifikasi wajib

Sebelum selesai, lakukan verifikasi berikut:

1. login flow berjalan
2. logout flow berjalan
3. endpoint/me atau current user berjalan
4. route protection berjalan
5. current user tampil di frontend
6. dropdown budget mengambil data dari backend
7. pemilihan jenis anggaran memanggil endpoint budget yang tepat
8. Dana Eksternal tidak memanggil budget API
9. build/test/lint yang relevan dijalankan
10. env variable baru terdokumentasi
11. ringkas mana yang live dan mana yang masih fallback/mock

---

# K. Output akhir yang saya inginkan

Saat selesai, laporkan dengan jelas:

1. apa yang ditambahkan untuk login
2. apa yang ditambahkan untuk integrasi budget API
3. endpoint/layer mana yang sudah benar-benar live
4. bagian mana yang masih fallback/mock dan alasannya
5. env variable apa saja yang harus saya isi
6. file/folder utama yang ditambah/diubah
7. verifikasi yang sudah dijalankan
8. gap tersisa jika masih ada

Mulai sekarang:
- audit implementasi yang sudah ada
- identifikasi gap login dan integrasi API
- lalu langsung implementasikan sampai selesai
- tetap bekerja hanya dalam batas workspace project ini
```

---

## Prompt 17 - Rapikan integrasi user master tanpa mengarang endpoint

```md
Lanjutkan dari implementasi project yang sudah ada saat ini.

Sebelum mulai, baca ulang:
- `docs/project-spec.md`
- `docs/cursor-prompts-step-by-step.md`

Jika ada perbedaan, prioritaskan `docs/project-spec.md` sebagai source of truth.

## Fokus pekerjaan
Rapikan area integrasi data user master yang dipakai untuk:
- divisi/bagian
- unit kerja
- daftar nama karyawan
- jabatan
- approver selection
- current user context jika ada bagian yang masih belum konsisten

Tujuan prompt ini adalah memastikan implementasi **jujur secara teknis**, rapi secara arsitektur, dan tidak mengarang endpoint eksternal yang belum pernah didefinisikan.

## Mode kerja
Kerjakan dalam mode full autonomous:
- audit implementasi yang sudah ada
- identifikasi bagian mana yang live dan mana yang masih dummy/mock/fallback
- rapikan separation of concern
- perbaiki wiring yang perlu
- dokumentasikan hasilnya dengan jujur
- jangan berhenti di analisis saja

## Batas keamanan wajib
Anda hanya boleh bekerja di dalam workspace/window project yang sedang terbuka.

Anda dilarang:
- membaca file di luar workspace
- mencari data dari laptop saya di luar folder project yang terbuka
- mengarang endpoint eksternal baru
- mengklaim endpoint live kalau sebenarnya tidak pernah diberikan di requirement
- keluar dari konteks project ini

Jika data endpoint nyata belum tersedia:
- jangan memaksa integrasi palsu
- buat interface/provider yang benar
- gunakan fallback/mock provider yang terisolasi
- dokumentasikan gap tersebut dengan jelas

## Tugas utama

### A. Audit implementasi user master saat ini
Periksa implementasi yang sudah ada untuk data:
- current user
- daftar approver
- daftar karyawan
- jabatan
- divisi/bagian
- unit kerja

Untuk masing-masing sumber data, tentukan dengan jelas:
1. apakah berasal dari response login nyata
2. apakah berasal dari endpoint live yang memang sudah diberikan
3. apakah masih mock/dummy/static data
4. apakah bercampur secara tidak jelas antara live dan fallback

### B. Rapikan arsitektur integrasi
Jika implementasi saat ini masih bercampur, rapikan agar ada pemisahan yang jelas antara:
- adapter live
- provider interface
- fallback/mock provider
- mapper/normalizer internal

Tujuannya:
- mudah diganti ke endpoint nyata nanti
- tidak menipu secara teknis
- tidak membuat frontend/backend seolah-olah live padahal masih dummy

### C. Aturan yang wajib dipatuhi
1. Jangan mengarang endpoint eksternal baru.
2. Jangan mengklaim integrasi final untuk:
   - daftar divisi
   - unit kerja
   - daftar karyawan
   - jabatan
   jika endpoint nyatanya belum pernah diberikan.
3. Jika current user bisa diambil dari login response nyata, gunakan itu.
4. Jika master data lain belum punya endpoint nyata:
   - buat provider interface yang rapi
   - buat mock/fallback provider yang terisolasi
   - tandai jelas di dokumentasi bahwa data masih fallback
5. Jangan campurkan mock provider dengan live adapter dalam satu implementasi yang membingungkan.

### D. Requirement backend
Pastikan backend memiliki struktur yang rapi untuk area ini, misalnya:
- interface/provider untuk employee directory source
- live adapter hanya untuk data yang benar-benar punya endpoint nyata
- fallback/mock adapter untuk data yang belum punya endpoint
- mapper ke model internal yang konsisten
- dokumentasi status integrasi per data source

Jika perlu, tambahkan endpoint internal backend yang menjelaskan source data yang dipakai atau minimal dokumentasikan dengan jelas di README/docs.

### E. Requirement frontend
Pastikan frontend:
- tidak mengasumsikan semua dropdown sudah live jika memang belum
- tetap bisa berjalan dengan provider data yang ada
- menampilkan data current user dari sumber yang benar
- tetap memungkinkan pemilihan approver/divisi/unit/jabatan melalui source yang tersedia
- tidak menyembunyikan fakta bahwa sebagian data masih fallback bila itu penting untuk developer

Jika perlu, rapikan service layer/frontend data source agar lebih mudah diganti nanti.

### F. Dokumentasi yang wajib
Dokumentasikan dengan jelas:

1. mana data yang sudah live
2. mana data yang masih fallback/mock
3. alasan kenapa masih fallback/mock
4. bagaimana contract/interface-nya
5. apa yang perlu disediakan nanti agar fallback bisa diganti ke endpoint nyata
6. file/env/config apa yang relevan

### G. Scope guard
Jangan keluar dari fokus prompt ini.
Jangan menambah domain/fitur baru di luar kebutuhan merapikan integrasi user master.

Jangan mengimplementasikan modul baru seperti:
- PR
- HPS
- PO
- SPP
- Voucher
- Register

## Standar implementasi
- jujur secara teknis
- rapi secara arsitektur
- tidak hardcode endpoint palsu
- tidak hardcode data seolah production-ready tanpa penandaan yang jelas
- prioritaskan maintainability
- pisahkan interface, live adapter, dan fallback/mock provider

## Verifikasi wajib
Sebelum selesai:
1. ringkas source untuk tiap data berikut:
   - current user
   - approver list
   - employee list
   - division list
   - work unit list
   - job title data
2. pastikan code path live vs fallback jelas
3. pastikan dokumentasi menyebut mana live dan mana fallback/mock
4. pastikan implementasi tetap berjalan tanpa mengarang endpoint baru

## Output akhir yang saya inginkan
Saat selesai, laporkan:
1. audit hasil integrasi user master
2. mana yang live
3. mana yang fallback/mock
4. bagian mana yang Anda rapikan
5. file/folder utama yang berubah
6. dokumentasi apa yang ditambahkan/diubah
7. gap yang masih tersisa agar integrasi final bisa benar-benar live

Mulai sekarang:
- audit implementasi user master yang sudah ada
- rapikan arsitekturnya
- dokumentasikan dengan jujur
- selesaikan dalam batas workspace project ini saja
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
15. Prompt 16
16. Prompt 17

Jika ingin lebih hemat iterasi, gabungkan step backend dan frontend per milestone, tetapi jangan mulai dari UI sebelum struktur domain dan database cukup stabil.

