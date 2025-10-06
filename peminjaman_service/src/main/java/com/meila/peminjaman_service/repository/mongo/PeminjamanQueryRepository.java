package com.meila.peminjaman_service.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.meila.peminjaman_service.model.PeminjamanQuery;

@Repository
public interface PeminjamanQueryRepository extends MongoRepository<PeminjamanQuery, String> {

}
