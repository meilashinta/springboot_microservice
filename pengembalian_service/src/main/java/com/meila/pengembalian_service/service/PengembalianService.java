package com.meila.pengembalian_service.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meila.pengembalian_service.model.Pengembalian;
import com.meila.pengembalian_service.repository.PengembalianRepository;
import com.meila.pengembalian_service.vo.Anggota;
import com.meila.pengembalian_service.vo.Buku;
import com.meila.pengembalian_service.vo.Peminjaman;
import com.meila.pengembalian_service.vo.ResponseTemplate;

@Service
public class PengembalianService {

    @Autowired
    private PengembalianRepository pengembalianRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    // Semua pengembalian
    public List<Pengembalian> getAllPengembalians() {
        return pengembalianRepository.findAll();
    }

    // Cari pengembalian berdasarkan id
    public Pengembalian getPengembalianById(Long id) {
        return pengembalianRepository.findById(id).orElse(null);
    }

    // Buat pengembalian baru
    public Pengembalian createPengembalian(Pengembalian pengembalian) throws ParseException {
        Peminjaman peminjaman = this.getPeminjaman(pengembalian.getPeminjamanId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date tanggalKembali = sdf.parse(peminjaman.getTanggalKembali());
        Date tanggalDikembalikan = sdf.parse(pengembalian.getTanggalDikembalikan());

        long diffInMillies = tanggalDikembalikan.getTime() - tanggalKembali.getTime();
        long jumlahHari = diffInMillies < 0 ? 0 : Math.abs(diffInMillies);
        long terlambat = TimeUnit.DAYS.convert(jumlahHari, TimeUnit.MILLISECONDS);
        double denda = terlambat * 2000;

        pengembalian.setTerlambat(terlambat + " Hari");
        pengembalian.setDenda(denda);

        return pengembalianRepository.save(pengembalian);
    }

    // Hapus pengembalian
    public void deletePengembalian(Long id) {
        pengembalianRepository.deleteById(id);
    }

    // Ambil pengembalian beserta detail: peminjaman, anggota, buku
    public List<ResponseTemplate> getPengembalianWithDetailById(Long id) {
        List<ResponseTemplate> responseTemplates = new ArrayList<>();
        Pengembalian pengembalian = getPengembalianById(id);

        if (pengembalian == null) return null;

        ServiceInstance serviceInstance = discoveryClient.getInstances("API-GATEWAY-PUSTAKA").get(0);

        // Ambil peminjaman
        Peminjaman peminjaman = restTemplate.getForObject(
                serviceInstance.getUri() + "/api/peminjaman/" + pengembalian.getPeminjamanId(),
                Peminjaman.class);

        // Ambil anggota
        Anggota anggota = restTemplate.getForObject(
                serviceInstance.getUri() + "/api/anggota/" + peminjaman.getAnggotaId(),
                Anggota.class);

        // Ambil buku
        Buku buku = restTemplate.getForObject(
                serviceInstance.getUri() + "/api/buku/" + peminjaman.getBukuId(),
                Buku.class);

        // Buat ResponseTemplate
        ResponseTemplate vo = new ResponseTemplate();
        vo.setPengembalian(pengembalian);
        vo.setPeminjaman(peminjaman);
        vo.setAnggota(anggota);
        vo.setBuku(buku);

        responseTemplates.add(vo);
        return responseTemplates;
    }

    // Ambil peminjaman (untuk perhitungan denda)
    public Peminjaman getPeminjaman(Long id) {
        try {
            ServiceInstance serviceInstancePeminjaman = discoveryClient.getInstances("API-GATEWAY-PUSTAKA").get(0);
            return restTemplate.getForObject(
                    serviceInstancePeminjaman.getUri() + "/api/peminjaman/" + id, Peminjaman.class);
        } catch (Exception e) {
            return null;
        }
    }
}
