package com.meila.uts.controller;

import com.meila.uts.dto.PemakaianDTO;
import com.meila.uts.model.Pemakaian;
import com.meila.uts.service.PemakaianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pemakaian")
public class PemakaianController {

    @Autowired
    private PemakaianService pemakaianService;

    @GetMapping
    public List<Pemakaian> getAllPemakaian() {
        return pemakaianService.getAllPemakaian();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pemakaian> getPemakaianById(@PathVariable Long id) {
        Pemakaian pemakaian = pemakaianService.getPemakaianById(id);
        return pemakaian != null ? ResponseEntity.ok(pemakaian) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Pemakaian createPemakaian(@RequestBody Pemakaian pemakaian) {
        return pemakaianService.createPemakaian(pemakaian);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pemakaian> updatePemakaian(@PathVariable Long id, @RequestBody Pemakaian pemakaianBaru) {
        Pemakaian updated = pemakaianService.updatePemakaian(id, pemakaianBaru);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePemakaian(@PathVariable Long id) {
        pemakaianService.deletePemakaian(id);
        return ResponseEntity.ok().build();
    }
}
