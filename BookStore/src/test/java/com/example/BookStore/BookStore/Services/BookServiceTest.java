package com.example.BookStore.BookStore.Services;

import com.example.BookStore.BookStore.DTO.BookDTO;
import com.example.BookStore.BookStore.Entities.BookEntity;
import com.example.BookStore.BookStore.Exception.ResourceNotAvailable;
import com.example.BookStore.BookStore.Exception.ResourceNotFound;
import com.example.BookStore.BookStore.Repositiories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookService bookService;

    private BookEntity bookEntity;
    private BookDTO BookDTO;

    @BeforeEach
    public void setUp() {
        bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setName("Test Book");
        bookEntity.setIsbn("1234567890123");
        bookEntity.setAvailableQuantity(10);
        bookEntity.setRentedQuantity(0);

        BookDTO = new BookDTO();
        BookDTO.setName("Test Book");
        BookDTO.setIsbn("1234567890123");
        BookDTO.setAvailableQuantity(10);
        BookDTO.setRentedQuantity(0);
    }

    @Test
    public void testAddBook() {
        when(modelMapper.map(BookDTO, BookEntity.class)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(modelMapper.map(bookEntity, BookDTO.class)).thenReturn(BookDTO);

        BookDTO savedBookDTO = bookService.addBook(BookDTO);

        assertNotNull(savedBookDTO);
        assertEquals("Test Book", savedBookDTO.getName());
        verify(bookRepository, times(1)).save(bookEntity);
    }

    @Test
    public void testGetBookByName() {
        when(bookRepository.findByName("Test Book")).thenReturn(Collections.singletonList(bookEntity));

        when(modelMapper.map(bookEntity, BookDTO.class)).thenReturn(BookDTO);

        List<BookDTO> foundBookDTOList = bookService.getBooksByName("Test Book");

        assertNotNull(foundBookDTOList);
        assertEquals(1, foundBookDTOList.size());
        assertEquals("Test Book", foundBookDTOList.get(0).getName());

        assertEquals("1234567890123", foundBookDTOList.get(0).getIsbn());
        verify(bookRepository, times(1)).findByName("Test Book");
    }

    @Test
    public void testGetBookByNameNotFound() {
        when(bookRepository.findByName("Test Book")).thenReturn(Collections.emptyList());

        ResourceNotFound exception = assertThrows(ResourceNotFound.class, () -> bookService.getBooksByName("Test Book"));

        assertEquals("No book of this name found", exception.getMessage());
    }

    @Test
    public void testUpdateBook() {
        BookDTO.setIsbn("1234567890123");

        when(bookRepository.existsByIsbn("1234567890123")).thenReturn(true);
        when(bookRepository.findByIsbn("1234567890123")).thenReturn(Optional.of(bookEntity));
        when(modelMapper.map(BookDTO, BookEntity.class)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(modelMapper.map(bookEntity, BookDTO.class)).thenReturn(BookDTO);

        BookDTO updatedBookDTO = bookService.updateBook(BookDTO);

        assertNotNull(updatedBookDTO);
        assertEquals("Test Book", updatedBookDTO.getName());

        verify(bookRepository, times(1)).existsByIsbn("1234567890123");
        verify(bookRepository, times(1)).findByIsbn("1234567890123");
        verify(bookRepository, times(1)).save(bookEntity);
    }


    @Test
    public void testDeleteBookByIsbn() {
        bookEntity.setIsbn("1234567890123");
        BookDTO.setIsbn("1234567890123");

        when(bookRepository.findByIsbn("1234567890123")).thenReturn(Optional.of(bookEntity));

        BookDTO deletedBookDTO = bookService.deleteBookByIsbn("1234567890123");

        assertNull(deletedBookDTO);
        verify(bookRepository, times(1)).deleteByIsbn("1234567890123");
    }

    @Test
    public void testRentBook() {
        bookEntity.setIsbn("1234567890123");
        bookEntity.setName("Test Book");
        bookEntity.setAvailableQuantity(10);
        bookEntity.setRentedQuantity(0);

        BookDTO.setIsbn("1234567890123");
        BookDTO.setName("Test Book");
        BookDTO.setAvailableQuantity(9);
        BookDTO.setRentedQuantity(1);

        when(bookRepository.findByIsbn("1234567890123")).thenReturn(Optional.of(bookEntity));
        when(bookRepository.saveAndFlush(bookEntity)).thenReturn(bookEntity);
        when(modelMapper.map(bookEntity, BookDTO.class)).thenReturn(BookDTO);

        BookDTO rentedBookDTO = bookService.rentBook("1234567890123");

        assertNotNull(rentedBookDTO);
        assertEquals(1, bookEntity.getRentedQuantity());
        assertEquals(9, bookEntity.getAvailableQuantity());

        verify(bookRepository, times(1)).saveAndFlush(bookEntity);
    }

    @Test
    public void testRentBookWithNoAvailableCopies() {
        bookEntity.setAvailableQuantity(0);
        when(bookRepository.findByIsbn("1234567890123")).thenReturn(Optional.of(bookEntity));

        ResourceNotAvailable exception = assertThrows(ResourceNotAvailable.class, () -> bookService.rentBook("1234567890123"));

        assertEquals("All the Books are Rented", exception.getMessage());
    }

    @Test
    public void testReturnBook() {
        bookEntity.setIsbn("1234567890123");
        bookEntity.setName("Test Book");
        bookEntity.setAvailableQuantity(12);
        bookEntity.setRentedQuantity(12);

        BookDTO.setIsbn("1234567890123");
        BookDTO.setName("Test Book");
        BookDTO.setAvailableQuantity(13);
        BookDTO.setRentedQuantity(11);

        when(bookRepository.findByIsbn("1234567890123")).thenReturn(Optional.of(bookEntity));
        when(bookRepository.saveAndFlush(bookEntity)).thenReturn(bookEntity);
        when(modelMapper.map(bookEntity, BookDTO.class)).thenReturn(BookDTO);

        BookDTO returnedBookDTO = bookService.returnBook("1234567890123");

        assertNotNull(returnedBookDTO);
        assertEquals(11, bookEntity.getRentedQuantity());
        assertEquals(13, bookEntity.getAvailableQuantity());

        verify(bookRepository, times(1)).saveAndFlush(bookEntity);
    }


    @Test
    public void testReturnBookWithNoRentedCopies() {
        when(bookRepository.findByIsbn("1234567890123")).thenReturn(Optional.of(bookEntity));

        ResourceNotFound exception = assertThrows(ResourceNotFound.class, () -> bookService.returnBook("1234567890123"));

        assertEquals("No rented copies for this book there", exception.getMessage());
    }
}
