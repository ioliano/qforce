package nl.qnh.qforce.domain;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class SWPerson implements Person {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("height")
    private String height;

    @JsonAlias({"mass"})
    @JsonProperty("weight")
    private String weight;

    @JsonProperty("birth_year")
    private String birthYear;

    @JsonProperty("gender")
    private SWGender gender;

    @JsonAlias({"films", "moviesUrl"})
    //Ensuring it's written but not sent to the client by the controller
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @JsonIgnore
    private List<String> moviesUrl;

    @JsonIgnore
    private List<SWMovie> swMovies;

    @Override
    public Integer getWeight() {
        try {
            return Integer.parseInt(weight);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public Integer getHeight() {
        try {
            return Integer.parseInt(height);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public Gender getGender() {
        return Gender.valueOf(gender.toString());
    }

    @Override
    //Hack to solve inheritance issue
    public List<Movie> getMovies() {
        return (List<Movie>) (List<?>) swMovies;
    }
}
