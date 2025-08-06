package com.url_shortner.shortner.enitities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class QR {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToOne(fetch = FetchType.LAZY)
    private URL url;
    private Timestamp createdAt;
    private String user_id;
    private long scans;
}
