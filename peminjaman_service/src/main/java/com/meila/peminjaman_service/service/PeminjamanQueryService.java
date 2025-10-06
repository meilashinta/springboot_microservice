package com.meila.peminjaman_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meila.peminjaman_service.model.PeminjamanQuery;
import com.meila.peminjaman_service.repository.mongo.PeminjamanQueryRepository;

@Service
public class PeminjamanQueryService{

    private final PeminjamanQueryRepository peminjamanQueryRepository;

    public PeminjamanQueryService(PeminjamanQueryRepository peminjamanQueryRepository) {
        this.peminjamanQueryRepository = peminjamanQueryRepository;
    }

    public List<PeminjamanQuery> getAllPeminjaman(){
        return peminjamanQueryRepository.findAll();
    }

    public PeminjamanQuery getPeminjamanById(String id){
        return peminjamanQueryRepository.findById(id).orElseThrow(() -> new RuntimeException("Peminjaman tidak ditemukan"));
    }
}
