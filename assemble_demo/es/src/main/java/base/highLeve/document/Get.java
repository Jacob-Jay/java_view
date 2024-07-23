package base.highLeve.document;

import base.highLeve.Utils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.Map;

/**
 * 获取文档，包括source、type等
 */
public class Get {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = Utils.get();


        try {
            GetRequest request = new GetRequest(
                    "order",
                    "11");

            filterFIled(request);


            getstoreField(request);


            GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
            String message = getResponse.getField("orderNo").getValue();

            dealResponse(getResponse);

            System.out.println("aaa");
        } catch (Exception e) {

            e.printStackTrace();



            if (e instanceof ElasticsearchException) {
                ElasticsearchException e1 = (ElasticsearchException) e;
                if (e1.status() == RestStatus.NOT_FOUND) {
                //如果索引不存在，会是此状态
                }

                if (e1.status() == RestStatus.CONFLICT) {
                    //如果版本不一致，会是此状态
                }

            }
        }
        Utils.close();
    }

    /**
     * 处理响应
     * @param getResponse
     */
    private static void dealResponse(GetResponse getResponse) {
        String index = getResponse.getIndex();
        String id = getResponse.getId();
        if (getResponse.isExists()) {//是否存在对应文档
            long version = getResponse.getVersion();
            String sourceAsString = getResponse.getSourceAsString();
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            byte[] sourceAsBytes = getResponse.getSourceAsBytes();
            System.out.println("aa");
        } else {

        }
    }

    /**
     * 获取store的字段，该字段必须store=true
     * @param request
     */
    private static void getstoreField(GetRequest request) {
        request.storedFields("orderNo");
    }

    /**
     * source字段进行筛选
     * @param request
     */
    private static void filterFIled(GetRequest request) {

//        request.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE); //不反悔数据
        String[] excludes = new String[]{"cancelReason"};
        String[] includes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext =
                new FetchSourceContext(true, includes, excludes);
        request.fetchSourceContext(fetchSourceContext);
    }
}
