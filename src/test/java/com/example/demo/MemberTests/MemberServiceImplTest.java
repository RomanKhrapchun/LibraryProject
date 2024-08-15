package com.example.demo.MemberTests;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.MemberDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Member;
import com.example.demo.entity.MemberBook;
import com.example.demo.mapper.MapperBook;
import com.example.demo.mapper.MapperMember;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MapperMember mapperMember;

    @Mock
    private MapperBook mapperBook;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        // Arrange
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("John Doe");

        Member memberEntity = new Member();
        memberEntity.setName("John Doe");

        when(mapperMember.toEntity(memberDTO)).thenReturn(memberEntity);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member savedMember = invocation.getArgument(0);
            savedMember.setId(1L); // Simulate generated ID
            savedMember.setMembershipDate(LocalDate.now());
            return savedMember;
        });
        when(mapperMember.toDto(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            MemberDTO dto = new MemberDTO();
            dto.setId(member.getId());
            dto.setName(member.getName());
            dto.setMembershipDate(member.getMembershipDate());
            return dto;
        });

        // Act
        MemberDTO result = memberService.create(memberDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals(LocalDate.now(), result.getMembershipDate());
        verify(memberRepository, times(1)).save(memberEntity);
    }

    @Test
    void testRead() {
        // Arrange
        Long memberId = 1L;
        Member memberEntity = new Member();
        memberEntity.setId(memberId);
        memberEntity.setName("Jane Doe");
        memberEntity.setMembershipDate(LocalDate.of(2023, 1, 1));

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberId);
        memberDTO.setName("Jane Doe");
        memberDTO.setMembershipDate(LocalDate.of(2023, 1, 1));

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberEntity));
        when(mapperMember.toDto(memberEntity)).thenReturn(memberDTO);

        // Act
        MemberDTO result = memberService.read(memberId);

        // Assert
        assertNotNull(result);
        assertEquals(memberId, result.getId());
        assertEquals("Jane Doe", result.getName());
        assertEquals(LocalDate.of(2023, 1, 1), result.getMembershipDate());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void testUpdate() {
        // Arrange
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(1L);
        memberDTO.setName("Updated Name");
        memberDTO.setMembershipDate(LocalDate.of(2023, 5, 15));

        Member memberEntity = new Member();
        memberEntity.setId(1L);
        memberEntity.setName("Original Name");
        memberEntity.setMembershipDate(LocalDate.of(2023, 1, 1));

        when(mapperMember.toEntity(memberDTO)).thenReturn(memberEntity);
        when(memberRepository.save(memberEntity)).thenReturn(memberEntity);

        // Act
        memberService.update(memberDTO);

        // Assert
        verify(memberRepository, times(1)).save(memberEntity);
    }

    @Test
    void testDelete_Success() {
        // Arrange
        Long memberId = 1L;
        Member memberEntity = new Member();
        memberEntity.setId(memberId);
        memberEntity.setBorrowedBooks(Arrays.asList()); // No borrowed books

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberEntity));
        doNothing().when(memberRepository).delete(memberEntity);

        // Act
        memberService.delete(memberId);

        // Assert
        verify(memberRepository, times(1)).delete(memberEntity);
    }

    @Test
    void testDelete_Failure_BorrowedBooksExist() {
        // Arrange
        Long memberId = 1L;
        Member memberEntity = new Member();
        memberEntity.setId(memberId);
        memberEntity.setBorrowedBooks(Arrays.asList(mock(MemberBook.class))); // Has borrowed books

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberEntity));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            memberService.delete(memberId);
        });

        assertEquals("Cannot delete a member with borrowed books", exception.getMessage());
        verify(memberRepository, times(0)).delete(memberEntity);
    }

    @Test
    void testGetAll() {
        // Arrange
        Member member1 = new Member();
        member1.setId(1L);
        member1.setName("Member One");
        member1.setMembershipDate(LocalDate.of(2023, 1, 1));

        Member member2 = new Member();
        member2.setId(2L);
        member2.setName("Member Two");
        member2.setMembershipDate(LocalDate.of(2023, 2, 1));

        MemberDTO memberDTO1 = new MemberDTO();
        memberDTO1.setId(1L);
        memberDTO1.setName("Member One");
        memberDTO1.setMembershipDate(LocalDate.of(2023, 1, 1));

        MemberDTO memberDTO2 = new MemberDTO();
        memberDTO2.setId(2L);
        memberDTO2.setName("Member Two");
        memberDTO2.setMembershipDate(LocalDate.of(2023, 2, 1));

        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2));
        when(mapperMember.toDto(member1)).thenReturn(memberDTO1);
        when(mapperMember.toDto(member2)).thenReturn(memberDTO2);

        // Act
        List<MemberDTO> result = memberService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(memberDTO1));
        assertTrue(result.contains(memberDTO2));
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testBorrowBook() {
        // Arrange
        Member member = new Member();
        MemberBook existingBorrow = new MemberBook();
        existingBorrow.setQuantity(5);  // Assume the member already borrowed 5 books

        member.getBorrowedBooks().add(existingBorrow);

        Book book = new Book();
        book.setAmount(5);
        book.setInitialAmount(5);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> memberService.borrowBook(1L, 1L, 6),  // Try to borrow 6 books, exceeding limit
                "Expected borrowBook to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Borrow limit exceeded"));
        verify(bookRepository, times(0)).save(book);
        verify(memberRepository, times(0)).save(member);
    }


    @Test
    void testReturnBook() {
        Member member = new Member();
        Book book = new Book();
        book.setAmount(3);
        book.setInitialAmount(5);

        MemberBook memberBook = new MemberBook();
        memberBook.setBook(book);
        memberBook.setQuantity(2);
        member.getBorrowedBooks().add(memberBook);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        memberService.returnBook(1L, 1L, 2);

        assertEquals(5, book.getAmount());
        assertFalse(book.isBorrowed());
        verify(bookRepository, times(1)).save(book);
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void testGetBorrowedBooksByMemberName() {
        Member member = new Member();
        MemberBook memberBook = new MemberBook();
        Book book = new Book();
        book.setTitle("The Alchemist");
        memberBook.setBook(book);
        memberBook.setQuantity(2);
        member.getBorrowedBooks().add(memberBook);

        when(memberRepository.findByName("John Doe")).thenReturn(Optional.of(member));
        when(mapperBook.toDto(book)).thenReturn(new BookDTO());

        List<BookDTO> result = memberService.getBorrowedBooksByMemberName("John Doe");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findByName("John Doe");
    }
}
