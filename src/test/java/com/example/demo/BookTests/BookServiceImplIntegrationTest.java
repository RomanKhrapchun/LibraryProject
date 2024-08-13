package com.example.demo.BookTests;

import com.example.demo.dto.BookDTO;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Rollback(false)
class BookServiceImplIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testDeleteBookWhenBorrowedIntegration() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setAmount(1);
        bookDTO.setBorrowed(true);
        BookDTO savedBook = bookService.create(bookDTO);
        assertThrows(IllegalStateException.class, () -> bookService.delete(savedBook.getId()));
    }

    @Test
    void testDeleteBookWhenNotBorrowedIntegration() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book 2");
        bookDTO.setAuthor("Test Author 2");
        bookDTO.setAmount(1);
        bookDTO.setBorrowed(false);
        BookDTO savedBook = bookService.create(bookDTO);
        bookService.delete(savedBook.getId());
        assertThrows(RuntimeException.class, () -> bookService.read(savedBook.getId()));
    }
}

