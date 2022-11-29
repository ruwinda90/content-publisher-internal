package com.example.contentpub.internal.domain.db.repository;

import com.example.contentpub.internal.domain.db.entity.Content;
import com.example.contentpub.internal.domain.db.projection.CompactContentDbView;
import com.example.contentpub.internal.domain.db.projection.ContentDbView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {

    Optional<Content> findById(Integer id);

    @Query(value = "SELECT a.id as id, a.title as title, b.summary as summary, b.details as details," +
            " a.created_at as createdAt, a.updated_at as updatedAt, c.id as writerId, c.name as writerName" +
            " FROM (SELECT * FROM content WHERE id = :contentId) AS a" +
            " INNER JOIN content_details AS b ON a.id = b.content_id" +
            " INNER JOIN writer as c ON a.writer_id = c.id;", nativeQuery = true)
    Optional<ContentDbView> findByIdWithDetails(@Param("contentId") Integer id);

    @Query(value = "SELECT a.id as id, a.title as title, a.created_at as createdAt," +
            " a.updated_at as updatedAt, c.id as writerId, c.name as writerName FROM" +
                        " (SELECT * FROM content WHERE category_id = :categoryId" +
                        " ORDER BY updated_at DESC LIMIT :pageSize OFFSET :offset) AS a" +
                    " INNER JOIN writer as c ON a.writer_id = c.id;", nativeQuery = true)
    List<CompactContentDbView> findContentByCategoryWithPagination(@Param("categoryId") Integer categoryId,
                                                                   @Param("pageSize") Integer pageSize,
                                                                   @Param("offset") Integer offset);

    @Query(value = "SELECT COUNT(*) FROM content WHERE category_id = :categoryId", nativeQuery = true)
    Integer findContentCountByCategory(@Param("categoryId") Integer categoryId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Content c WHERE c.id = :contentId")
    void delete(@Param("contentId") Integer id);

}
