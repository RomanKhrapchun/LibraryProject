package com.example.demo.service;

import com.example.demo.dto.MemberDTO;

import java.util.List;

public interface MemberService {
    MemberDTO create(MemberDTO bookDTO);

    MemberDTO read(Long id);

    void update(MemberDTO dto);

    void delete(Long id);

    List<MemberDTO> getAll();
}
