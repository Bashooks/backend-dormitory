package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Document;
import java.util.List;
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByPersonId(Long personId);
}
