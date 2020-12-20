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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // "memberForm" 에 비어있는 MemberForm 객체를 넣어준다.
    // 이유는 validation 같은 것을 해주기 때문에,
    // members/createMemberForm 이 렌더린 될때 MemberForm object 를 참고해서 렌더링함.
    // 렌더링 페이지에서 MemberForm object 의 field 를 보고 렌더링함.
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

    // 화면에 맞는 api 들은 form 객체나 DTO 를 사용해야함. Entity 로 ahemsrp 1:1 로 매핑될 수 없다.
    // Entity 는 최대한 순수하게 유지되어야 한다. // 핵심 비지니스 로직이 틀어지거나, 화면이 깨지거나 할 수 있다.
    // 지금은 단순해서 Member entity 를 사용함.
    // template 엔진인 서버안에서 렌더린되어서 괜찮은데, api 를 만들때는 이유를 불문하고 Entity 를 반환하면 안됨.
    // 왜? - api 는 스펙이고 member entity 에 field 를 추가하게 되면 api 의 스펙이 변해버린다.
    // 아래를 개선하자면 DTO 로 변환해서 전달해야한다.
    @GetMapping("/members")
    public String list(Model model){
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}
