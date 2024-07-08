package nl.qnh.qforce.service;

/**
 * Used to manage Request Analytics Logic
 * */
public interface AnalyticsService {


    /**
     * Searches for persons.
     *
     * @param requestName the name of the Request of the Rest controller
     * @return the list of persons
     */
    void storeAnalytics(String requestName);
}
