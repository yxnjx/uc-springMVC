package com.sw.finalProject.repository;

import com.sw.finalProject.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
    @Query(value = "Select * from member where email=:email and password=:password", nativeQuery = true)
    Member findByEmailAndPassword(String email, String password);

    //이메일로 회원 찾기
    Member findByEmail(String email);
}
