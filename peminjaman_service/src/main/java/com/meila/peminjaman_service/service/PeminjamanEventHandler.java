package com.meila.peminjaman_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.meila.peminjaman_service.model.PeminjamanCommand;
import com.meila.peminjaman_service.model.PeminjamanQuery;
import com.meila.peminjaman_service.repository.mongo.PeminjamanQueryRepository;
import com.meila.peminjaman_service.vo.Anggota;
import com.meila.peminjaman_service.vo.Buku;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeminjamanEventHandler {

    private final PeminjamanQueryRepository peminjamanQueryRepository;
    private final RestTemplate restTemplate;

    @RabbitListener(queues = "${app.rabbitmq.queue.transaction}")
    @Transactional
    public void consume(PeminjamanCommand event) {
        // Validasi event null
        if (event == null || event.getId() == null) {
            log.warn("Menerima event null atau tanpa ID, pesan diabaikan.");
            return;
        }

        String id = event.getId().toString();

        // Handling DELETE
        if (PeminjamanCommand.EventType.DELETED.equals(event.getEventType())) {
            peminjamanQueryRepository.deleteById(id);
            log.info("🗑️ Delete ke MongoDB berhasil untuk ID: {}", id);
            return;
        }

        // Ambil entity dari MongoDB atau buat baru
        PeminjamanQuery entity = peminjamanQueryRepository.findById(id)
                .orElse(new PeminjamanQuery());

        // Ambil data Anggota
        try {
            log.info("Mengambil data anggota dengan ID: {}", event.getAnggotaId());
            String anggotaUrl = "http://localhost:9002/api/anggota/" + event.getAnggotaId();
            Anggota anggota = restTemplate.getForObject(anggotaUrl, Anggota.class);

            if (anggota != null) {
                entity.setNama(anggota.getNama());
                entity.setEmail(anggota.getEmail());
                entity.setJenisKelamin(anggota.getJenisKelamin());
            }
        } catch (Exception e) {
            log.warn("Gagal mengambil data anggota: {}", e.getMessage());
        }

        // Ambil data Buku
        try {
            log.info("Mengambil data buku dengan ID: {}", event.getBukuId());
            String bukuUrl = "http://localhost:9002/api/buku/" + event.getBukuId();
            Buku buku = restTemplate.getForObject(bukuUrl, Buku.class);

            entity.setBukuId(event.getBukuId());
            if (buku != null) {
                entity.setJudulBuku(buku.getJudul());
                entity.setPengarangBuku(buku.getPengarang());
                entity.setPenerbitBuku(buku.getPenerbit());
                entity.setTahunTerbitBuku(buku.getTahunTerbit());
            }
        } catch (Exception e) {
            log.warn("Gagal mengambil data buku: {}", e.getMessage());
        }

        // Set tanggal pinjam/kembali
        entity.setId(id);
        entity.setTanggalPinjam(event.getTanggalPinjam());
        entity.setTanggalKembali(event.getTanggalKembali());
        entity.setAnggotaId(event.getAnggotaId());

        // Simpan ke MongoDB
        peminjamanQueryRepository.save(entity);

        if (PeminjamanCommand.EventType.CREATED.equals(event.getEventType())) {
            log.info("📩 Insert ke MongoDB berhasil untuk ID: {}", id);
        } else {
            log.info("✅ Update ke MongoDB berhasil untuk ID: {}", id);
        }
    }
}
