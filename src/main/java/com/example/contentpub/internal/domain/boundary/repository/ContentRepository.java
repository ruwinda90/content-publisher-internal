package com.example.contentpub.internal.domain.boundary.repository;

import com.example.contentpub.internal.domain.dto.view.CompactContentItemData;
import com.example.contentpub.internal.domain.dto.view.ContentItemData;
import com.example.contentpub.internal.external.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {

    Optional<Content> findById(Integer id);

    @Query(value = "SELECT new com.example.contentpub.internal.domain.dto.view.ContentItemData(" +
            "c.id, c.title, c.contentDetails.summary, c.contentDetails.details, c.createdAt, c.updatedAt, " +
            "c.writer.id, c.writer.name)" +
            " FROM Content c WHERE c.id =:contentId")
    Optional<ContentItemData> findByIdWithDetails(@Param("contentId") Integer id);

    @Query(value = "SELECT new com.example.contentpub.internal.domain.dto.view.CompactContentItemData(" +
            "c.id, c.title, c.createdAt, c.updatedAt, c.writer.id, c.writer.name) " +
            "FROM Content c WHERE c.category.id =:categoryId")
    Page<CompactContentItemData> findContentByCategoryWithPagination(@Param("categoryId") Integer categoryId,
                                                                     Pageable pageRequest);

    @Transactional
    @Modifying
    @Query("DELETE FROM Content c WHERE c.id = :contentId")
    void delete(@Param("contentId") Integer id);

}
