package nl.qnh.qforce.swapi;

import lombok.Data;

import java.util.List;

@Data
public class SWApiResponse<T> {

    private int count;
    private String next;
    private String previous;
    private List<T> results;
}
