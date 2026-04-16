# Nusaproc & Nusafina Project Specification

## 1. Ringkasan

Dokumen ini menjadi baseline spesifikasi proyek untuk pengembangan dua domain aplikasi:

- **Nusaproc**: procurement, mencakup PR, HPS, proses pengadaan, PO, dan keterkaitan ke pembayaran.
- **Nusafina**: finance, mencakup anggaran, PPAB, permohonan pembayaran, voucher, register kas/bank, dan monitoring serapan anggaran.

Fase implementasi awal difokuskan pada **modul PPAB (Permohonan Penggunaan Anggaran)** sebagai dokumen awal sebelum proses PR.

---

## 2. Tujuan Produk

Sistem dibangun untuk:

1. Mengelola alur dokumen procurement dan finance secara terstruktur.
2. Menyediakan workflow approval berjenjang dengan monitoring status yang jelas.
3. Mengintegrasikan data user/karyawan dan data anggaran dari sistem eksternal.
4. Menyimpan capaian anggaran internal yang tidak diakomodir di database anggaran eksternal.
5. Menjadikan PPAB sebagai sumber awal komitmen anggaran yang akan diturunkan ke PR dan proses berikutnya.

---

## 3. Ruang Lingkup Domain

### 3.1 Nusaproc

Ruang lingkup bisnis Nusaproc:

- PR dibuat per kegiatan dan diajukan oleh unit kerja
- satu PR dapat memiliki banyak item
- HPS dapat direvisi berkali-kali
- metode pengadaan mencakup:
  - pengadaan langsung
  - penunjukan langsung
  - tender/seleksi
  - pembelian rutin
- evaluasi vendor formal dengan skor
- output proses procurement minimal:
  - **PO**
  - **permohonan pembayaran**

### 3.2 Nusafina

Ruang lingkup bisnis Nusafina:

- anggaran berdasarkan:
  - tahun anggaran
  - unit kerja
  - program/kegiatan
  - akun/kode anggaran
- alur utama:
  - **PPAB** terlebih dahulu
  - kemudian **PR**
- capaian anggaran dilihat dalam beberapa layer:
  - capaian berdasarkan PPAB
  - capaian berdasarkan HPS
  - capaian berdasarkan realisasi pembayaran
- permohonan penggunaan anggaran dan permohonan pembayaran adalah dokumen terpisah
- bukti pembayaran dan bukti penerimaan dikelola sebagai **voucher**
- voucher memiliki otorisasi berjenjang sesuai kewenangan dan nominal
- monitoring serapan anggaran dapat dilihat per:
  - unit
  - akun
  - program
  - periode
  - vendor

---

## 4. Urutan Dokumen Bisnis

Urutan dokumen yang disepakati:

1. **PPAB**
2. **PR**
3. **HPS**
4. **PO**
5. **SPP**
6. **Voucher**
7. **Register**

Trigger dari satu dokumen ke dokumen berikutnya terjadi **setelah approval final** pada dokumen sebelumnya.

---

## 5. Aktor Sistem

Aktor awal yang telah disepakati:

- seluruh karyawan
- user anggaran
- user HPS
- user pengadaan
- user keuangan
- user akuntansi
- admin

Pada implementasi teknis, aktor-aktor ini dapat dipetakan ke role aplikasi dan permission granular.

---

## 6. Status Dokumen Umum

Status bisnis umum:

- **Draft**
- **Proses**: menunggu approval pihak tertentu
- **Dokumen Diapprove**
- **Reject**

Status teknis yang direkomendasikan untuk backend:

- `DRAFT`
- `SUBMITTED`
- `IN_APPROVAL`
- `APPROVED`
- `REJECTED`

UI boleh tetap menampilkan label bisnis yang lebih sederhana.

---

## 7. Teknologi dan Arsitektur

### 7.1 Stack

- **Frontend**: ReactJS
- **Backend**: Golang
- **Gateway**: seluruh akses frontend diarahkan ke gateway
- **Database**: PostgreSQL 18.3

### 7.2 Pola Workspace

Pengembangan akan dilakukan dalam satu workspace Cursor yang memuat dua direktori utama:

- project frontend ReactJS
- project backend Golang

### 7.3 Prinsip Arsitektur

1. Frontend tidak memanggil service internal secara langsung.
2. Semua request frontend masuk melalui gateway.
3. Backend bertanggung jawab untuk:
   - rule bisnis
   - workflow approval
   - audit trail
   - integrasi API eksternal
   - penyimpanan capaian anggaran internal
4. Database PostgreSQL menjadi source of truth untuk data transaksi internal aplikasi.

---

## 8. Fokus Fase Awal: Modul PPAB

### 8.1 Definisi

PPAB adalah dokumen permohonan penggunaan anggaran yang diajukan oleh user/unit kerja sebagai komitmen awal terhadap anggaran dan menjadi dasar pembentukan PR setelah approval final.

### 8.2 Tujuan Modul PPAB

- memfasilitasi pembuatan draft pengajuan anggaran
- mengelola item kebutuhan dan referensi anggaran
- menjalankan approval berjenjang
- menyimpan capaian anggaran internal
- menyediakan monitoring progress bagi pembuat dan approver

### 8.3 Di luar scope fase awal PPAB

Belum menjadi fokus implementasi awal:

- modul PR penuh
- modul HPS penuh
- modul PO penuh
- modul SPP, voucher, dan register penuh

Namun desain PPAB harus siap untuk integrasi ke modul-modul tersebut.

---

## 9. Requirement Fungsional PPAB

### 9.1 Header PPAB

Field yang disepakati:

1. **Pembuat**
   - readonly
   - diambil dari user login
2. **Divisi/Bagian**
   - searchable dropdown
3. **Tanggal PPAB**
   - default hari ini
   - tidak boleh backdate
   - boleh future date
4. **Perihal**
   - input teks
5. **Target realisasi pengadaan**
   - date
6. **Jenis**
   - Barang
   - Jasa
7. **Jenis Anggaran**
   - Opex
   - Capex
   - Produksi
   - Dana Eksternal
   - Biaya Ditangguhkan
   - PMO
8. **Unit Kerja**
   - searchable dropdown
9. **Dokumen pendukung**
   - upload file maksimal 2 MB

### 9.2 Nomor Dokumen

Ketentuan nomor PPAB:

- nomor PPAB final dibuat saat **approve final**
- sebelum final, sistem menampilkan **nomor bayangan unik** untuk monitoring proses
- nomor bayangan tetap unik dan dapat dipakai user untuk tracking dokumen

### 9.3 Item PPAB

PPAB mendukung item multiple dengan field:

- uraian
- keterangan
- satuan
- nominal satuan
- qty
- pilihan pajak
  - tidak kena pajak
  - 11%
  - 12%
  - 1.1%
- nilai dasar
- nilai pajak
- subtotal/grand total
- referensi anggaran

### 9.4 Rule Pajak

Ketentuan penyimpanan nilai item:

- pajak disimpan **terpisah**
- sistem menghitung otomatis:
  - nilai dasar = nominal satuan x qty
  - nilai pajak = hasil perhitungan sesuai pilihan pajak
  - subtotal/grand total = nilai dasar + nilai pajak

### 9.5 Referensi Anggaran per Item

Satu PPAB hanya boleh memiliki **satu jenis anggaran** di header, tetapi tiap item dapat merujuk ke baris anggaran yang berbeda dalam jenis anggaran yang sama.

Field referensi anggaran pada item:

- kode anggaran
- uraian anggaran
- capaian anggaran
- sisa
- keterangan anggaran

### 9.6 Alur Kewenangan

Alur kewenangan diisi langsung di form dan sudah menentukan urutan approver. Tidak perlu ada proses penentuan approver pertama saat submit, karena urutan dan approver sudah eksplisit pada data form.

Field alur kewenangan:

- urutan otomatis
- mode otorisasi:
  - paraf
  - ttd
- label otorisasi bisnis:
  - diajukan
  - diperiksa
  - disetujui pelaksanaannya
  - diketahui pelaksanaannya
- skip
- nama approver
- jabatan

Ketentuan:

- jabatan otomatis terisi dari nama approver terpilih
- jabatan **boleh diedit** untuk kebutuhan pejabat pengganti sementara
- approver **boleh edit dokumen**
- seluruh perubahan approver wajib masuk **change log**

### 9.7 Tombol dan Aksi

Aksi minimum:

- simpan draft
- kirim draft
- edit draft
- approve
- reject dengan keterangan
- monitoring progress

### 9.8 Alur Approval

Alur approval PPAB:

1. pembuat menyimpan draft
2. pembuat mengirim draft
3. dokumen masuk ke approver urutan aktif pertama sesuai data alur kewenangan
4. approver dapat:
   - edit
   - approve
   - reject
5. jika approve:
   - dokumen lanjut ke approver berikutnya
6. jika approver terakhir approve:
   - status menjadi approved
   - nomor PPAB final diterbitkan
   - dokumen dapat menjadi dasar ke PR
7. jika reject:
   - reject dapat diarahkan ke approver sebelumnya tertentu atau ke pembuat
   - wajib ada keterangan reject
   - riwayat approval tetap disimpan

### 9.9 Monitoring

Pembuat harus dapat memonitor:

- nomor bayangan / nomor final
- tanggal
- perihal
- jenis anggaran
- total nilai
- status
- posisi approval saat ini
- current approver
- riwayat tindakan

---

## 10. Integrasi Eksternal

### 10.1 Integrasi User/Karyawan

Integrasi eksternal mencakup:

- login
- informasi user
- daftar bagian/divisi
- daftar unit kerja
- daftar nama karyawan
- jabatan karyawan

Catatan:

- **tidak ada pengambilan alur kewenangan dari sistem eksternal**
- alur kewenangan disusun manual di form PPAB

### 10.2 Integrasi Database Anggaran

Mapping jenis anggaran ke sumber data:

- Opex -> endpoint RKAP OPEX
- Capex -> endpoint investasi
- Produksi -> endpoint produksi
- Biaya Ditangguhkan -> endpoint ditangguhkan
- PMO -> endpoint PMO
- Dana Eksternal -> input manual oleh user

Semua hasil integrasi harus dinormalisasi ke model internal yang seragam agar frontend tidak bergantung pada bentuk response yang berbeda-beda.

### 10.3 Keamanan Integrasi

Ketentuan integrasi:

- credential, token, dan header sensitif tidak boleh di-hardcode
- gunakan environment variable atau mekanisme konfigurasi rahasia
- lakukan logging yang aman tanpa membocorkan rahasia

---

## 11. Capaian Anggaran Internal

Sistem harus menyimpan capaian anggaran internal karena tidak tersedia di database anggaran eksternal.

### 11.1 Layer capaian

Minimal terdapat tiga layer capaian:

1. capaian berdasarkan **PPAB**
2. capaian berdasarkan **HPS**
3. capaian berdasarkan **realisasi pembayaran**

### 11.2 Prinsip pencatatan

Disarankan menggunakan model **ledger/mutasi**, bukan sekadar menyimpan angka final di master budget.

Keuntungan:

- histori perubahan dapat ditelusuri
- aman untuk audit
- mudah menghitung capaian per tahap
- memudahkan penyesuaian saat revisi

### 11.3 Waktu pencatatan

Baseline yang disepakati:

- capaian dapat dibedakan antara komitmen submitted dan komitmen approved
- capaian utama PPAB resmi masuk setelah **approve final**

### 11.4 Penyesuaian saat perubahan

Jika approver mengubah item atau nominal:

- capaian anggaran harus dihitung ulang
- perubahan harus tercatat di change log

---

## 12. Kebutuhan Audit dan Logging

Karena approver dapat mengubah dokumen, audit trail wajib menjadi fitur inti.

### 12.1 Change log

Sistem menyimpan:

- field yang berubah
- nilai lama
- nilai baru
- siapa yang mengubah
- kapan perubahan terjadi
- konteks perubahan, misalnya saat draft atau saat approval

### 12.2 Approval log

Sistem menyimpan:

- approver
- urutan
- aksi
- catatan
- waktu aksi
- target reject jika ada

### 12.3 Activity timeline

Timeline minimal:

- draft dibuat
- draft diperbarui
- draft dikirim
- approve per step
- reject
- revisi
- approve final

---

## 13. Model Entitas Konseptual

Entitas minimum yang direkomendasikan:

- `users`
- `employees`
- `divisions`
- `work_units`
- `ppab`
- `ppab_items`
- `ppab_approval_steps`
- `ppab_approval_actions`
- `ppab_status_histories`
- `ppab_change_logs`
- `ppab_attachments`
- `budget_reference_cache`
- `budget_achievement_ledger`

---

## 14. Validasi Minimum

### 14.1 Validasi Header

- pembuat wajib berasal dari session login
- divisi/bagian wajib
- tanggal PPAB wajib
- tanggal PPAB tidak boleh backdate
- perihal wajib
- target realisasi wajib
- jenis wajib
- jenis anggaran wajib
- unit kerja wajib

### 14.2 Validasi Item

- minimal ada satu item
- uraian wajib
- satuan wajib
- qty harus lebih dari nol
- nominal satuan harus valid
- pilihan pajak wajib
- referensi anggaran wajib untuk non dana eksternal
- input manual wajib untuk dana eksternal

### 14.3 Validasi Approval

- minimal ada satu approver
- urutan tidak boleh duplikat
- nama approver wajib
- mode otorisasi wajib
- label otorisasi bisnis wajib

### 14.4 Validasi File

- ukuran maksimum 2 MB
- batasi tipe file sesuai kebijakan implementasi

---

## 15. Prinsip UX untuk PPAB

Prinsip awal UI:

1. Form harus mendukung item multiple dengan perhitungan otomatis.
2. Dropdown organisasi, unit kerja, user, dan anggaran harus searchable.
3. Status approval harus mudah dipahami pembuat.
4. Riwayat approval dan change log harus bisa dilihat.
5. User harus selalu tahu:
   - sedang menunggu siapa
   - dokumen terakhir berubah oleh siapa
   - apakah ada reject dan alasannya

---

## 16. Deliverable Fase Implementasi Awal

Target implementasi awal untuk PPAB:

1. struktur backend dan frontend dasar
2. autentikasi berbasis integrasi user eksternal
3. lookup data user, divisi, unit kerja, dan budget reference
4. create/edit/save draft PPAB
5. item multiple dengan kalkulasi pajak terpisah
6. approval berjenjang
7. reject ke approver tertentu atau pembuat
8. change log
9. attachment upload
10. monitoring progress
11. ledger capaian anggaran PPAB

---

## 17. Keputusan yang Sudah Dikunci

- pajak disimpan terpisah
- subtotal/grand total mengikuti pendekatan nilai dasar + nilai pajak
- field otorisasi merupakan kombinasi mode tanda tangan dan label otorisasi bisnis
- ada opsi skip pada alur kewenangan
- jabatan otomatis dari nama, tetapi boleh diedit
- approver boleh edit dokumen
- edit oleh approver wajib masuk change log
- urutan approver tidak ditentukan lagi saat submit karena sudah dibentuk di form
- tanggal PPAB boleh future date, tetapi tidak boleh backdate
- integrasi eksternal tidak mencakup pengambilan alur kewenangan
- nomor final dibuat saat approve final
- sistem menyediakan nomor bayangan unik sebelum final
- satu PPAB hanya punya satu jenis anggaran
- satu PPAB boleh memiliki banyak item dengan banyak referensi anggaran dalam jenis yang sama
- perubahan item/nilai wajib menghitung ulang capaian
- reject/revisi tetap memakai dokumen yang sama dengan history tetap tersimpan
- alur kewenangan boleh diubah

---

## 18. Open Item Lanjutan

Hal-hal berikut dapat diputuskan pada tahap desain detail:

1. format nomor bayangan dan nomor final
2. struktur permission yang lebih granular per role
3. kebijakan tipe file attachment yang diizinkan
4. strategi notifikasi
5. desain gateway secara detail
6. pemetaan user login eksternal ke session internal aplikasi
7. strategi cache untuk data budget reference dan employee directory

---

## 19. Prinsip Implementasi

Saat masuk ke tahap coding:

1. mulai dari model domain dan workflow, bukan dari UI terlebih dahulu
2. pisahkan integrasi eksternal di adapter yang jelas
3. gunakan migrasi database sejak awal
4. prioritaskan auditability dan traceability
5. pastikan setiap perubahan status terdokumentasi
6. siapkan desain agar modul PR dapat menggunakan PPAB approved tanpa refactor besar

