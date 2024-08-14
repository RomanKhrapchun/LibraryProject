package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.MemberDTO;

import java.util.List;

public interface MemberService {
    MemberDTO create(MemberDTO bookDTO);

    MemberDTO read(Long id);

    void update(MemberDTO dto);

    void delete(Long id);

    List<MemberDTO> getAll();

    void borrowBook(Long memberId, Long bookId, int quantity);

    void returnBook(Long memberId, Long bookId, int quantity);

    List<BookDTO> getBorrowedBooksByMemberName(String name);
}
