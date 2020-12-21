package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성.
        Order order = Order.crateOrder(member, delivery, orderItem);

        //주문 저장
        // order 가 orderItem, delivery 을 관리하기 때문에 cascade 셋팅을 해서 order 만 persist 해도 나머지 들이 persist 된다.
        // 즉, 라이프사이클이 같고 private owner 인경우에만 사용하는 것이 좋다.
        // 만약, delivery 가 다른곳에서도 관리된다면 쓰면안된다.
        // 왜냐하면 cascade 로인해 나머지도 다 지워질 수 있기 때문에.
        orderRepository.save(order);

        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        // entity 에 비즈니스 로직을 넣는 것을 도메인 모델 패턴이라 한다.
        // 반대로 엔티티에 비지니스 로직이 거의 없고 서비스 계층에서 대부분의 비지니스 로직을 처리하는 것을 트랜잭션 스크립트 패턴이라 한다.
        order.cancel();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
