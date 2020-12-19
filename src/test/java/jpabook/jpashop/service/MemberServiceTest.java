package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
// test 에 선언된 Transactional 의 경우, 모든 transaction 을 rollback 시킨다.
// 당연히, service , repository 는 rollback 하지 않는다.
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    // rollback 하지않고 query 를 날리기 위해서는 아래 셋팅을 해야한다.
    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
    
        //when
        Long saveId = memberService.join(member);
    
        //then
//        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));
    }

    // junit 4 에서는 아래처럼 사용한다.
//    @Test(IllegalStateException.class)
    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");
    
        //when
        memberService.join(member1);
        // Junit5 에서 exception 처리 형식.
        assertThrows(IllegalStateException.class, ()->{
            memberService.join(member2);
        });

        //then
        assertTrue(true);
    }
}