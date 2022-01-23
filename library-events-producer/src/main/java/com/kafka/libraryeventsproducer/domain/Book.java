package com.kafka.libraryeventsproducer.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Book {

    private String bookId;
    private String bookName;
    private String bookAuthor;
}
