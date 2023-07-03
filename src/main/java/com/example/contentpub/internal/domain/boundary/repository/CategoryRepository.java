package com.example.contentpub.internal.domain.boundary.repository;

import com.example.contentpub.internal.external.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findById(Integer id);

    boolean existsById(Integer id);

}
