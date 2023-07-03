package com.example.contentpub.internal.domain.boundary.repository;

import com.example.contentpub.internal.external.entity.ContentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDetailsRepository extends JpaRepository<ContentDetails, Integer> {

}
