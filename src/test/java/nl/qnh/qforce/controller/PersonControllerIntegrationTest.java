package nl.qnh.qforce.controller;

import nl.qnh.qforce.domain.Analytics;
import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.domain.SWPerson;
import nl.qnh.qforce.repo.AnalyticRepository;
import nl.qnh.qforce.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PersonControllerIntegrationTest {

    @Autowired
    private PersonController personController;

    @MockBean
    private PersonService personService;

    @Autowired
    private AnalyticRepository analyticsRepository;

    private static final String NOTFOUND = "404 NOT_FOUND";
    private static final String OK = "200 OK";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        analyticsRepository.deleteAll();  // Clear the database before each test
    }

    @Test
    void testGetPersonByIdPersonExists() {
        long id = 1L;
        SWPerson swPerson = new SWPerson();
        swPerson.setId(id);
        swPerson.setName("Luke Skywalker");

        when(personService.get(id)).thenReturn(Optional.of(swPerson));

        ResponseEntity<Person> response = personController.getPersonById(id);

        assertEquals(OK, response.getStatusCode().toString());
        assertEquals("Luke Skywalker", Objects.requireNonNull(response.getBody()).getName());

        verify(personService, times(1)).get(id);

        // Verify that analyticsService.storeAnalytics was called
        Analytics analytics = analyticsRepository.findById("getPersonById").orElse(null);
        assertNotNull(analytics);
        assertEquals(1L, analytics.getNumberOfTimesRequested());
    }

    @Test
    void testGetPersonByIdPersonDoesNotExist() {
        long id = 1L;

        when(personService.get(id)).thenReturn(Optional.empty());

        ResponseEntity<Person> response = personController.getPersonById(id);

        assertEquals(NOTFOUND, response.getStatusCode().toString());

        verify(personService, times(1)).get(id);

        // Verify that analyticsService.storeAnalytics was called
        Analytics analytics = analyticsRepository.findById("getPersonById").orElse(null);
        assertNotNull(analytics);
        assertEquals(1L, analytics.getNumberOfTimesRequested());

        response = personController.getPersonById(id);

        assertEquals(NOTFOUND, response.getStatusCode().toString());

        analytics = analyticsRepository.findById("getPersonById").orElse(null);
        assertNotNull(analytics);
        assertEquals(2L, analytics.getNumberOfTimesRequested());
    }

    @Test
    void testSearchPersonsPersonsFound() {
        String query = "Luke";
        SWPerson swPerson = new SWPerson();
        swPerson.setId(1L);
        swPerson.setName("Luke Skywalker");


        SWPerson swPerson1 = new SWPerson();
        swPerson1.setId(512123L);
        swPerson1.setName("Luke Skyrunner");

        when(personService.search(query)).thenReturn(List.of(swPerson, swPerson1));

        ResponseEntity<List<Person>> response = personController.searchPersons(query);

        assertEquals(OK, response.getStatusCode().toString());
        assertEquals("Luke Skywalker", Objects.requireNonNull(response.getBody()).getFirst().getName());

        verify(personService, times(1)).search(query);

        // Verify that analyticsService.storeAnalytics was called
        Analytics analytics = analyticsRepository.findById("searchPersons").orElse(null);
        assertNotNull(analytics);
        assertEquals(1L, analytics.getNumberOfTimesRequested());
    }

    @Test
    void testSearchPersonsNoPersonsFound() {
        String query = "Luke";

        when(personService.search(query)).thenReturn(List.of());

        ResponseEntity<List<Person>> response = personController.searchPersons(query);

        assertEquals(NOTFOUND, response.getStatusCode().toString());

        verify(personService, times(1)).search(query);

        // Verify that analyticsService.storeAnalytics was called
        Analytics analytics = analyticsRepository.findById("searchPersons").orElse(null);
        assertNotNull(analytics);
        assertEquals(1L, analytics.getNumberOfTimesRequested());
    }

    @Test
    void testSearchPersonsEmptyQuery() {
        String query = "";

        ResponseEntity<List<Person>> response = personController.searchPersons(query);

        assertEquals("404 NOT_FOUND", response.getStatusCode().toString());

        // Verify that analyticsService.storeAnalytics was called
        Analytics analytics = analyticsRepository.findById("searchPersons").orElse(null);
        assertNotNull(analytics);
        assertEquals(1L, analytics.getNumberOfTimesRequested());

        response = personController.searchPersons(query);

        assertEquals(NOTFOUND, response.getStatusCode().toString());

        //Check that the 2nd call increments
        analytics = analyticsRepository.findById("searchPersons").orElse(null);
        assertNotNull(analytics);
        assertEquals(2L, analytics.getNumberOfTimesRequested());
    }
}