package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, String name, int price, int stockQuntity) {
        // 트랜잭션 상태에서 Entity 를 조회해야 영속 상태가되고
        // 그 상태에서 값을 변경을 해야 Dirty checking 을 통해 값들이 업데이트 된다.
        Item findItem = itemRepository.findOne(itemId);

        // 아래 코드보다는 setter 없이.
        // entity 안에서 바로 추적할수 있는 method 를 만드는 것이 좋다.
        // ex. findItem.change(name, price, stockQuantity);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuntity);
        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
