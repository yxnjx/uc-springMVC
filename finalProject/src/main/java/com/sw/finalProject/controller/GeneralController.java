package com.sw.finalProject.controller;

import com.sw.finalProject.dto.CommentDto;
import com.sw.finalProject.dto.MemberForm;
import com.sw.finalProject.dto.SaleForm;
import com.sw.finalProject.entity.Comment;
import com.sw.finalProject.entity.Member;
import com.sw.finalProject.entity.Sale;
import com.sw.finalProject.repository.CommentRepository;
import com.sw.finalProject.repository.MemberRepository;
import com.sw.finalProject.repository.SaleRepository;
import com.sw.finalProject.service.CommentService;
import com.sw.finalProject.service.UploadFileService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class GeneralController {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        loginCheck(session, model);
        return "index";
    }

    @GetMapping("/sales/new")
    public String newForm(HttpSession session, Model model) {
        loginCheck(session, model);

        return "sale/newImage";
    }

    @GetMapping("/sales")
    public String getAllSales(HttpSession session, Model model) {
        //DB에 있는 모든 물건 목록 가져오기
        List<Sale> allEntityList = (List<Sale>)saleRepository.findAll();

        //Model 에 담기
        model.addAttribute("list", allEntityList); //allEntityList라는 이름으로 model에 담김

        loginCheck(session, model);
        return "sale/saleList";
    }

    @GetMapping("/sales/{id}")
    public String getOne(@PathVariable Long id, Model model, HttpSession session) {
        Sale sale = saleRepository.findById(id).orElse(null);
        model.addAttribute("sale", sale);

        //관련댓글(saleId)에 대한 댓글
        List<CommentDto> listDto = commentService.comments(id); //모든 댓글 복사하는 명령
        model.addAttribute("commentDtos", listDto);

        loginCheck(session, model);
        return "sale/showDetail";
    }

    @GetMapping("/sales/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        Sale sale = saleRepository.findById(id).orElse(null);
        model.addAttribute("sale", sale);
        loginCheck(session, model);
        return "sale/edit";
    }

    @PostMapping("/sales/realUpdate")
    public String realUpdate(SaleForm dto, @RequestParam("file") MultipartFile file) {
//        Sale entitySale = dto.toEntity();
//        Sale existingSale = saleRepository.findById(entitySale.getId()).orElse(null);
//
//        if(existingSale == null) {
//            return "redirect:/sales";
//        }
//        existingSale.setTitle(entitySale.getTitle());
//        existingSale.setPrice(entitySale.getPrice());
//        existingSale.setContent(entitySale.getContent());
//        saleRepository.save(existingSale);

        // 이미지 업로드 처리
        String savedFileName = uploadFileService.upload(file);

        // 이미지 업로드 성공 시, Sale 엔티티 업데이트
        if (savedFileName != null) {
            Sale sale = saleRepository.findById(dto.getId()).orElse(null);
            if (sale != null) {
                sale.setTitle(dto.getTitle());
                sale.setPrice(dto.getPrice());
                sale.setContent(dto.getContent());
                sale.setFilename(savedFileName);
                saleRepository.save(sale);
            }
        }

        return "redirect:/sales/" + dto.getId();
    }

    @GetMapping("/sales/delete/{id}")
    public String deleteSale(@PathVariable Long id) {
        //연관된 comment 삭제(이 작업 안 하면 댓글이 있을 경우 글 삭제 X)
        commentRepository.deleteBySaleId(id);
        //그 후 sale 행 삭제
        saleRepository.deleteById(id);
        return "redirect:/sales";
    }

    //******************MemberController******************//

    @GetMapping("/members/signUpForm")
    public String newMemberForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/signUpForm";
    }

    @GetMapping("/members/loginForm")
    public String signUpPage() {
        return "members/loginForm";
    }

    @PostMapping("/members/loginProcess")
    public String loginOk(MemberForm dto, HttpSession session, Model model) {
        Member logined = memberRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());

        if(logined != null) { //회원이 있을 때
            session.setAttribute("email", logined.getEmail()); //session에 값 등록
            loginCheck(session, model);
            return "index";
        } else {
            model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "members/loginForm";
        }
    }

    @PostMapping("/members/join")
    public String join(MemberForm dto, Model model) {
        if(dto.getName().isEmpty() || dto.getEmail().isEmpty() || dto.getPassword().isEmpty()) {
            model.addAttribute("error", "이름, 이메일, 비밀번호 모두 입력해주세요.");
            return "members/signUpForm";
        }

        Member member = dto.toEntity();
        Member saved = memberRepository.save(member);
        return "redirect:/members/" + saved.getId();
    }

    @GetMapping("/members")
    public String index(HttpSession session ,Model model) {
        Iterable<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);

        loginCheck(session, model);

        return "members/index";
    }


    @GetMapping("/members/{id}")
    public String show(@PathVariable Long id, Model model, HttpSession session) {
        Member member = memberRepository.findById(id).orElse(null);
        model.addAttribute("member", member);

        loginCheck(session, model);
        return "members/show";
    }

    @GetMapping("/members/{id}/edit")
    public String edit(@PathVariable Long id, Model model, HttpSession session) {
        Member memberEntity = memberRepository.findById(id).orElse(null);
        model.addAttribute("member", memberEntity);

        loginCheck(session, model);
        return "members/edit";
    }

    @PostMapping("/members/update")
    public String update(MemberForm dto) {
        log.info(dto.toString());
        Member memberEntity = dto.toEntity();
        Member target = memberRepository.findById(memberEntity.getId()).orElse(null);
        if (target != null) {
            target.setEmail(memberEntity.getEmail());
            target.setPassword(memberEntity.getPassword());
            memberRepository.save(memberEntity);
        }
        return "redirect:/members/" + memberEntity.getId();
    }

    @GetMapping("/members/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다.");

        // 1. 삭제 대상을 가져옴
        Member target = memberRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 2. 대상 삭제
        if (target != null) {
            memberRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다.");

            // 세션 무효화
            session.invalidate();
        }

        // 3. 결과 페이지로 리다이렉트
        return "redirect:/members";
    }

    //로그아웃
    @GetMapping("/members/logout")
    public String logout(HttpSession session) {
        session.invalidate(); //session 값 제거
        return "index";
    }

    //**********글 업로드**********//

    @PostMapping("/sales/uploadImage")
    public String uploadImage(SaleForm dto, @RequestParam("file")MultipartFile file, Model model, HttpSession session) {
        // SAVE FILE
        String savedFileName = uploadFileService.upload(file); //저장된 파일 이름을 savedFileName에 넘겨줌

        if (savedFileName != null) { //정상적으로 저장이 됐을 경우
            Sale sale = dto.toEntity(); //dto -> Entity
            sale.setFilename(savedFileName); //filename -> savedFileName 세팅
            saleRepository.save(sale); //db 저장
        } else {
            log.error("파일 저장 실패");
            System.out.println("파일저장실패");
        }

        loginCheck(session, model);
        return "redirect:/sales";
    }

    //**********로그인시 메뉴바 유지**********//

    public void loginCheck(HttpSession session, Model model) {
        //로그인 성공 검사 및 header.mustache에 전달
        String email = (String)session.getAttribute("email"); //session 값을 읽어내서 email 값이 있는지 없는지 검사
        if(email != null) { //이미 로그인한 사람
            model.addAttribute("email", email);
        }
    }

    //**********댓글 저장(생성)**********//
    @PostMapping("/sales/comment")
    public String saveComment(CommentDto dto) { //form으로 받아온 값을 dto에 담음
        Comment saved = commentService.create(dto.getSaleId(), dto); //commentService.create - 댓글 저장

        return "redirect:/sales/" + dto.getSaleId(); //댓글 저장(생성) 후 redirect해서 댓글 작성한 글을 리턴해줌
    }
}
