package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createFrom(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }


    // 1. cotroller 에서 원하는 validation 과 Entity 에서 원하는 validation 이 다를수 있다.
    // 그렇기 때문에 MemberForm, Member 를 따로 만드는것이 좋다.

    // 2. BindingResult 가 셋팅되어있으면,
    // MemberForm 에서 exception 이 발생해도  BindingResult 에 결과값이 담기고 로직은 그대로 실행된다.
    // spring 이 BindingResult 에 exception 을 받아서 화면까지 끌고 가준다 -> 어떤 에러인지 다 뿌려준다. // spring 과 timeleaf 가 잘 통합되어있기 때문에.
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/creatememberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }
}
