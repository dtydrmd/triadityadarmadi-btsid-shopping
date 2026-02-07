# SAP - Listing Data Hutang per Vendor

Dokumen ini berisi cara cepat untuk menampilkan data hutang (accounts payable)
per vendor/supplier di SAP. Gunakan salah satu opsi sesuai kebutuhan.

## Opsi 1 (Disarankan): Vendor Balances

Tampilkan saldo hutang per vendor dalam satu report ringkas.

**SAP GUI (ECC / S4HANA):**

- TCode: `S_ALR_87012082` (Vendor Balances)
- Isi parameter:
  - Company Code
  - Key Date
  - Vendor (opsional, bisa range)
  - Currency (opsional)
- Execute (F8)
- Output menampilkan saldo per vendor.

Jika butuh item yang masih open saja, gunakan:
- TCode: `S_ALR_87012085` (Vendor Open Items)

## Opsi 2: Vendor Line Items (Detail per vendor)

Gunakan jika perlu detail invoice/credit memo per vendor.

**SAP GUI:**

- TCode: `FBL1N` (Vendor Line Item)
- Isi parameter:
  - Company Code
  - Vendor (single atau range)
  - Open Items / Cleared Items / All Items
  - Key Date atau Posting Date range
- Execute (F8)
- Agar terlihat total per vendor:
  - Sort by `Vendor`
  - Subtotals by `Vendor`
  - Simpan layout/variant jika sering dipakai

## Opsi 3: Quick Check per Vendor

Cek cepat saldo vendor tertentu.

- TCode: `FK10N` (Vendor Account Balance)
- Masukkan Company Code dan Vendor

## S/4HANA Fiori (Jika tersedia)

Gunakan aplikasi Fiori berikut:

- **Display Supplier Balances**
- **Supplier Line Items**

Filter dengan Company Code, Key Date, dan status Open Items.
Gunakan opsi `Export to Spreadsheet` untuk rekap.

## Tips Umum

- Pastikan memilih **Open Items** jika fokus hutang yang belum dibayar.
- Cek **Special G/L** bila ada down payment.
- Simpan layout/variant agar konsisten dipakai tim.
