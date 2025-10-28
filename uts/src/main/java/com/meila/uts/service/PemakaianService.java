package com.meila.uts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meila.uts.dto.PemakaianDTO;
import com.meila.uts.model.Pemakaian;
import com.meila.uts.repository.PemakaianRepository;

@Service
public class PemakaianService {
    @Autowired
    private PemakaianRepository pemakaianRepository;

    public List<Pemakaian> getAllPemakaian() {
        return pemakaianRepository.findAll();
    }

    public Pemakaian getPemakaianById(Long id) {
        return pemakaianRepository.findById(id).orElse(null);
    }

    public Pemakaian createPemakaian(Pemakaian pemakaian) {
        String kodeTransaksi = pemakaian.getKodeTransaksi();
        String pelanggan = pemakaian.getPelanggan();
        Double meterBulanIni = pemakaian.getMeterBulanIni();
        Double meterBulanLalu = pemakaian.getMeterBulanLalu();
        Double tarifPerMeter = pemakaian.getTarifPerMeter();

        PemakaianDTO dto = new PemakaianDTO(kodeTransaksi, pelanggan, meterBulanIni, meterBulanLalu, tarifPerMeter);

        pemakaian.setPemakaian(dto.getPemakaian());
        pemakaian.setTotal(dto.getTotal());

        return pemakaianRepository.save(pemakaian);
    }

    public Pemakaian updatePemakaian(Long id, Pemakaian pemakaian) {
        Pemakaian oldData = this.getPemakaianById(id);
        if (oldData == null) {
            return null;
        }
        String kodeTransaksi = oldData.getKodeTransaksi();
        String pelanggan = pemakaian.getPelanggan();
        Double meterBulanIni = pemakaian.getMeterBulanIni();
        Double meterBulanLalu = pemakaian.getMeterBulanLalu();
        Double tarifPerMeter = pemakaian.getTarifPerMeter();

        PemakaianDTO dto = new PemakaianDTO(kodeTransaksi, pelanggan, meterBulanIni, meterBulanLalu, tarifPerMeter);

        oldData.setPelanggan(dto.getPelanggan());
        oldData.setMeterBulanIni(dto.getMeterBulanIni());
        oldData.setMeterBulanLalu(dto.getMeterBulanLalu());
        oldData.setPemakaian(dto.getPemakaian());
        oldData.setTarifPerMeter(dto.getTarifPerMeter());
        oldData.setTotal(dto.getTotal());

        return pemakaianRepository.save(oldData);
    }

    public void deletePemakaian(Long id) {
        pemakaianRepository.deleteById(id);
    }
}
