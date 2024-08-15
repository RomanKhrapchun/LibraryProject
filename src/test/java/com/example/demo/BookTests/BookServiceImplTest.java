package com.example.demo.BookTests;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BorrowedBookDTO;
import com.example.demo.entity.Book;
import com.example.demo.mapper.MapperBook;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private MapperBook mapperBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_NewBook() {
        BookDTO bookDTO = new BookDTO();
        Book book = new Book();
        when(bookRepository.findByTitleAndAuthor(anyString(), anyString())).thenReturn(Optional.empty());
        when(mapperBook.toEntity(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(mapperBook.toDto(book)).thenReturn(bookDTO);

        BookDTO result = bookService.create(bookDTO);

        assertNotNull(result);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testCreate_ExistingBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setAmount(5);

        Book existingBook = new Book();
        existingBook.setTitle("Test Book");
        existingBook.setAuthor("Test Author");
        existingBook.setAmount(10);

        when(bookRepository.findByTitleAndAuthor("Test Book", "Test Author"))
                .thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        BookDTO expectedBookDTO = new BookDTO();
        expectedBookDTO.setTitle("Test Book");
        expectedBookDTO.setAuthor("Test Author");
        expectedBookDTO.setAmount(15);
        when(mapperBook.toDto(existingBook)).thenReturn(expectedBookDTO);

        BookDTO result = bookService.create(bookDTO);

        assertNotNull(result, "Result should not be null");
        assertEquals(15, result.getAmount(), "Book amount should be updated to 15");
        verify(bookRepository, times(1)).save(existingBook);
    }


    @Test
    void testRead() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(mapperBook.toDto(book)).thenReturn(bookDTO);

        BookDTO result = bookService.read(1L);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate() {
        BookDTO bookDTO = new BookDTO();
        Book book = new Book();
        when(mapperBook.toEntity(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        bookService.update(bookDTO);

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDelete() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        book.setBorrowed(false);

        bookService.delete(1L);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testGetAll() {
        Book book1 = new Book();
        Book book2 = new Book();
        BookDTO bookDTO1 = new BookDTO();
        BookDTO bookDTO2 = new BookDTO();
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));
        when(mapperBook.toDto(book1)).thenReturn(bookDTO1);
        when(mapperBook.toDto(book2)).thenReturn(bookDTO2);

        List<BookDTO> result = bookService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetDistinctBorrowedBookTitles() {
        List<String> mockTitles = Arrays.asList("The Alchemist", "1984");
        when(bookRepository.findDistinctBorrowedBookTitles()).thenReturn(mockTitles);

        List<String> result = bookService.getDistinctBorrowedBookTitles();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("The Alchemist"));
        assertTrue(result.contains("1984"));
    }

    @Test
    void testGetDistinctBorrowedBookTitlesAndCount() {
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"The Alchemist", 10L},
                new Object[]{"1984", 5L}
        );
        when(bookRepository.findDistinctBorrowedBookTitlesAndCount()).thenReturn(mockResults);

        List<BorrowedBookDTO> result = bookService.getDistinctBorrowedBookTitlesAndCount();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("The Alchemist", result.get(0).getTitle());
        assertEquals(10L, result.get(0).getTotalBorrowed());
        assertEquals("1984", result.get(1).getTitle());
        assertEquals(5L, result.get(1).getTotalBorrowed());
    }
}
