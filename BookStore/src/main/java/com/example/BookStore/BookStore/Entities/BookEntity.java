package com.example.BookStore.BookStore.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
    @Column(unique = true)
    private String isbn;
    private String publisher;
    private Double securityAmount;
    private Double price;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private Integer rentedQuantity;
}
