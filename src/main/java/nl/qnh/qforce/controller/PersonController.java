package nl.qnh.qforce.controller;

import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.service.AnalyticsService;
import nl.qnh.qforce.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final AnalyticsService analyticsService;

    public PersonController(AnalyticsService analyticsService,
                            PersonService personService) {
        this.analyticsService = analyticsService;
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personService.get(id);
        analyticsService.storeAnalytics("getPersonById");
        return person.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Person>> searchPersons(@RequestParam("q") String characterName) {
        if (characterName.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Person> persons = personService.search(characterName);
        analyticsService.storeAnalytics("searchPersons");
        if (persons.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(persons);
    }

}
