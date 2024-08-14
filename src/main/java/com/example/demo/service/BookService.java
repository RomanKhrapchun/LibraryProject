package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BorrowedBookDTO;

import java.util.List;

public interface BookService {
    BookDTO create(BookDTO bookDTO);

    BookDTO read(Long id);

    void update(BookDTO dto);

    void delete(Long id);

    List<BookDTO> getAll();

    List<String> getDistinctBorrowedBookTitles();

    List<BorrowedBookDTO> getDistinctBorrowedBookTitlesAndCount();

}
