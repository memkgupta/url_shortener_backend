package com.url_shortner.shortner.repositories;

import com.url_shortner.shortner.enitities.URL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<URL, String>, JpaSpecificationExecutor<URL> {
    Optional<URL> findByShortCode(String code);

    @Query("select coalesce(sum(u.clicks), 0) from URL u where u.user_id = :userId and u.created_at >= :startDate and u.created_at <= :endDate")
    Long getTotalClicks(Timestamp startDate, Timestamp endDate, String userId);

    @Query("select count(u) from  URL u where u.user_id = :userId and u.created_at >=:startDate and u.created_at <=:endDate")
    Long getTotalUrlsCreated(Timestamp startDate, Timestamp endDate, String userId);


}
