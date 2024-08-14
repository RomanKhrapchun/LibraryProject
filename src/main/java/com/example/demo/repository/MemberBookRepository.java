package com.example.demo.repository;

import com.example.demo.entity.MemberBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {
    // Additional query methods if needed
}
