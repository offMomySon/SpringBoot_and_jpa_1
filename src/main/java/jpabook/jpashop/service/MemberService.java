package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// class 에 Transactional 을 셋팅하면, 모든 public 메서드에 Transactional 이 셋팅된다.
@Service
@Transactional(readOnly = true)
//@AllArgsConstructor    // 모든 field 에 대해 생성자를 만들어 준다.
@RequiredArgsConstructor // final 이 있는 field 만 가지고 생성자를 만들어 준다.
public class MemberService {

    // 변경할 일이 없기 때문에 final 을 권장.
    private final MemberRepository memberRepository;

    // setter injection
    // 왜 사용?
    // 1. test code 를 작성할 때, Mock 를 주입해줄 수 있다. / field 는 주입하기가 어렵다.
    // 단점.
    // 1. 사실 app 이 실행되고 memberRepository 가 셋팅된다음, 변경될 일이 없다.
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 생성자 injection ,
    // 생성자가 하나만 있는 경우, Autowired 를 쓰지않아도 자동으로 injection 시켜준다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입.
     */
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회.
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 한건조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
