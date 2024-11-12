package com.software.ott.content.repository;

import com.software.ott.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    long count();

    List<Content> findAllByTitleContaining(String title);

    List<Content> findAllByListedInContaining(String genre);

    List<Content> findAllByCastContaining(String cast);
}
