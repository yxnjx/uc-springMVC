package com.example.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {
    @GetMapping("/hi") //요청 주소: localhost:8080/hi
    public String niceToMeetYou(Model model) { //사용자가 요청하면 자동으로 받음
        model.addAttribute("username", "홍길동"); //model을 통해서 변수 이름을 넘겨줌
        return "greetings"; //View: greetings.mustache(View로 동작)
    }

    @GetMapping("/bye") //요청 주소: localhost:8080/bye
    public String bye(Model model) { //사용자가 요청하면 자동으로 받음
        model.addAttribute("username", "홍길동");
        return "goodbye"; //View: goodbye.mustache
    }
}
