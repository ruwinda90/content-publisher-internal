package com.example.contentpub.internal.domain.db.repository;

import com.example.contentpub.internal.domain.db.entity.ContentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDetailsRepository extends JpaRepository<ContentDetails, Integer> {

}
