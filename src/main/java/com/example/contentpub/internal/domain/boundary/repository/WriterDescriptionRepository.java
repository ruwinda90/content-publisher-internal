package com.example.contentpub.internal.domain.boundary.repository;

import com.example.contentpub.internal.external.entity.WriterDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterDescriptionRepository extends JpaRepository<WriterDescription, Integer> {

}
