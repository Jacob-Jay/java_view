package base.highLeve.document;

import base.highLeve.Utils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;

import java.io.IOException;

/**
 * 仅仅获取source
 */
public class GetSource {
    public static void main(String[] args) throws IOException {
        GetSourceRequest getSourceRequest = new GetSourceRequest(
                "order",
                "1");
        GetSourceResponse source = Utils.get().getSource(getSourceRequest, RequestOptions.DEFAULT);
        Utils.close();
    }
}
