package nl.qnh.qforce.repo;


import nl.qnh.qforce.domain.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticRepository extends JpaRepository<Analytics, String> {
}
