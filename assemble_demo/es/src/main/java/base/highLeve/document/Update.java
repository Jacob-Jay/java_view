package base.highLeve.document;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.get.GetResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Update {

    public static void main(String[] args) {
        Action.doAction(new Consumer<RestHighLevelClient>() {
            @Override
            public void accept(RestHighLevelClient restHighLevelClient) {
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("orderTime", LocalDateTime.now());
                UpdateRequest request = new UpdateRequest("order", "1")
                        .doc(jsonMap);

                try {
                    UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
                    dealResponse(updateResponse);


                    getData(updateResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取数据（如果更新时开启了数据获取）
     * @param updateResponse
     */
    private static void getData(UpdateResponse updateResponse) {
        GetResult result = updateResponse.getGetResult();
        if (result.isExists()) {
            String sourceAsString = result.sourceAsString();
            Map<String, Object> sourceAsMap = result.sourceAsMap();
            byte[] sourceAsBytes = result.source();
        } else {

        }
    }

    private static void dealResponse(UpdateResponse updateResponse) {
        String index = updateResponse.getIndex();
        String id = updateResponse.getId();
        long version = updateResponse.getVersion();
        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {

        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {

        } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {

        } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {

        }
    }
}
