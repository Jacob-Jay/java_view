package base.highLeve.index;

import base.highLeve.Utils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.io.IOException;

public class GetMapping {
    public static void main(String[] args) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest("order");
        RestHighLevelClient restHighLevelClient = Utils.get();
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println("sd");
    }
}
