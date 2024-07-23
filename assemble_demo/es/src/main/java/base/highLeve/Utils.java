package base.highLeve;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * 机遇lowleve进行了封装  不再是构建请求 而是通过es提供的组件填充信息  es自动转换为请求
 */
public class Utils {

    private static  RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));
    public static RestHighLevelClient get(){
        return client;
    }
    public static void close(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
