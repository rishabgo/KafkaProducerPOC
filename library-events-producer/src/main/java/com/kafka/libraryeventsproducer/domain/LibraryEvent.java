package com.kafka.libraryeventsproducer.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Data
public class LibraryEvent {
    private String libraryEventId;
    private Book book;
    private LibraryEventType eventType;
}
