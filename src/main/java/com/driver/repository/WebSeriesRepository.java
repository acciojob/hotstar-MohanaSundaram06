package com.driver.repository;

import com.driver.model.SubscriptionType;
import com.driver.model.WebSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WebSeriesRepository extends JpaRepository<WebSeries,Integer> {

    WebSeries findBySeriesName(String seriesName);

//    @Query(value = "select count(id) from webSeries where age_limit >= ?1 and subscription_type = ?2", nativeQuery = true)
//    Integer getAvailableCountOfWebSeriesViewable(Integer age, SubscriptionType subscriptionType);
}
