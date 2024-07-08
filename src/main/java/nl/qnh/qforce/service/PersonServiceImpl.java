package nl.qnh.qforce.service;

import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.domain.SWPerson;
import nl.qnh.qforce.swapi.SWApiRequester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    private final SWApiRequester apiRequester;

    @Autowired
    public PersonServiceImpl(SWApiRequester apiRequester) {
        this.apiRequester = apiRequester;
    }


    @Override
    public List<Person> search(String nameQuery) {
        List<SWPerson> starWarsPeople = apiRequester.searchByCharacterName(nameQuery);

        return starWarsPeople.stream()
                .map(Person.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Person> get(long id) {
        Optional<SWPerson> starWarsPerson = apiRequester.searchPersonById(id);
        //Id is not coming in the json
        starWarsPerson.ifPresent(swPerson -> swPerson.setId(id));

        return starWarsPerson.map(Person.class::cast);
    }
}
