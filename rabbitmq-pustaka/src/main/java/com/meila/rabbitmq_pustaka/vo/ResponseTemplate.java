package com.meila.rabbitmq_pustaka.vo;

public class ResponseTemplate {
    Peminjaman peminjaman;
    Anggota anggota;
    Buku buku;

    public ResponseTemplate() {
    }

    public ResponseTemplate(Anggota anggota, Buku buku, Peminjaman peminjaman) {
        this.anggota = anggota;
        this.buku = buku;
        this.peminjaman = peminjaman;
    }

    public Anggota getAnggota() {
        return anggota;
    }

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public Peminjaman getPeminjaman() {
        return peminjaman;
    }

    public void setPeminjaman(Peminjaman peminjaman) {
        this.peminjaman = peminjaman;
    }

   public String sendMailMessage() {
    StringBuilder message = new StringBuilder();
    message.append("Subject: Konfirmasi Peminjaman Buku Berhasil\n\n");
    message.append("========================================\n");
    message.append("      KONFIRMASI PEMINJAMAN BUKU       \n");
    message.append("========================================\n\n");

    message.append("Yth. ").append(anggota.getNama()).append(" (").append(anggota.getNim()).append(")\n");
    message.append("Jenis Kelamin: ").append(anggota.getJenisKelamin()).append("\n\n");

    message.append("Terima kasih telah meminjam buku di Perpustakaan Digital.\n");
    message.append("Berikut rincian peminjaman Anda:\n\n");

    message.append("ID Peminjaman  : ").append(peminjaman.getId()).append("\n");
    message.append("Judul Buku     : ").append(buku.getJudul()).append("\n");
    message.append("Pengarang      : ").append(buku.getPengarang()).append("\n");
    message.append("Tanggal Pinjam : ").append(peminjaman.getTanggalPinjam()).append("\n");
    message.append("Tanggal Kembali: ").append(peminjaman.getTanggalKembali()).append("\n\n");

    message.append("Harap kembalikan buku tepat waktu untuk menghindari denda.\n\n");

    message.append("Salam,\n");
    message.append("Tim Perpustakaan Digital\n");
    message.append("========================================\n");

    return message.toString();
}

}
