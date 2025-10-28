package com.meila.uts.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pemakaian")
public class Pemakaian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kodeTransaksi;
    private String pelanggan;
    private Double meterBulanIni;
    private Double meterBulanLalu;
    private Double pemakaian;
    private Double tarifPerMeter;
    private Double total;

}
