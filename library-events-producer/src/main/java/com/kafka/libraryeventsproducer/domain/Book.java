package com.kafka.libraryeventsproducer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    private String bookId;
    private String bookName;
    private String bookAuthor;
}
