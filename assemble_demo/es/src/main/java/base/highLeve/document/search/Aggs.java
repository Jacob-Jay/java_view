package base.highLeve.document.search;

import base.highLeve.document.Action;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public class Aggs {
    public static void main(String[] args) {
        Action.doAction(new Consumer<RestHighLevelClient>() {
            @Override
            public void accept(RestHighLevelClient restHighLevelClient) {

                SearchRequest searchRequest = new SearchRequest();
                searchRequest.indices("order");
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


                /**
                 * 创建聚合
                 */
                TermsAggregationBuilder aggregation = AggregationBuilders.terms("groupByStatus").field("orderStatus");
                aggregation.subAggregation(AggregationBuilders.sum("priceTotal").field("price"));//子聚合
                searchSourceBuilder.aggregation(aggregation);

                TermsAggregationBuilder payStatusCOunt = AggregationBuilders.terms("payStatusCOunt").field("payStatus");
                payStatusCOunt.subAggregation(AggregationBuilders.sum("priceTotal").field("price"));
                searchSourceBuilder.aggregation(payStatusCOunt);

                searchRequest.source(searchSourceBuilder);

                try {
                    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                    Aggregations aggregations = search.getAggregations();

                    for (Aggregation item : aggregations) {
                        String name = item.getName();
                        Terms inner = aggregations.get(name);
                        List<? extends Terms.Bucket> buckets = inner.getBuckets();

                        if (buckets.size() > 0) {
                            for (Terms.Bucket bucket : buckets) {
                                Aggregations innerAgg = bucket.getAggregations();
                                Sum sum = innerAgg.get("priceTotal");
                                System.out.println(name+"  aggs  name:"+bucket.getKeyAsString()+" count: "+bucket.getDocCount()+"  price sum"+ sum.value());
                            }
                        }
                    }

                    System.out.println("aa");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}
