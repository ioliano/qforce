package nl.qnh.qforce.swapi;

import nl.qnh.qforce.domain.SWPerson;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

interface SWApiPersonInterface {

    @GetExchange("/{id}")
    Mono<SWPerson> searchPersonById(@PathVariable("id") long id);

    @GetExchange("?search={query}")
    Mono<SWApiResponse<SWPerson>> searchByCharacterName(@PathVariable("query") String query);

}
