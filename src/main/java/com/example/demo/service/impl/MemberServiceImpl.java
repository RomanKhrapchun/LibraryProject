package com.example.demo.service.impl;

import com.example.demo.dto.MemberDTO;
import com.example.demo.entity.Member;
import com.example.demo.mapper.MapperMember;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MapperMember mapperMember = new MapperMember();

    @Override
    public MemberDTO create(MemberDTO productDTO) {
        Member member = mapperMember.toEntity(productDTO);
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
        memberRepository.delete(memberRepository.findById(id).orElseThrow());
    }

    @Override
    public List<MemberDTO> getAll() {
        return memberRepository.findAll().stream().map(mapperMember::toDto).collect(Collectors.toList());
    }
}
