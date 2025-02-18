package com.sw.chap11.controller;

import com.sw.chap11.entity.Member;
import com.sw.chap11.dto.MemberForm;
import com.sw.chap11.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @GetMapping("/members/registerForm")
    public String newMemberForm() {
        return "members/new";
    }

    @GetMapping("/members/loginForm")
    public String signUpPage() {
        return "members/loginForm";
    }

    @PostMapping("/members/loginProcess")
    public String loginOk(MemberForm dto, HttpSession session, Model model) {
        Member logined = memberRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword()); //Member 테이블에서 이메일, 비번이 있는지 검사

        if(logined != null) { //회원이 있을 때
            session.setAttribute("email", logined.getEmail()); //session에 값 등록
            model.addAttribute("email", logined.getEmail());
        }
        return "index";
    }

    @PostMapping("/members/join")
    public String join(MemberForm memberForm) {
        log.info(memberForm.toString());
        Member member = memberForm.toEntity();
        log.info(member.toString());
        Member saved = memberRepository.save(member);
        log.info(saved.toString());
        return "redirect:/members/" + saved.getId();
    }

    @GetMapping("/members/{id}")
    public String show(@PathVariable Long id, Model model) {
        Member member = memberRepository.findById(id).orElse(null);
        model.addAttribute("member", member);
        return "members/show";
    }

    @GetMapping("/members")
    public String index(Model model) {
        Iterable<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "members/index";
    }

    @GetMapping("/members/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Member memberEntity = memberRepository.findById(id).orElse(null);
        model.addAttribute("member", memberEntity);
        return "members/edit";
    }

    @PostMapping("/members/update")
    public String update(MemberForm form) {
        log.info(form.toString());
        Member memberEntity = form.toEntity();
        Member target = memberRepository.findById(memberEntity.getId()).orElse(null);
        if (target != null) {
            memberRepository.save(memberEntity);
        }
        return "redirect:/members/" + memberEntity.getId();
    }

    @GetMapping("/members/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다!!");
        // 1. 삭제 대상을 가져옴
        Member target = memberRepository.findById(id).orElse(null);
        log.info(target.toString());
        // 2. 대상 삭제
        if (target != null) {
            memberRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다.");
        }
        // 3. 결과 페이지로 리다이렉트
        return "redirect:/members";
    }

    //logout 처리
    @GetMapping("/members/logout")
    public String logout(HttpSession session) {
        session.invalidate(); //session 값을 제거
        return "index";
    }
}



















