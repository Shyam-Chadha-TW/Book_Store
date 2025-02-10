package com.example.BookStore.BookStore.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @NotBlank(message = "Name is required")
    private String name;
    private String author;
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^[0-9]{13}$", message = "ISBN must be exactly 13 digits")
    private String isbn;
    private String publisher;
    @PositiveOrZero(message = "Security amount can't be negative")
    private Double securityAmount;
    @Positive(message = "Price should be grater than 0")
    private Double price;
    @PositiveOrZero(message = "Total quantity can't be negative")
    private Integer totalQuantity;
    @PositiveOrZero(message = "Available quantity can't be negative")
    private Integer availableQuantity;
    @PositiveOrZero(message = "Rented quantity can't be negative")
    private Integer rentedQuantity;
}
