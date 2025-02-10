package com.example.BookStore.BookStore.Repositiories;

import com.example.BookStore.BookStore.Entities.BookEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByName(String bookName);

    Optional<BookEntity> findByIsbn(String isbn);

    @Transactional
    void deleteByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
