package com.url_shortner.shortner.repositories;

import com.url_shortner.shortner.enitities.QR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface QRRepository extends JpaRepository<QR, String> {
    @Query("select count(q) from QR q where q.user_id =:userId and q.createdAt<=:endDate and q.createdAt>=:startDate")
    Long getTotalQrsCreated(String userId, Timestamp startDate, Timestamp endDate);
}
