package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /* This Repository use access to data base with User entity
     * By with the help of this we fetch the data from the data base of User entity
     * this repository is stablish connection between the database database and entity(User Class)
     *  */
}
