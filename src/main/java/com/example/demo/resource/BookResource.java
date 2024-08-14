package com.example.demo.resource;


import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BorrowedBookDTO;
import com.example.demo.service.BookService;
import com.example.demo.service.impl.BookServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book")
public class BookResource {
    @Autowired
    private BookService bookService = new BookServiceImpl();

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody final BookDTO bookDTO) {
        System.out.println("Validating BookDTO: " + bookDTO);
        bookService.create(bookDTO);
        return ResponseEntity.ok("Book created successfully");
    }

    @GetMapping("/{id}")
    public BookDTO read(final @PathVariable Long id) {
        return bookService.read(id);
    }

    @PutMapping("/update")
    public ResponseEntity<BookDTO> update(@RequestBody BookDTO bookDTO) {
        bookService.update(bookDTO);
        return ResponseEntity.ok(bookDTO);

    }

    @DeleteMapping("/{id}")
    public String delete(final @PathVariable("id") Long id) {
        BookDTO bookDTO = bookService.read(id);
        bookService.delete(id);
        return "Book:" + bookDTO.toString();
    }

    @GetMapping(value = "/all")
    public List<BookDTO> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/borrowed-books/distinct-titles")
    public ResponseEntity<List<String>> getDistinctBorrowedBookTitles() {
        List<String> titles = bookService.getDistinctBorrowedBookTitles();
        return ResponseEntity.ok(titles);
    }

    @GetMapping("/borrowed-books/distinct-titles-count")
    public ResponseEntity<List<BorrowedBookDTO>> getDistinctBorrowedBookTitlesAndCount() {
        List<BorrowedBookDTO> titlesAndCount = bookService.getDistinctBorrowedBookTitlesAndCount();
        return ResponseEntity.ok(titlesAndCount);
    }
}
