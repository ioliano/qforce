package nl.qnh.qforce.service;
import static org.mockito.Mockito.*;

import nl.qnh.qforce.domain.Analytics;
import nl.qnh.qforce.repo.AnalyticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class AnalyticServiceImplTest {

    @Mock
    private AnalyticRepository analyticRepository;

    @InjectMocks
    private AnalyticServiceImpl analyticService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStoreAnalyticsNewEntry() {
        String requestName = "testRequest";
        when(analyticRepository.findById(requestName)).thenReturn(Optional.empty());

        analyticService.storeAnalytics(requestName);

        verify(analyticRepository, times(1))
                .save(argThat(analytics -> analytics.getRequestName().equals(requestName) &&
                        analytics.getNumberOfTimesRequested() == 1L
        ));
    }

    @Test
    void testStoreAnalyticsExistingEntry() {
        String requestName = "testRequest";
        Analytics existingAnalytics = new Analytics();
        existingAnalytics.setRequestName(requestName);
        existingAnalytics.setNumberOfTimesRequested(6L);

        when(analyticRepository.findById(requestName)).thenReturn(Optional.of(existingAnalytics));

        analyticService.storeAnalytics(requestName);

        verify(analyticRepository, times(1)).save(argThat(analytics ->
                analytics.getRequestName().equals(requestName) &&
                        analytics.getNumberOfTimesRequested() == 7L
        ));
    }
}