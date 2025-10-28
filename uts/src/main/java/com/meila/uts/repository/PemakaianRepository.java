package com.meila.uts.repository;

import com.meila.uts.model.Pemakaian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PemakaianRepository extends JpaRepository<Pemakaian, Long> {
}
