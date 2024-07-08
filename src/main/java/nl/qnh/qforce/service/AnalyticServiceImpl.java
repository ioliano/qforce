package nl.qnh.qforce.service;

import nl.qnh.qforce.domain.Analytics;
import nl.qnh.qforce.repo.AnalyticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnalyticServiceImpl implements AnalyticsService {

    private final AnalyticRepository analyticRepository;

    @Autowired
    public AnalyticServiceImpl(AnalyticRepository analyticRepository) {
        this.analyticRepository = analyticRepository;
    }

    @Override
    public void storeAnalytics(String requestName) {
        try{

        Optional<Analytics> analytics = analyticRepository.findById(requestName);

        Analytics newAnalytics;

        if (analytics.isEmpty()) {
            newAnalytics = new Analytics();
            newAnalytics.setRequestName(requestName);
            newAnalytics.setNumberOfTimesRequested(1L);
        } else {
            newAnalytics = analytics.get();
            newAnalytics.setNumberOfTimesRequested(newAnalytics.getNumberOfTimesRequested() + 1);
        }

        analyticRepository.save(newAnalytics);
        } catch (Exception e){
            //Catching everything as this is a secondary flow
            System.out.println("Exception while trying to save analytics");
        }
    }
}
