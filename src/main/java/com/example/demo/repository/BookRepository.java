package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /* This Repository use access to data base with Book entity
     * By with the help of this we fetch the data from the data base of Book entity
     * this repository is stablish connection between the database database and entity(Book Class)
     *  */

    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.author = :author")
    Optional<Book> findByTitleAndAuthor(String title, String author);

}
