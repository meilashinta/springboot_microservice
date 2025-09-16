package com.meila.pelangganservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meila.pelangganservice.model.Pelanggan;
import com.meila.pelangganservice.services.PelangganService;

@RestController
@RequestMapping("/api/pelanggan")
public class PelangganController {

    @Autowired
    private PelangganService pelangganService;

    @GetMapping
    public List<Pelanggan> getAllPelanggan() {
        return pelangganService.getAllPelanggan();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelanggan> getPelangganById(@PathVariable Long id) {
        Pelanggan pelanggan = pelangganService.getPelangganById(id);
        return pelanggan != null ? ResponseEntity.ok(pelanggan) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Pelanggan createPelanggan(@RequestBody Pelanggan pelanggan) {
        return pelangganService.createPelanggan(pelanggan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePelanggan(@PathVariable Long id) {
        pelangganService.deletePelanggan(id);
        return ResponseEntity.ok().build();
    }
}
