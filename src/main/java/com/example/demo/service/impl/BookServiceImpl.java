package com.example.demo.service.impl;


import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import com.example.demo.mapper.MapperBook;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MapperBook mapperBook = new MapperBook();

    @Override
    public BookDTO create(BookDTO bookDTO) {
        Optional<Book> existingBookOptional = bookRepository.findByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor());
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            existingBook.setAmount(existingBook.getAmount() + bookDTO.getAmount());
            bookRepository.save(existingBook);
            return mapperBook.toDto(existingBook);
        } else {
            Book newBook = mapperBook.toEntity(bookDTO);
            bookRepository.save(newBook);
            return mapperBook.toDto(newBook);
        }
    }

    @Override
    public BookDTO read(Long id) {
        return mapperBook.toDto(bookRepository.findById(id).orElseThrow());
    }

    @Override
    public void update(BookDTO dto) {
        bookRepository.save(mapperBook.toEntity(dto));
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.isBorrowed()) {
            throw new IllegalStateException("Cannot delete a borrowed book");
        }
        bookRepository.delete(book);
    }

    @Override
    public List<BookDTO> getAll() {
        return bookRepository.findAll().stream().map(mapperBook::toDto).collect(Collectors.toList());
    }
}
