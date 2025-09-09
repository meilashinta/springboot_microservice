package com.meila.orderservice.vo;

public class Produk {
    private Long id;
    private String nama;
    private String satuan;
    private String harga;

    public Produk() {

    }

    public Produk(Long id, String nama, String satuan, String harga) {
        this.id = id;
        this.nama = nama;
        this.satuan = satuan;
        this.harga = harga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}