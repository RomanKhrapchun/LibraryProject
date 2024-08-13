package com.example.demo.BookTests;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.mapper.MapperBook;
import com.example.demo.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MapperBook mapperBook;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteBookWhenNotBorrowed() {
        Book book = new Book();
        book.setId(1L);
        book.setBorrowed(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.delete(1L);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testDeleteBookWhenBorrowed() {
        Book book = new Book();
        book.setId(1L);
        book.setBorrowed(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertThrows(IllegalStateException.class, () -> bookService.delete(1L));
        verify(bookRepository, never()).delete(any(Book.class));
    }
}
