package com.example.demo.MemberTests;

import com.example.demo.dto.MemberDTO;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MemberServiceImplIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testCreateMember() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("John Smith");
        memberDTO.setMembershipDate(LocalDate.now());
        memberDTO.setBookBorrowed(false);

        MemberDTO savedMember = memberService.create(memberDTO);

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getId()).isGreaterThan(0);
    }

    @Test
    void testReadMember() {
        Long memberId = 14L; // Ідентифікатор існуючого члена
        MemberDTO memberDTO = memberService.read(memberId);

        assertThat(memberDTO).isNotNull();
        assertThat(memberDTO.getName()).isEqualTo("John Smith");
    }

    @Test
    void testUpdateMember() {
        Long memberId = 11L;
        MemberDTO memberDTO = memberService.read(memberId);

        memberDTO.setName("Jane Smith");
        memberService.update(memberDTO);

        MemberDTO updatedMember = memberService.read(memberId);
        assertThat(updatedMember.getName()).isEqualTo("Jane Smith");
    }

    @Test
    void testDeleteMember() {
        Long memberId = 14L;
        memberService.delete(memberId);

        assertThrows(RuntimeException.class, () -> memberService.read(memberId));
    }
}
