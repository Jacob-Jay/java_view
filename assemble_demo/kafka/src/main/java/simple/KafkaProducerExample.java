package simple;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class KafkaProducerExample {
    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Kafka服务器地址
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        TimeUnit.SECONDS.sleep(11111111);
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("my-topic", Integer.toString(i), Integer.toString(i));
            Future<RecordMetadata> send = producer.send(record);
            try {
                RecordMetadata recordMetadata = send.get();
                System.out.println(recordMetadata.offset());
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        TimeUnit.SECONDS.sleep(11111111);
        producer.close();
    }
}

