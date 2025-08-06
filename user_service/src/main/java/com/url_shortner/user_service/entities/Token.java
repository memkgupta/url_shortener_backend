package com.url_shortner.user_service.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Token {
    @Id
    @GeneratedValue
    private long id;

    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType type;
    private Timestamp expires;
    @ManyToOne
    private Auth auth;
    private Timestamp created;

}
