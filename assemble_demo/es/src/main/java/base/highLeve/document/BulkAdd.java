package base.highLeve.document;

import com.jq.Order;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class BulkAdd {

    static  List<Order> orders = new ArrayList<>();
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
        Action.doAction(new Consumer<RestHighLevelClient>() {
            @Override
            public void accept(RestHighLevelClient restHighLevelClient) {
                BulkRequest request = new BulkRequest();


                int index = 1;
                for (Order order : orders) {
                    request.add(new IndexRequest("order")
                            .id(String.valueOf(index++)).source(Order.mapData(order)));
                }

                try {
                    BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);


                    dealResponse(bulkResponse);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private static void dealResponse(BulkResponse bulkResponse) {
        if (bulkResponse.hasFailures()) {
//判断是否存在失败，但不知道是哪一项，需要便利
        }


        /**
         * 因为可以存在多种操作，区分处理
         */
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();

            if (bulkItemResponse.isFailed()) {
//                判断是否失败
                BulkItemResponse.Failure failure =
                        bulkItemResponse.getFailure();
            }
            switch (bulkItemResponse.getOpType()) {
                case INDEX:
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
            }
        }
    }
}
