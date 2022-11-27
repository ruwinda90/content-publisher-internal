package com.example.contentpub.internal.domain.db.repository;

import com.example.contentpub.internal.domain.db.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findById(Integer id);

}
