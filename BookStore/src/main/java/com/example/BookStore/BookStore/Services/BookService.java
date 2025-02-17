package com.example.BookStore.BookStore.Services;

import com.example.BookStore.BookStore.DTO.BookDTO;
import com.example.BookStore.BookStore.Entities.BookEntity;
import com.example.BookStore.BookStore.Exception.ResourceAlreadyExist;
import com.example.BookStore.BookStore.Exception.ResourceNotAvailable;
import com.example.BookStore.BookStore.Exception.ResourceNotFound;
import com.example.BookStore.BookStore.Repositiories.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookDTO addBook(BookDTO bookDTO) {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new ResourceAlreadyExist("ISBN already present.");
        }
        BookEntity bookEntity = modelMapper.map(bookDTO, BookEntity.class);
        bookEntity.setTotalQuantity(bookEntity.getRentedQuantity() + bookEntity.getAvailableQuantity());
        bookEntity = bookRepository.save(bookEntity);
        return modelMapper.map(bookEntity, BookDTO.class);
    }

    public List<BookDTO> getBooksByName(String bookName) {
        List<BookEntity> bookEntities = bookRepository.findByName(bookName);
        if (bookEntities.isEmpty()) {
            throw new ResourceNotFound("No book of this name found");
        }
        return bookEntities.stream().map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class)).collect(Collectors.toList());
    }

    public BookDTO getBookByIsbn(String isbn) {
        BookEntity book = bookRepository.findByIsbn(isbn).orElseThrow(()-> new ResourceNotFound("No Book of this ISBN found"));
        return modelMapper.map(book,BookDTO.class);
    }

    public List<BookDTO> getAllBooks() {
        List<BookEntity> bookEntities = bookRepository.findAll();
        if (bookEntities.isEmpty()) {
            throw new ResourceNotFound("No Books found");
        }
        return bookEntities.stream().map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class)).collect(Collectors.toList());
    }

    public BookDTO updateBook(BookDTO bookDTO) {
        if(!bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new ResourceNotFound("ISBN not found!!");
        }
        BookEntity book = bookRepository.findByIsbn(bookDTO.getIsbn()).orElseThrow(() -> new ResourceNotFound("ISBN not found!!"));
        BookEntity bookEntity = modelMapper.map(bookDTO, BookEntity.class);
        bookEntity.setId(book.getId());
        bookEntity = bookRepository.save(bookEntity);
        return modelMapper.map(bookEntity, BookDTO.class);
    }

    public BookDTO deleteBookByIsbn(String isbn) {
        BookEntity book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new ResourceNotFound("No Book of this ISBN found"));
        if (book.getRentedQuantity() != 0) {
            throw new IllegalStateException("Cannot delete the book because it has rented copies.");
        }
        bookRepository.deleteByIsbn(isbn);
        return modelMapper.map(book,BookDTO.class);
    }

    @Transactional
    public BookDTO rentBook(String isbn) {
        BookEntity bookEntity = bookRepository.findByIsbn(isbn).orElseThrow(()->new ResourceNotFound("ISBN not found!!"));

        if(bookEntity.getAvailableQuantity() == 0){
            throw new ResourceNotAvailable("All the Books are Rented");
        }
        bookEntity.setRentedQuantity(bookEntity.getRentedQuantity() + 1);
        bookEntity.setAvailableQuantity(bookEntity.getAvailableQuantity() - 1);

        bookRepository.saveAndFlush(bookEntity);

        return modelMapper.map(bookEntity, BookDTO.class);
    }

    @Transactional
    public BookDTO returnBook(String isbn) {
        BookEntity bookEntity = bookRepository.findByIsbn(isbn).orElseThrow(()->new ResourceNotFound("ISBN not found!!"));
        if(bookEntity.getRentedQuantity() == 0){
            throw new ResourceNotFound("No rented copies for this book there");
        }
        bookEntity.setRentedQuantity(bookEntity.getRentedQuantity() - 1);
        bookEntity.setAvailableQuantity(bookEntity.getAvailableQuantity() + 1);
        bookRepository.saveAndFlush(bookEntity);
        return modelMapper.map(bookEntity, BookDTO.class);
    }
}
