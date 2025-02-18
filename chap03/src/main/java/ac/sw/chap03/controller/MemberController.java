package ac.sw.chap03.controller;

import ac.sw.chap03.dto.MemberForm;
import ac.sw.chap03.entity.Member;
import ac.sw.chap03.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j //log 사용 가능
@Controller //컨트롤러 역할
public class MemberController {
    @Autowired
    MemberRepository memberRepository;

    //회원 등록 폼
    @GetMapping("/members/new") //localhost:8088 + /members/new
    public String newMember() {
        return "member/newMember"; //newMember.mustache
    }

    @PostMapping("/members/create") //localhost:8088 + /members/create
    public String newMem(MemberForm dto, Model model) {
        System.out.println("========== DTO ==========");
        //System.out.println("아이디 : " + dto.getUid());
        System.out.println("비밀번호 : " + dto.getPwd());
        System.out.println("전화번호 : " + dto.getTel());
        System.out.println("나이 : " + dto.getAge());
        log.info("========== DTO ==========");
        log.info(dto.toString());

        //DTO(MemberForm 식판) => 엔티티(Member)
        Member entity = dto.toEntity(); //dto 안에 toEntity라는 내용이 있음.

        //h2(수행할 때 생겼다가 종료하면 데이터 없어짐) 데이터베이스에 저장
        memberRepository.save(entity);
        model.addAttribute("userid", "aaa");

        return "index"; //index.mustache
    }

    //member 전체 리스트 가져오기
    @GetMapping("/members")
    public String getAllMembers(Model model) {
        //DB에 있는 모든 member 가져오기
        List<Member> allEntityList = memberRepository.findAll();

        //Model에 담기
        model.addAttribute("memList", allEntityList);

        return "member/memberList";
    }
}
