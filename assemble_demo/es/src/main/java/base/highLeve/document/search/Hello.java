package base.highLeve.document.search;

import base.highLeve.document.Action;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;


/**
 * 查询基础
 */
public class Hello {
    public static void main(String[] args) {


        Action.doAction(new Consumer<RestHighLevelClient>() {
            @Override
            public void accept(RestHighLevelClient restHighLevelClient) {
                SearchRequest searchRequest = new SearchRequest();
                searchRequest.indices("order");

                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

                //查询
                searchSourceBuilder.query(termQueryBuilder());

                sort(searchSourceBuilder);

                pageLimit(searchSourceBuilder);


                filedFilter(searchSourceBuilder);

                searchRequest.source(searchSourceBuilder);


                try {
                    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

                    dealResponse(searchResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }


    /**
     * 处理响应
     *
     * {
     *   "took" : 5,
     *   "timed_out" : false,
     *   "_shards" : {
     *     "total" : 3,
     *     "successful" : 3,
     *     "skipped" : 0,
     *     "failed" : 0
     *   },
     *   "hits" : {
     *     "total" : {
     *       "value" : 9,
     *       "relation" : "eq"
     *     },
     *     "max_score" : null,
     *     "hits" : [
     *       {
     *         "_index" : "order",
     *         "_type" : "_doc",
     *         "_id" : "1",
     *         "_score" : null,
     *         "_source" : {
     *           "orderNo" : "stml00001",
     *           "payTime" : "2024-04-15T10:36:41.513Z",
     *           "refundReason" : null,
     *           "orderStatus" : 1,
     *           "storeId" : 2,
     *           "orderTime" : "2024-04-15T10:35:41.513Z",
     *           "price" : 45,
     *           "storeName" : "zz cd",
     *           "id" : 1,
     *           "cancelReason" : "bad not good  next time",
     *           "payStatus" : 1,
     *           "orderCategory" : 1,
     *           "refundAmount" : null
     *         },
     *         "sort" : [
     *           1
     *         ]
     *       }
     *     ]
     *   }
     * }
     * @param searchResponse
     */
    private static void dealResponse(SearchResponse searchResponse) {


        /**
         * 状态吗
         */
        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        Boolean terminatedEarly = searchResponse.isTerminatedEarly();
        boolean timedOut = searchResponse.isTimedOut();


        /**
         * 分片数据
         * "_shards" : {
         *      *     "total" : 3,
         *      *     "successful" : 3,
         *      *     "skipped" : 0,
         *      *     "failed" : 0
         *      *   },
         */
        int totalShards = searchResponse.getTotalShards();
        int successfulShards = searchResponse.getSuccessfulShards();
        int failedShards = searchResponse.getFailedShards();
        for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
            // failures should be handled here
        }


        /**
         * 获取数据汇总信息
         * "hits" : {
         *      *     "total" : {
         *      *       "value" : 9,
         *      *       "relation" : "eq"
         *      *     },
         *      *     "max_score" : null,
         *      *     "hits" : []
         *      }
         */
        SearchHits hits = searchResponse.getHits();
        TotalHits totalHits = hits.getTotalHits();
        long numHits = totalHits.value;
        TotalHits.Relation relation = totalHits.relation;
        float maxScore = hits.getMaxScore();


        /**
         * 获取结果，即各条数据
         *
         * {
         *      *         "_index" : "order",
         *      *         "_type" : "_doc",
         *      *         "_id" : "1",
         *      *         "_score" : null,
         *      *         "_source" : {
         *      *           "orderNo" : "stml00001",
         *      *           "payTime" : "2024-04-15T10:36:41.513Z",
         *      *           "refundReason" : null,
         *      *           "orderStatus" : 1,
         *      *           "storeId" : 2,
         *      *           "orderTime" : "2024-04-15T10:35:41.513Z",
         *      *           "price" : 45,
         *      *           "storeName" : "zz cd",
         *      *           "id" : 1,
         *      *           "cancelReason" : "bad not good  next time",
         *      *           "payStatus" : 1,
         *      *           "orderCategory" : 1,
         *      *           "refundAmount" : null
         *      *         },
         *      *         "sort" : [
         *      *           1
         *      *         ]
         *      *       }
         */
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String id = hit.getId();
            float score = hit.getScore();

            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String orderNo = (String) sourceAsMap.get("orderNo");
            System.out.println("document id is : "+id+"  order no is :"+orderNo);
        }

        System.out.println("");
    }


    /**
     * 进行字段过滤
     * @param searchSourceBuilder
     */
    private static void filedFilter(SearchSourceBuilder searchSourceBuilder) {
//        searchSourceBuilder.fetchSource(false);//不获取字段

        //过滤部分字段
        String[] includeFields = new String[] {"title", "innerObject.*"};
        String[] excludeFields = new String[] {"user"};
//        searchSourceBuilder.fetchSource(includeFields, excludeFields);
    }

    /**
     * 增加排序 可以多个一起
     * @param searchSourceBuilder
     */
    private static void sort(SearchSourceBuilder searchSourceBuilder) {
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        searchSourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));
    }

    /**
     * 数据分页
     * @param searchSourceBuilder
     */
    private static void pageLimit(SearchSourceBuilder searchSourceBuilder) {
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
    }

    private static MatchAllQueryBuilder matchAll() {
        return QueryBuilders.matchAllQuery();
    }


    private static TermQueryBuilder termQueryBuilder() {

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("storeName", "cd");
        return termQueryBuilder;
    }
}
