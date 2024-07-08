package nl.qnh.qforce.service;

import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<Person> search(String query) {
        return List.of();
    }

    @Override
    public Optional<Person> get(long id) {
        return personRepository.findById(id);
    }
}
