package com.kafka.libraryeventsproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.libraryeventsproducer.domain.LibraryEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Slf4j
@Component
public class LibraryEventProducer {

    @Value("${spring.kafka.producer.topic:library-topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper mapper;

    public ListenableFuture<SendResult<String, String>> sendLibraryEvent(final LibraryEvent libraryEvent) throws JsonProcessingException {

        //generate book id
        libraryEvent.getBook().setBookId(UUID.randomUUID().toString());

        String key = libraryEvent.getLibraryEventId();
        String value = mapper.writeValueAsString(libraryEvent);

        ProducerRecord<String, String> producerRecord = buildProducerRecord(key, value, topic);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @SneakyThrows
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                handleSuccess(key, value, result);
            }
        });
        return future;
    }

    private ProducerRecord<String, String> buildProducerRecord(String key, String value, String topic) {

        return new ProducerRecord<>(topic, key, value);
    }

    private void handleSuccess(String key, String value, SendResult<String, String> result) {
        log.error("Message sent successfully for key {} and value {} ", key, value);
    }

    private void handleFailure(String key, String value, Throwable ex) throws Throwable {
        log.error("Error sending message, exception is {}", ex.getMessage());
        throw ex;
    }

}
