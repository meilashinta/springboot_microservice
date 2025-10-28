package com.meila.uts.dto;

public class PemakaianDTO {
    private Long id;
    private String kodeTransaksi;
    private String pelanggan;
    private Double meterBulanIni;
    private Double meterBulanLalu;
    private Double pemakaian;
    private Double tarifPerMeter;
    private Double total;

    public PemakaianDTO(
        String kodeTransaksi, String pelanggan, Double meterBulanIni, Double meterBulanLalu,
            Double tarifPerMeter) {
        this.kodeTransaksi = kodeTransaksi;
        this.pelanggan = pelanggan;
        this.meterBulanIni = meterBulanIni;
        this.meterBulanLalu = meterBulanLalu;
        this.tarifPerMeter = tarifPerMeter;
        this.setPemakaian();
        this.setTotal();
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) 
    { 
        this.id = id; 
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public Double getMeterBulanIni() {
        return meterBulanIni;
    }

    public void setMeterBulanIni(Double meterBulanIni) {
        this.meterBulanIni = meterBulanIni;
    }

    public Double getMeterBulanLalu() {
        return meterBulanLalu;
    }

    public void setMeterBulanLalu(Double meterBulanLalu) {
        this.meterBulanLalu = meterBulanLalu;
    }

    public Double getPemakaian() {
        return pemakaian;
    }

    public void setPemakaian() {
        this.pemakaian = this.meterBulanIni - this.meterBulanLalu;
    }

    public Double getTarifPerMeter() {
        return tarifPerMeter;
    }

    public void setTarifPerMeter(Double tarifPerMeter) {
        this.tarifPerMeter = tarifPerMeter;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal() {
        this.total = this.pemakaian * this.tarifPerMeter;
    }

}
