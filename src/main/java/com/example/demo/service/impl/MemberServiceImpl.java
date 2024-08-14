package com.example.demo.service.impl;

import com.example.demo.dto.MemberDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Member;
import com.example.demo.entity.MemberBook;
import com.example.demo.mapper.MapperMember;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    @Value("${member.borrow.limit}")
    private int borrowLimit;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MapperMember mapperMember;

    @Override
    public MemberDTO create(MemberDTO memberDTO) {
        Member member = mapperMember.toEntity(memberDTO);
        member.setMembershipDate(LocalDate.now());
        memberRepository.save(member);
        return mapperMember.toDto(member);
    }

    @Override
    public MemberDTO read(Long id) {
        return mapperMember.toDto(memberRepository.findById(id).orElseThrow());
    }

    @Override
    public void update(MemberDTO dto) {
        memberRepository.save(mapperMember.toEntity(dto));
    }

    @Override
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (!member.getBorrowedBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete a member with borrowed books");
        }

        memberRepository.delete(member);
    }

    @Override
    public List<MemberDTO> getAll() {
        return memberRepository.findAll().stream().map(mapperMember::toDto).collect(Collectors.toList());
    }

    @Override
    public void borrowBook(Long memberId, Long bookId, int quantity) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        int currentBorrowedBooksCount = member.getBorrowedBooks().stream()
                .mapToInt(MemberBook::getQuantity).sum();

        if (currentBorrowedBooksCount + quantity > borrowLimit) {
            throw new IllegalStateException("Borrow limit exceeded. You can only borrow " + (borrowLimit - currentBorrowedBooksCount) + " more book(s).");
        }

        if (book.getAmount() < quantity) {
            throw new IllegalStateException("Not enough copies available to borrow");
        }

        // Check if the member has already borrowed this book
        Optional<MemberBook> existingBorrow = member.getBorrowedBooks().stream()
                .filter(mb -> mb.getBook().equals(book))
                .findFirst();

        if (existingBorrow.isPresent()) {
            // Update the existing record
            MemberBook memberBook = existingBorrow.get();
            memberBook.setQuantity(memberBook.getQuantity() + quantity);
        } else {
            // Create a new borrow record
            MemberBook memberBook = new MemberBook();
            memberBook.setMember(member);
            memberBook.setBook(book);
            memberBook.setQuantity(quantity);
            member.getBorrowedBooks().add(memberBook);
        }

        book.setAmount(book.getAmount() - quantity);

        memberRepository.save(member);
        bookRepository.save(book);
    }


    @Override
    public void returnBook(Long memberId, Long bookId, int quantity) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        MemberBook memberBook = member.getBorrowedBooks().stream()
                .filter(mb -> mb.getBook().equals(book))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("This member has not borrowed this book"));

        if (memberBook.getQuantity() < quantity) {
            throw new IllegalStateException("Cannot return more books than borrowed");
        }

        memberBook.setQuantity(memberBook.getQuantity() - quantity);
        book.setAmount(book.getAmount() + quantity);

        if (memberBook.getQuantity() == 0) {
            member.getBorrowedBooks().remove(memberBook);
        }

        bookRepository.save(book);
        memberRepository.save(member);
    }


}
