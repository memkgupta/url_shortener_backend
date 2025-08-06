package com.url_shortner.user_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "_user")
@Data
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String name;
    private String email;
    private String profile;
    private String phoneNo;
    private String role;
    private String password;
}
