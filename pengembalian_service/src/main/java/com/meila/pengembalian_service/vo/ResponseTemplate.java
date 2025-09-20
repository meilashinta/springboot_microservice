package com.meila.pengembalian_service.vo;

import com.meila.pengembalian_service.model.Pengembalian;

public class ResponseTemplate {
    private Pengembalian pengembalian;
    private Peminjaman peminjaman;
    private Anggota anggota;
    private Buku buku;

    public ResponseTemplate() {
    }

    public ResponseTemplate(Pengembalian pengembalian, Peminjaman peminjaman, Anggota anggota, Buku buku) {
        this.pengembalian = pengembalian;
        this.peminjaman = peminjaman;
        this.anggota = anggota;
        this.buku = buku;
    }

    public Pengembalian getPengembalian() {
        return pengembalian;
    }

    public void setPengembalian(Pengembalian pengembalian) {
        this.pengembalian = pengembalian;
    }

    public Peminjaman getPeminjaman() {
        return peminjaman;
    }

    public void setPeminjaman(Peminjaman peminjaman) {
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
}
