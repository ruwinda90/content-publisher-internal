package com.example.contentpub.internal.domain.boundary.repository;

import com.example.contentpub.internal.external.entity.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Integer> {

    @Query(value = "SELECT id FROM writer WHERE user_id = :userId", nativeQuery = true)
    Integer findWriterId(Integer userId);

}
