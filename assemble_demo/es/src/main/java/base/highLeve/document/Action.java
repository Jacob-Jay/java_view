package base.highLeve.document;

import base.highLeve.Utils;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.function.Consumer;

public class Action {
    public static void doAction(Consumer<RestHighLevelClient> consumer){
        RestHighLevelClient restHighLevelClient = Utils.get();

        consumer.accept(restHighLevelClient);
        Utils.close();
    }
}
