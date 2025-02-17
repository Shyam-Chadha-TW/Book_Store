package com.example.BookStore.BookStore.Controllers;

import com.example.BookStore.BookStore.DTO.BookDTO;
import com.example.BookStore.BookStore.Exception.ResourceNotFound;
import com.example.BookStore.BookStore.Services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddBook() throws Exception {
        BookDTO BookDTO = new BookDTO("New Book", "New Author", "1234567890123", "New Publisher",200.99, 30.0, 12, 6, 3);

        when(bookService.addBook(any(BookDTO.class))).thenReturn(BookDTO);

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Book"))
                .andExpect(jsonPath("$.author").value("New Author"))
                .andExpect(jsonPath("$.isbn").value("1234567890123"))
                .andExpect(jsonPath("$.publisher").value("New Publisher"))
                .andExpect(jsonPath("$.securityAmount").value(200.99))
                .andExpect(jsonPath("$.price").value(30.0))
                .andExpect(jsonPath("$.totalQuantity").value(12))
                .andExpect(jsonPath("$.availableQuantity").value(6))
                .andExpect(jsonPath("$.rentedQuantity").value(3));
    }

    @Test
    public void testGetBookByName() throws Exception {
        BookDTO BookDTO1 = new BookDTO("Book 1", "Author 1", "12345", "Publisher",156.15, 20.0, 10, 5, 2);
        BookDTO BookDTO2 = new BookDTO("Book 1", "Author 2", "67890", "Publisher",240.00, 25.0, 15, 7, 3);
        List<BookDTO> BookDTOList = List.of(BookDTO1, BookDTO2);

        when(bookService.getBooksByName("Book 1")).thenReturn(BookDTOList);

        mockMvc.perform(get("/book/name/{bookName}", "Book 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Book 1"))
                .andExpect(jsonPath("$[1].name").value("Book 1"));
    }

    @Test
    public void testGetBookByNameNotFound() throws Exception {
        when(bookService.getBooksByName(anyString())).thenReturn(null);

        mockMvc.perform(get("/book/name/{bookName}", "NonExistentBook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBookByIsbn() throws Exception {
        BookDTO BookDTO = new BookDTO("Book Name", "Author", "12345", "Publisher",300.19, 20.0, 10, 5, 2);

        when(bookService.getBookByIsbn(anyString())).thenReturn(BookDTO);

        mockMvc.perform(get("/book/isbn/{isbn}", "12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Book Name"))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.publisher").value("Publisher"))
                .andExpect(jsonPath("$.securityAmount").value(300.19))
                .andExpect(jsonPath("$.price").value(20.0))
                .andExpect(jsonPath("$.totalQuantity").value(10))
                .andExpect(jsonPath("$.availableQuantity").value(5))
                .andExpect(jsonPath("$.rentedQuantity").value(2));
    }

    @Test
    public void testGetBookByIsbnNotFound() throws Exception {
        when(bookService.getBookByIsbn(anyString())).thenReturn(null);

        mockMvc.perform(get("/book/isbn/{isbn}", "NonExistentISBN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllBooks() throws Exception {
        BookDTO BookDTO1 = new BookDTO("Book 1", "Author 1", "12345", "Publisher",156.15, 20.0, 10, 5, 2);
        BookDTO BookDTO2 = new BookDTO("Book 2", "Author 2", "67890", "Publisher",240.00, 25.0, 15, 7, 3);
        List<BookDTO> BookDTOList = List.of(BookDTO1, BookDTO2);

        when(bookService.getAllBooks()).thenReturn(BookDTOList);

        mockMvc.perform(get("/book/getAllBooks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Book 1"))
                .andExpect(jsonPath("$[1].name").value("Book 2"));
    }

    @Test
    public void testGetAllBooksNotFound() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of());

        mockMvc.perform(get("/book/getAllBooks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookDTO BookDTO = new BookDTO("Updated Book", "Updated Author", "1122334455667", "Updated Publisher",400.99, 35.0, 15, 7, 4);

        when(bookService.updateBook(any(BookDTO.class))).thenReturn(BookDTO);

        mockMvc.perform(put("/book/updateBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.isbn").value("1122334455667"))
                .andExpect(jsonPath("$.publisher").value("Updated Publisher"))
                .andExpect(jsonPath("$.securityAmount").value(400.99))
                .andExpect(jsonPath("$.price").value(35.0))
                .andExpect(jsonPath("$.totalQuantity").value(15))
                .andExpect(jsonPath("$.availableQuantity").value(7))
                .andExpect(jsonPath("$.rentedQuantity").value(4));
    }

    @Test
    public void testUpdateBookNotFound() throws Exception {
        BookDTO BookDTO = new BookDTO("NonExistent Book", "Unknown Author", "1234567890123", "Unknown Publisher",700.20, 40.0, 5, 2, 1);

        when(bookService.updateBook(any(BookDTO.class))).thenReturn(null);

        mockMvc.perform(put("/book/updateBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BookDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRentBook() throws Exception {

        when(bookService.rentBook("The Lord of the Rings")).thenReturn(
                new BookDTO("The Lord of the Rings", "Author", "12345", "Publisher", 350.0, 20.0, 10, 199, 51)
        );

        mockMvc.perform(put("/book/rentBook/{bookName}", "The Lord of the Rings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("The Lord of the Rings"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.publisher").value("Publisher"))
                .andExpect(jsonPath("$.securityAmount").value(350.00))
                .andExpect(jsonPath("$.price").value(20.0))
                .andExpect(jsonPath("$.totalQuantity").value(10))
                .andExpect(jsonPath("$.availableQuantity").value(199))
                .andExpect(jsonPath("$.rentedQuantity").value(51));
    }

    @Test
    public void testNonExistingRentBook() throws Exception {
        when(bookService.rentBook(anyString())).thenThrow(new ResourceNotFound("ISBN not found!!"));

        mockMvc.perform(put("/book/rentBook/NonExistentBook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("ISBN not found!!"));
    }

    @Test
    public void testZeroAvailableQuantityRentBook() throws Exception {
        when(bookService.rentBook(anyString())).thenThrow(new ResourceNotFound("All the Books are Rented"));

        mockMvc.perform(put("/book/rentBook/{bookName}", "Book Name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("All the Books are Rented"));
    }

    @Test
    public void testReturnBook() throws Exception {
        when(bookService.returnBook(anyString())).thenReturn(
                new BookDTO("Book Name", "Author", "12345", "Publisher",180.40, 20.0, 10, 6, 4)

        );

        mockMvc.perform(put("/book/returnBook/{bookName}", "Book Name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Book Name"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.publisher").value("Publisher"))
                .andExpect(jsonPath("$.securityAmount").value(180.40))
                .andExpect(jsonPath("$.price").value(20.0))
                .andExpect(jsonPath("$.totalQuantity").value(10))
                .andExpect(jsonPath("$.availableQuantity").value(6))
                .andExpect(jsonPath("$.rentedQuantity").value(4));
    }

    @Test
    public void testNonExistingReturnBook() throws Exception {
        when(bookService.returnBook(anyString())).thenThrow(new ResourceNotFound("ISBN not found!!"));

        mockMvc.perform(put("/book/returnBook/NonExistentBook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("ISBN not found!!"));
    }

    @Test
    public void testZeroRentedQuantityReturnBook() throws Exception {
        when(bookService.returnBook(anyString())).thenThrow(new ResourceNotFound("No rented copies for this book there"));

        mockMvc.perform(put("/book/returnBook/{bookName}", "Book Name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("No rented copies for this book there"));
    }

    @Test
    public void testDeleteBookByIsbnNotFound() throws Exception {
        when(bookService.deleteBookByIsbn(anyString())).thenThrow(new ResourceNotFound("No Book of this ISBN found"));

        mockMvc.perform(delete("/book/delete/NonExistentISBN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("No Book of this ISBN found"));
    }

    @Test
    public void testAddBookInvalidInput() throws Exception {
        BookDTO invalidBookDTO = new BookDTO("", "", "", "",-42.0, -1.0, -5, -3, -2);

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBookDTO)))
                .andExpect(status().isBadRequest());
    }
}