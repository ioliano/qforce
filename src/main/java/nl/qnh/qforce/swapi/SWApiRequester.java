package nl.qnh.qforce.swapi;

import nl.qnh.qforce.domain.SWMovie;
import nl.qnh.qforce.domain.SWPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SWApiRequester {

    private final SWApiPersonInterface swApiPersonInterface;
    private final WebClient webClient;

    @Autowired
    public SWApiRequester(@Value("${dev.swapi.url}") String url) {
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(webClient))
                .build();

        this.swApiPersonInterface = factory.createClient(SWApiPersonInterface.class);
    }

    public Optional<SWPerson> searchPersonById(long id) {
        Optional<SWPerson> personOptional = swApiPersonInterface.searchPersonById(id).blockOptional();
        personOptional.ifPresent(p -> {
            this.populateMovies(p);
            //Id is not coming in the json
            p.setId(id);
        });
        return personOptional;
    }

    public List<SWPerson> searchByCharacterName(String query) {
        List<SWPerson> persons = swApiPersonInterface.searchByCharacterName(query)
                .map(SWApiResponse::getResults)
                .block();

        if (persons != null) {
            persons.forEach(this::populateMovies);
        }

        return persons;
    }

    private void populateMovies(SWPerson person) {
        List<SWMovie> movies = person.getMoviesUrl().stream()
                .map(this::getMovieByUrl)
                .collect(Collectors.toList());

        person.setSwMovies(movies);
    }


    private SWMovie getMovieByUrl(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(SWMovie.class)
                .block();
    }
}
