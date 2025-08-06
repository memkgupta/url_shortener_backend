package com.url_shortner.user_service.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(
        name = "auth",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"auth_type", "provider_id"}),
                @UniqueConstraint(columnNames = {"auth_type", "email"})
        }
)
@Data
public class Auth {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    private AuthTypeEnum authType;

    private String oAuthProvider;

    @Column(name = "provider_id")
    private String provider_id;

    @Column(name = "email")
    private String email;

    private Timestamp created_at;
}
