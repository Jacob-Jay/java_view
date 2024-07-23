package base.highLeve.index;

import base.highLeve.Utils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class AddIndex {
    public static void main(String[] args) throws IOException, InterruptedException {
        CreateIndexRequest request = new CreateIndexRequest("order");


//        both(request);

        multiStep(request);

        RestHighLevelClient restHighLevelClient = Utils.get();
//        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        restHighLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                boolean acknowledged = createIndexResponse.isAcknowledged();

                boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("fail");
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        Utils.close();
    }

    /**
     * 全部一次性设置
     * @param request
     */
    private static void both(CreateIndexRequest request) {
        request.source("{\n" +
                "    \"settings\" : {\n" +
                "        \"number_of_shards\" : 1,\n" +
                "        \"number_of_replicas\" : 0\n" +
                "    },\n" +
                "    \"mappings\" : {\n" +
                "        \"properties\" : {\n" +
                "            \"message\" : { \"type\" : \"text\" }\n" +
                "        }\n" +
                "    },\n" +
                "    \"aliases\" : {\n" +
                "        \"twitter_alias\" : {}\n" +
                "    }\n" +
                "}", XContentType.JSON);
    }


    /**
     * 分为3部分单独设置
     * @param request
     */
    private static void multiStep(CreateIndexRequest request) {
        indexSetting(request);


        mappingSet(request);

        aliasSetting(request);
    }


    /**
     * 别名设置
     * @param request
     */
    private static void aliasSetting(CreateIndexRequest request) {
        request.alias(new Alias("twitter_alias").filter(QueryBuilders.matchQuery("payStatus", "1")));
    }


    /**
     * 字段映射关系
     * @param request
     */
    private static void mappingSet(CreateIndexRequest request) {
        jsonMapping(request);

//        mapMapping(request);

    }

    private static void mapMapping(CreateIndexRequest request) {




        Map<String, Object> properties = new HashMap<>();

        Map<String, Object> message = new HashMap<>();
        message.put("type", "text");
        properties.put("message", message);




        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);
    }

    private static void jsonMapping(CreateIndexRequest request) {
        request.mapping(
                "{\n" +
                        "    \"properties\":{\n" +
                        "      \"id\":{\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\"orderNo\":{\n" +
                        "        \"type\": \"keyword\"\n," +
                        "        \"store\": \"true\"\n" +
                        "      },\"orderStatus\":{\n" +
                        "        \"type\": \"integer\"\n" +
                        "      },\"payStatus\":{\n" +
                        "        \"type\": \"integer\"\n" +
                        "      },\"storeId\":{\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\"storeName\":{\n" +
                        "        \"type\": \"text\"\n" +
                        "      },\"orderTime\":{\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\":\"yyyy-MM-dd HH:mm:ss\"" +
                        "      },\"payTime\":{\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\":\"yyyy-MM-dd HH:mm:ss\"" +
                        "      },\"price\":{\n" +
                        "        \"type\": \"float\"\n" +
                        "      },\"refundAmount\":{\n" +
                        "        \"type\": \"float\"\n" +
                        "      },\"refundReason\":{\n" +
                        "        \"type\": \"text\"\n" +
                        "      },\"cancelReason\":{\n" +
                        "        \"type\": \"text\"\n" +
                        "      },\"orderCategory\":{\n" +
                        "        \"type\": \"integer\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }",
                XContentType.JSON);
    }

    /**
     * 进行索引设置 分片数 副本数等
     * @param request
     */
    private static void indexSetting(CreateIndexRequest request) {
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 0)
        );
    }
}
