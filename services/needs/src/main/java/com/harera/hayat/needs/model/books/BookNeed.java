package com.harera.hayat.needs.model.books;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.harera.hayat.needs.model.Need;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "book_need")
public class BookNeed extends Need {

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "book_author")
    private String bookAuthor;

    @Column(name = "book_publisher")
    private String bookPublisher;

    @Column(name = "book_language")
    private String bookLanguage;
}
