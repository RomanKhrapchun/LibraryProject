package com.example.demo.mapper;

import com.example.demo.dto.MemberDTO;
import com.example.demo.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MapperMember {
    public MemberDTO toDto(final Member member) {
        final MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setName(member.getName());
        dto.setMembershipDate(member.getMembershipDate());
        dto.setBookBorrowed(member.isBookBorrowed());
        return dto;
    }

    public Member toEntity(final MemberDTO dto) {
        final Member member = new Member();
        member.setId(dto.getId());
        member.setId(dto.getId());
        member.setName(dto.getName());
        member.setMembershipDate(dto.getMembershipDate());
        member.setBookBorrowed(dto.isBookBorrowed());
        return member;
    }
}
