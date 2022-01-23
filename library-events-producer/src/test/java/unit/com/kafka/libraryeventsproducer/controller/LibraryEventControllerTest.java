package com.kafka.libraryeventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.libraryeventsproducer.domain.Book;
import com.kafka.libraryeventsproducer.domain.LibraryEvent;
import com.kafka.libraryeventsproducer.domain.LibraryEventType;
import com.kafka.libraryeventsproducer.producer.LibraryEventProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private LibraryEventProducer libraryEventProducer;

    @Test
    public void testPostLibraryEvent() throws Exception {


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
        String jsonRequest = objectMapper.writeValueAsString(libraryEvent);
        //expect
        mockMvc.perform(post("/api/v1/library")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());


    }
}
