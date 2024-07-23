package spring;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EsTest {

    @Autowired
    private OrderRepository orderRepository;






    @GetMapping("init")
    public String init(){
        List<Order> data = EsApplication.data();
        orderRepository.saveAll(data);
        return "asd";
    }

    @GetMapping("get/{id}")
    public Order getById(@PathVariable Long id) {
        return orderRepository.findById(id).get();
    }

    @GetMapping("update/{id}/{storeName}")
    public List<Order> update(@PathVariable Long id,@PathVariable String storeName) {
        List<Order> result = new ArrayList<>();
        Order order = new Order();
        order.setId(id);
        order.setStoreName(storeName);
        Order order1 = orderRepository.findById(id).get();
        orderRepository.save(order);
        Order order2 = orderRepository.findById(id).get();
        result.add(order1);
        result.add(order2);
        return result;
    }

    @GetMapping("update/{orderStatusa}/{storeName}/{orderStatusb}")
    public List<Order>query(@PathVariable String orderStatusa,@PathVariable String storeName,@PathVariable String orderStatusb){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("storeName", storeName));
        boolQueryBuilder.should(QueryBuilders.termQuery("orderStatus", orderStatusb));
        boolQueryBuilder.should(QueryBuilders.termQuery("orderStatus", orderStatusb));
        boolQueryBuilder.minimumShouldMatch(1);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
//        orderRepository.searchSimilar()
        return null;
    }

}
