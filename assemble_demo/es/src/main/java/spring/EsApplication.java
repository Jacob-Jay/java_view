package spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class EsApplication {

    static List<Order> orders = new ArrayList<>();
    static List<Long> storeIds = new ArrayList<>();
    static List<String> storeNames = new ArrayList<>();
    static List<String> refundReason = new ArrayList<>();


    static List<String> cancelReason = new ArrayList<>();

    static {
        storeIds.add(1L);
        storeIds.add(2L);

        storeNames.add("jq xian");
        storeNames.add("zz cd");

        refundReason.add("not good  cancel");
        refundReason.add("bad not good  next time");

        cancelReason.add("not good  cancel");
        cancelReason.add("bad not good  next time");



        LocalDateTime orderTime = LocalDateTime.now();
        Random random = new Random();

        for (int i = 1; i < 10; i++) {
           Order data = new Order();
            data.setId((long) i);
            data.setOrderNo("stml0000"+i);
            data.setStoreId(storeIds.get(i%storeIds.size()));
            data.setStoreName(storeNames.get(i%storeNames.size()));
            data.setOrderTime(orderTime.plusDays(i));
            data.setPayTime(data.getOrderTime().plusMinutes(i));

            int price = random.nextInt(100);

            data.setPrice(new BigDecimal(price));
            if (price > 50) {
                data.setRefundAmount(new BigDecimal(price/2));
                data.setRefundReason(refundReason.get(i%refundReason.size()));
            }


            data.setCancelReason(cancelReason.get(i%cancelReason.size()));
            data.setOrderCategory(i%3);
            data.setOrderStatus(i%5);
            data.setPayStatus(i%5);
            orders.add(data);
        }

    }


    public static List<Order> data() {
        return orders;
    }

    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }

}
