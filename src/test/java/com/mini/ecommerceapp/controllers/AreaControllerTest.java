package com.mini.ecommerceapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerceapp.exceptions.ExceptionDetails;
import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.repository.AreaRepository;
import com.mini.ecommerceapp.utils.MockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.List;

import static com.mini.ecommerceapp.utils.Constants.AREA_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AreaControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private MockHttpServletRequest mockRequest;

    private ObjectMapper objectMapper;

    @Autowired
    private AreaRepository repository;

    private Area globalArea;

    @Autowired
    public void create(WebApplicationContext context) {
        webTestClient = MockMvcWebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .defaultRequest(MockMvcRequestBuilders.get("/")
                        .with(SecurityMockMvcRequestPostProcessors
                                .jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        ))
                        .configureClient()
                        .build();
    }

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();

        mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attrs);

        Area area = MockData.buildArea();
        area.setId(1);
        area.setName("Area 1");

        globalArea = repository.save(area);
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Save area")
    void saveArea() throws JsonProcessingException {
        Area value = MockData.buildArea();
        value.setName("Area 2");
        String body = objectMapper.writeValueAsString(value);

        webTestClient
                .post().uri("/area/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Area.class);
    }

    @Test
    @DisplayName("Get area with id 1")
    void getArea() {

        Area area = webTestClient
                .get().uri("/area/" + globalArea.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Area.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(area);
        assertEquals("Area 1", area.getName());
    }

    @Test
    @DisplayName("Dont get area with id 2")
    void dontGetArea() {
        ExceptionDetails exceptionDetails = webTestClient
                .get().uri("/area/2")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ExceptionDetails.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(exceptionDetails);
        assertEquals(404, exceptionDetails.getStatus());
        assertEquals("ResourceNotFoundException", exceptionDetails.getTitle());
        assertEquals(AREA_NOT_FOUND, exceptionDetails.getDetail());
    }

    @Test
    @DisplayName("Get all areas")
    void getAllAreas() {
        List<Area> area = webTestClient
                .get().uri("/area")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Area>>() {
                })
                .returnResult()
                .getResponseBody();

        assertNotNull(area);
        assertEquals(1, area.size());
        assertEquals("Area 1", area.get(0).getName());
    }

    @Test
    @DisplayName("Get area with name")
    void getAreaWithName() {
        Area area = webTestClient
                .get().uri("/area/byName/Area 1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Area.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(area);
        assertEquals("Area 1", area.getName());
    }

    @Test
    @DisplayName("Dont get area with name")
    void dontGetAreaWithName() {
        ExceptionDetails exceptionDetails = webTestClient
                .get().uri("/area/byName/Area 100")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ExceptionDetails.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(exceptionDetails);
        assertEquals(404, exceptionDetails.getStatus());
        assertEquals("ResourceNotFoundException", exceptionDetails.getTitle());
        assertEquals(AREA_NOT_FOUND, exceptionDetails.getDetail());
    }

    @Test
    @DisplayName("Update area")
    void updateArea() throws JsonProcessingException {
        Area value = MockData.buildArea();
        value.setName("Area 2");
        value.setId(globalArea.getId());
        String body = objectMapper.writeValueAsString(value);

        Area area = webTestClient
                .put().uri("/area/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Area.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(area);
        assertEquals("Area 2", area.getName());
    }

    @Test
    @DisplayName("Dont update area")
    void dontUpdateArea() throws JsonProcessingException {
        Area value = MockData.buildArea();
        value.setId(2);
        String body = objectMapper.writeValueAsString(value);

        ExceptionDetails details = webTestClient
                .put().uri("/area/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ExceptionDetails.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(details);
        assertEquals(AREA_NOT_FOUND, details.getDetail());
    }

    @Test
    @DisplayName("Delete area")
    void deleteArea() {

        webTestClient
                .delete().uri("/area/" + globalArea.getId() + "/delete")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class);
    }

    @Test
    @DisplayName("Dont update area")
    void dontDeleteArea() {

        ExceptionDetails details = webTestClient
                .delete().uri("/area/2/delete")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ExceptionDetails.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(details);
        assertEquals(AREA_NOT_FOUND, details.getDetail());
    }

    @Test
    @DisplayName("Search area")
    void searchArea() {
        List<Area> areas = webTestClient
                .get().uri(uriBuilder -> uriBuilder
                        .path("/area/search")
                        .queryParam("search", "Area")
                        .queryParam("startTimeStamp", LocalDateTime.now().plusMinutes(5))
                        .queryParam("endTimeStamp", LocalDateTime.now().plusHours(1))
                        .build()
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Area>>() {
                })
                .returnResult()
                .getResponseBody();

        assertNotNull(areas);
        assertEquals(1, areas.size());
        assertEquals("Area 1", areas.get(0).getName());
    }
}