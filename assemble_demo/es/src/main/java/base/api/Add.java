package base.api;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.jq.Product;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class Add {
    public static void main(String[] args) throws IOException {
        // URL and API key
        String serverUrl = "http://localhost:9200";

// Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
//                .setDefaultHeaders(new Header[]{
//                        new BasicHeader("Authorization", "ApiKey " + apiKey)
//                })
                .build();

// Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

// And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        SearchResponse<Product> search = esClient.search(s -> s
                        .index("product")
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue("jq"))
                                )),
                Product.class);

        for (Hit<Product> hit: search.hits().hits()) {
            System.out.println(hit.source());
        }

    }
}
