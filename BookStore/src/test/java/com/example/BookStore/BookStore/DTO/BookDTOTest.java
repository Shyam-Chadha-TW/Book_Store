package com.example.BookStore.BookStore.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;

public class BookDTOTest {
    public static final String NAME_IS_REQUIRED = "Name is required";
    public static final String ISBN_IS_REQUIRED = "ISBN is required";
    public static final String ISBN_LIMIT = "ISBN must be exactly 13 digits";
    public static final String SECURITY_AMOUNT_CANNOT_BE_NEGATIVE = "Security amount can't be negative";
    public static final String PRICE_GRATER_THAN_ZERO = "Price should be grater than 0";
    public static final String TOTAL_QUANTITY_CANNOT_BE_NEGATIVE = "Total quantity can't be negative";
    public static final String AVAILABLE_QUANTITY_CANNOT_BE_NEGATIVE = "Available quantity can't be negative";
    public static final String RENTED_QUANTITY_CANNOT_BE_NEGATIVE = "Rented quantity can't be negative";

    public final String name = "Valid Book Name";
    public final String author = "Valid Author";
    public final String publisher = "Valid Publisher";
    public final String isbn = "1234567890123";
    public final double securityAmount = 50.0;
    public final double price = 12.0;
    public final Integer totalQuantity = 100;
    public final Integer availableQuantity = 70;
    public final Integer rentedQuantity = 30;

    static Validator getValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }

    @Test
    void emptyBookNameShouldFailValidation() {
        BookDTO testRequest = new BookDTO("",author,isbn,publisher,securityAmount,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(NAME_IS_REQUIRED));

    }
    @Test
    void nullBookNameShouldFailValidation(){
        BookDTO testRequest = new BookDTO(null,author,isbn,publisher,securityAmount,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(NAME_IS_REQUIRED));
    }
    @Test
    void spaceBookNameShouldFailValidation(){
        BookDTO testRequest = new BookDTO(" ",author,isbn,publisher,securityAmount,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(NAME_IS_REQUIRED));
    }
    @Test
    void nullIsbnShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,null,publisher,securityAmount,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(ISBN_IS_REQUIRED));
    }
    @Test
    void emptyIsbnShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,"",publisher,securityAmount,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(2));
        List<String> violationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertThat(violationMessages, hasItems(ISBN_IS_REQUIRED, ISBN_LIMIT));
    }

    @Test
    void spaceIsbnShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author," ",publisher,securityAmount,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(2));
        List<String> violationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertThat(violationMessages, hasItems(ISBN_IS_REQUIRED, ISBN_LIMIT));
    }

    @Test
    void lessCharacterIsbnShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,"123456789012",publisher,securityAmount,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(ISBN_LIMIT));
    }
    @Test
    void moreCharacterIsbnShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,"12345678901234",publisher,securityAmount,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(ISBN_LIMIT));
    }
    @Test
    void negativeSecurityAmountShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,isbn,publisher,-1.0,price,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(SECURITY_AMOUNT_CANNOT_BE_NEGATIVE));
    }

    @Test
    void negativePriceShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,isbn,publisher,securityAmount,-50.0,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(PRICE_GRATER_THAN_ZERO));
    }

    @Test
    void zeroPriceShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,isbn,publisher,securityAmount,0.0,totalQuantity,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(PRICE_GRATER_THAN_ZERO));
    }
    @Test
    void negativeTotalQuantityShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,isbn,publisher,securityAmount,price,-10,availableQuantity,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(TOTAL_QUANTITY_CANNOT_BE_NEGATIVE));
    }
    @Test
    void negativeAvailableQuantityShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,isbn,publisher,securityAmount,price,totalQuantity,-5,rentedQuantity);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(AVAILABLE_QUANTITY_CANNOT_BE_NEGATIVE));
    }
    @Test
    void negativeRentedQuantityShouldFailValidation(){
        BookDTO testRequest = new BookDTO(name,author,isbn,publisher,securityAmount,price,totalQuantity,availableQuantity,-3);

        Set<ConstraintViolation<BookDTO>> violations = getValidator().validate(testRequest);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is(RENTED_QUANTITY_CANNOT_BE_NEGATIVE));
    }
}


