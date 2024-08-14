package com.example.demo.mapper;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class MapperBook {
    public BookDTO toDto(final Book book) {
        final BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setAmount(book.getAmount());
        dto.setBorrowed(book.isBorrowed());
        return dto;
    }

    public Book toEntity(final BookDTO dto) {
        if (dto.getTitle() == null || dto.getAuthor() == null) {
            throw new IllegalArgumentException("Title and Author cannot be null");
        }
        final Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setAmount(dto.getAmount());
        book.setBorrowed(dto.isBorrowed());
        return book;
    }
}
