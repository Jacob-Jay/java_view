package base.lowLevel.index;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

import java.io.IOException;

public class Analyze {
    public static void main(String[] args) {
        CLientUtil.executre(e->{
            Request request = new Request(
                    "POST",
                    "_analyze");
            request.setJsonEntity("{\n" +
                    "  \"analyzer\": \"fingerprint\",\n" +
                    "  \"text\": \"Yes yes, Gödel said this sentence is consistent and.\"\n" +
                    "}");
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
