package com.sw.chap11.repository;

import com.sw.chap11.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
    @Query(name = "select * from member where email=:email and password=:password", nativeQuery = true)
    Member findByEmailAndPassword(String email, String password);
}
