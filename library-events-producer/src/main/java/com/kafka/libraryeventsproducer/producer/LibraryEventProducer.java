package com.kafka.libraryeventsproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.libraryeventsproducer.domain.LibraryEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class LibraryEventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendLibraryEvent(final LibraryEvent libraryEvent) throws JsonProcessingException {
        String key = libraryEvent.getLibraryEventId();
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(libraryEvent);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("library_topic", key, value);
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
    }

    private void handleSuccess(String key, String value, SendResult<String, String> result) {
        log.error("Message sent successfully for key {} and value {} ", key, value);
    }

    private void handleFailure(String key, String value, Throwable ex) throws Throwable {
        log.error("Error sending message, exception is {}", ex.getMessage());
        throw ex;
    }

}
