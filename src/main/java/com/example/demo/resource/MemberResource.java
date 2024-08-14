package com.example.demo.resource;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.MemberDTO;
import com.example.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/member")
public class MemberResource {
    @Autowired
    private MemberService memberService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody final MemberDTO memberDTO) {
        memberService.create(memberDTO);
        return ResponseEntity.ok("Member created successfully");
    }

    @GetMapping("/{id}")
    public MemberDTO read(final @PathVariable Long id) {
        return memberService.read(id);
    }

    @PutMapping("/update")
    public ResponseEntity<MemberDTO> update(@Valid @RequestBody MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return ResponseEntity.ok(memberDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(final @PathVariable("id") Long id) {
        memberService.delete(id);
        return ResponseEntity.ok("Member deleted successfully");
    }

    @GetMapping(value = "/all")
    public List<MemberDTO> getAll() {
        return memberService.getAll();
    }

    @PostMapping("/{memberId}/borrow/{bookId}/{quantity}")
    public ResponseEntity<String> borrowBook(@PathVariable Long memberId, @PathVariable Long bookId, @PathVariable int quantity) {
        memberService.borrowBook(memberId, bookId, quantity);
        return ResponseEntity.ok("Book(s) borrowed successfully");
    }

    @PostMapping("/{memberId}/return/{bookId}/{quantity}")
    public ResponseEntity<String> returnBook(
            @PathVariable Long memberId,
            @PathVariable Long bookId,
            @PathVariable int quantity) {
        memberService.returnBook(memberId, bookId, quantity);
        return ResponseEntity.ok("Returned " + quantity + " book(s) successfully");
    }

    @GetMapping("/borrowed-books/{name}")
    public ResponseEntity<List<BookDTO>> getBorrowedBooksByMemberName(@PathVariable String name) {
        List<BookDTO> borrowedBooks = memberService.getBorrowedBooksByMemberName(name);
        return ResponseEntity.ok(borrowedBooks);
    }
}
