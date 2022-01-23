package com.kafka.libraryeventsproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.libraryeventsproducer.domain.Book;
import com.kafka.libraryeventsproducer.domain.LibraryEvent;
import com.kafka.libraryeventsproducer.domain.LibraryEventType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private LibraryEventProducer libraryEventProducer;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp(){
        //Mocking @Value annotation. Using reflection to provide default value
        ReflectionTestUtils.setField(libraryEventProducer, "topic", "library-topic");
    }

    @Test
    public void testSendLibraryEvent_failure() throws JsonProcessingException {

        //given
        Book book = Book.builder()
                .bookId("1")
                .bookName("Kafka")
                .bookAuthor("XYZ")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .eventType(LibraryEventType.NEW)
                .book(book)
                .build();
        SettableListenableFuture listenableFuture = new SettableListenableFuture();
        listenableFuture.setException(new RuntimeException("Error occurred during send"));

        //when
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(listenableFuture);

        //then
        assertThrows(Exception.class, () -> libraryEventProducer.sendLibraryEvent(libraryEvent).get());
    }


}
