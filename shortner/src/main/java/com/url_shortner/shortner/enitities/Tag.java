package com.url_shortner.shortner.enitities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    private Set<URL> urls = new HashSet<>();
}
