package com.kafka.libraryeventsproducer.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LibraryEvent {
    private String libraryEventId;
    private Book book;
    @Setter
    private LibraryEventType eventType;
}
