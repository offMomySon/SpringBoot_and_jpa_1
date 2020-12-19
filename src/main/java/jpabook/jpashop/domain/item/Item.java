package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="dtype")
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 도메인 주도 설계
    // entity 자체가 해결할수 있는것은 entity 안에 business 로직을 넣어두면 좋다.
    // 데이터를 가지고있는 것에 대해 Business 로직이 나가는 것이 응집도가 있다.
    // 보통 item service 에서 갯수를 가져와서 더해서 setStockQuantity 같은 것으로 결과를 넣는 것을 많이하는데,
    // 객체지향적으로 생각해보면 Data 를 가진쪽이 Business 메서드가 있는 것이 가장 좋다 - 응집력이 있다.

    // 아래의 경우는 재고 넣고 뺴는 로직은 stockQunatity 를 사용하고, ItemEntity 가 가지고 있음.
    // 그래서 Item Entity 에 있는 것이 관리하기 가장 좋다. 핵심 비지니스 로직을 Entity 에 넣음/
    /**
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if( restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
