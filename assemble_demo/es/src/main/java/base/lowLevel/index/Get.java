package base.lowLevel.index;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

import java.io.IOException;

public class Get {
    public static void main(String[] args) {
        CLientUtil.executre(e->{
            Request request = new Request(
                    "GET",
                    "/_cat/indices");
            try {
                Response response = e.performRequest(request);
                System.out.println(response);
//                .
                System.out.println( EntityUtils.toString(response.getEntity()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
