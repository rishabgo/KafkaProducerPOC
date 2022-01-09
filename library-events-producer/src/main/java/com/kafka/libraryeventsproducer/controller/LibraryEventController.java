package com.kafka.libraryeventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.libraryeventsproducer.domain.LibraryEvent;
import com.kafka.libraryeventsproducer.domain.LibraryEventType;
import com.kafka.libraryeventsproducer.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class LibraryEventController {

    @Autowired
   private LibraryEventProducer libraryEventProducer;

    @PostMapping("/library")
    public ResponseEntity postLibraryEvents(@RequestBody final LibraryEvent libraryEvent) throws JsonProcessingException {
        //call producer to publish library event
        libraryEvent.setEventType(LibraryEventType.NEW);
        libraryEventProducer.sendLibraryEvent(libraryEvent);
        return new ResponseEntity(libraryEvent, HttpStatus.CREATED);
    }

    @PutMapping("/library")
    public ResponseEntity putLibraryEvents(@RequestBody final LibraryEvent libraryEvent) throws JsonProcessingException {
        libraryEvent.setEventType(LibraryEventType.UPDATE);
        //call producer to publish library event
        libraryEventProducer.sendLibraryEvent(libraryEvent);
        return new ResponseEntity(libraryEvent, HttpStatus.OK);
    }
}
