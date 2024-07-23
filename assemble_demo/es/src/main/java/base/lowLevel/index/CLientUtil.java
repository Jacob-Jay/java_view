package base.lowLevel.index;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 所有操作与kibana的devtool一摸一样
 */
public class CLientUtil {


    public static void executre(Consumer<RestClient> consumer) {
        try {
            consumer.accept(restClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * 线程安全，全局唯一
     */
    private static RestClient restClient = init();


    private  static RestClient init(){
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200, "http"));

        /**
         * 设置默认的header
         */
        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
        builder.setDefaultHeaders(defaultHeaders);


        /**
         * 设置失败回调
         */
        builder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                System.out.println(node);
            }
        });


        /**
         * 筛选可以接收请求的节点
         */
        builder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);


        /**
         * 对请求进行配置的回调
         */
        builder.setRequestConfigCallback(
                new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(
                            RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder.setSocketTimeout(10000);
                    }
                });


        /**
         * 对请求客户端进行配置的回调
         */
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(
                    HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder;
//                return httpClientBuilder.setProxy(
//                        new HttpHost("proxy", 9000, "http"));
            }
        });

        RestClient build = builder.build();
        return build;
    }
}
