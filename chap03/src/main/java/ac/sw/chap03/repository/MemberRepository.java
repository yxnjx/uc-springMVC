package ac.sw.chap03.repository;

import ac.sw.chap03.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface MemberRepository extends CrudRepository<Member, Long> { //pk => uid(타입 = Long)
    //CrudRepository<T, ID> //T - Entity 타입의 클래스 , ID - pk 값의 타입
    //CrudRepository는 관리되는 엔티티 클래스에 대해 정교한 CRUD 기능 제공(insert, update, delete, select)
    @Override
    ArrayList<Member> findAll();
}
