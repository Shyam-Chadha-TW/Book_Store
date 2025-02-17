package com.example.BookStore.BookStore.Controllers;

import com.example.BookStore.BookStore.DTO.BookDTO;
import com.example.BookStore.BookStore.Exception.ResourceAlreadyExist;
import com.example.BookStore.BookStore.Exception.ResourceNotFound;
import com.example.BookStore.BookStore.Services.BookService;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody @Valid BookDTO bookDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errorMessages,HttpStatus.BAD_REQUEST);
        }
        BookDTO newBook = bookService.addBook(bookDTO);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    @GetMapping("/name/{bookName}")
    public ResponseEntity<List<BookDTO>> getBookByName(@PathVariable String bookName) {
        List<BookDTO> bookDTOList = bookService.getBooksByName(bookName);
        return (bookDTOList ==null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        BookDTO bookDTO = bookService.getBookByIsbn(isbn);
        return (bookDTO==null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookDTOList = bookService.getAllBooks();
        return (bookDTOList.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(bookDTOList,HttpStatus.OK);
    }

    @PutMapping("/updateBook")
    public ResponseEntity<BookDTO> updateBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO bookDTOUpdated = bookService.updateBook(bookDTO);
        return (bookDTOUpdated==null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(bookDTOUpdated,HttpStatus.OK);
    }

    @PutMapping("/rentBook/{isbn}")
    public ResponseEntity<BookDTO> rentBook(@PathVariable String isbn) {
        BookDTO bookDTO = bookService.rentBook(isbn);
        return new ResponseEntity<>(bookDTO,HttpStatus.OK);
    }

    @PutMapping("/returnBook/{isbn}")
    public ResponseEntity<BookDTO> returnBook(@PathVariable String isbn) {
        BookDTO bookDTO = bookService.returnBook(isbn);
        return new ResponseEntity<>(bookDTO,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{isbn}")
    public ResponseEntity<BookDTO> deleteBookByIsbn(@PathVariable String isbn) {
        BookDTO bookDTO = bookService.deleteBookByIsbn(isbn);
        return new ResponseEntity<>(bookDTO,HttpStatus.OK);
    }
}
