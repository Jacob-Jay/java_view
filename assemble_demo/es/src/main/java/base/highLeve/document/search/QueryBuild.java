package base.highLeve.document.search;

import base.highLeve.document.Action;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.function.Consumer;

public class QueryBuild {
    public static void main(String[] args) {
        Action.doAction(new Consumer<RestHighLevelClient>() {
            @Override
            public void accept(RestHighLevelClient restHighLevelClient) {

                SearchRequest searchRequest = new SearchRequest();
                searchRequest.indices("order");
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


                searchSourceBuilder.query(booleanQuery());




                searchRequest.source(searchSourceBuilder);


                try {
                    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                    long value = search.getHits().getTotalHits().value;
                    if (value > 0) {
                        SearchHit[] hits = search.getHits().getHits();
                        for (SearchHit hit : hits) {
                            System.out.println(hit.getSourceAsString());
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static BoolQueryBuilder booleanQuery() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("storeName", "cd"));
        boolQueryBuilder.should(QueryBuilders.termQuery("orderStatus", "1"));
        boolQueryBuilder.should(QueryBuilders.termQuery("orderStatus", "2"));
        boolQueryBuilder.minimumShouldMatch(1);
        return boolQueryBuilder;
    }

    private static MatchPhraseQueryBuilder matchPhrase() {
        return QueryBuilders.matchPhraseQuery("cancelReason", "next time");
    }

    private static MatchQueryBuilder match() {
        return QueryBuilders.matchQuery("storeName", "cd");
    }



}
