package base.highLeve.document;

import com.jq.Order;
import base.highLeve.Utils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;

public class Add {

    public static void main(String[] args) throws IOException {



        RestHighLevelClient client = Utils.get();
        IndexResponse indexResponse = client.index(buildByMap(), RequestOptions.DEFAULT);
        String index = indexResponse.getIndex();
        String id = indexResponse.getId();


        /**
         * 可以获取操作结果是插入还是更新
         */
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("insert");
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("update");
        }

        /**
         * 获取分片结果
         */
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            System.out.println("same shard failed");
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                String reason = failure.reason();
            }
        }
        Utils.close();
    }

    private static IndexRequest buildByMap() {

        IndexRequest indexRequest = new IndexRequest("order")
                .id("1").source(Order.mapData(Order.data()));
        return  indexRequest;
    }

    private static IndexRequest buildByJson() {
        IndexRequest request = new IndexRequest("posts");
        request.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);
        return request;
    }
}
