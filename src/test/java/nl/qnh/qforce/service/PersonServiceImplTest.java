package nl.qnh.qforce.service;

import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.domain.SWPerson;
import nl.qnh.qforce.swapi.SWApiRequester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class PersonServiceImplTest {

    @Mock
    private SWApiRequester apiRequester;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch() {
        //Checking two results
        String nameQuery = "Luke";
        SWPerson swPerson = new SWPerson();
        swPerson.setName("Luke Skywalker");

        SWPerson swPerson1 = new SWPerson();
        swPerson1.setName("Luke Skyrunner");

        when(apiRequester.searchByCharacterName(nameQuery))
                .thenReturn(List.of(swPerson, swPerson1));

        List<Person> result = personService.search(nameQuery);

        assertEquals(2, result.size());
        assertEquals("Luke Skywalker", result.getFirst().getName());
        assertEquals("Luke Skyrunner", result.getLast().getName());

        //Checking no results
        nameQuery = "Americano";

        when(apiRequester.searchByCharacterName(nameQuery))
                .thenReturn(List.of());

        result = personService.search(nameQuery);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGet() {
        long id = 1L;

        SWPerson swPerson = new SWPerson();
        swPerson.setName("Luke Skywalker");
        swPerson.setId(id);

        when(apiRequester.searchPersonById(id)).thenReturn(Optional.of(swPerson));

        Optional<Person> result = personService.get(id);

        assertTrue(result.isPresent());
        assertEquals("Luke Skywalker", result.get().getName());
    }
}