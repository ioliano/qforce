package nl.qnh.qforce.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


/**
 * Analytics for controllers endpoint.
 */
@Data
@Entity
public class Analytics {

    @Id
    String requestName;
    long numberOfTimesRequested;
}
