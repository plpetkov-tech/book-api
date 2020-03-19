package com.plamenpetkovonline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import com.plamenpetkovonline.service.Book;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.json.JsonArrayBuilder;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    Book jon;
    Book bon;
    String jonString;
    String bonString;
    String requestUri = "/api/books";
    Book[] returnMe = new Book[2];

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void setup() throws JsonProcessingException {
        jon = new Book("Lucifer","Paco","2009","Drama");
        bon = new Book("God","Pesho","2020","Fantasy");
        jonString = mapper.writeValueAsString(jon);
        bonString = mapper.writeValueAsString(bon);
        returnMe[1] = jon;
        returnMe[2] = bon;
        repository.save(jon);
        repository.save(bon);
     }

    // This test will pass
    @Test
    void getGenre() {
        //given


        //when


        //then

    }
    @Test
    void getAuthor() {
    }

    @Test
    void getYear() {
    }

    @Test
    void getTitle() {
    }

    @Test
    void get() {
    }

    @Test
    void getAll() throws Exception {
        //given
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(returnMe));
        String[] response2 = {jonString,bonString};
        String response = response2.toString();

        //when
        mockMvc.perform(MockMvcRequestBuilders.get(requestUri))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //then
        Mockito.verify(repository).findAll();
    }

    @Test
    void post() {
    }

    @Test
    void put() {
    }

    @Test
    void delete() {
    }

    @Test
    public void testApiBookEndpoint() {
        given()
          .when().get("/api/books")
          .then()
             .statusCode(200)
             .body(is("[{\"author\":\"Mike Zakousky\",\"genre\":\"Drama\",\"id\":1,\"title\":\"Cherry blossom\",\"year\":\"2009\"},{\"author\":\"Mike Zakousky\",\"genre\":\"Comedy\",\"id\":2,\"title\":\"Cherry fall\",\"year\":\"2019\"},{\"author\":\"Plamen Petkov\",\"genre\":\"Drama\",\"id\":3,\"title\":\"Roses blossom\",\"year\":\"2019\"}]"));
    }


}