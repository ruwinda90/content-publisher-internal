package com.example.contentpub.internal.domain.boundary.repository;

import com.example.contentpub.internal.external.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findById(Integer id);

}
