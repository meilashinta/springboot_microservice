package com.meila.peminjaman_service.controller;

import com.meila.peminjaman_service.model.PeminjamanQuery;
import com.meila.peminjaman_service.service.PeminjamanQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peminjaman/query")
@RequiredArgsConstructor
public class PeminjamanQueryController {

    private final PeminjamanQueryService peminajamQueryService;

    @GetMapping
    public ResponseEntity<List<PeminjamanQuery>> getAllPeminjaman() {
        return ResponseEntity.ok(peminajamQueryService.getAllPeminjaman());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeminjamanQuery> getPeminjamanById(@PathVariable String id) {
        PeminjamanQuery peminjaman = peminajamQueryService.getPeminjamanById(id);
        return ResponseEntity.ok(peminjaman);
    }
}
