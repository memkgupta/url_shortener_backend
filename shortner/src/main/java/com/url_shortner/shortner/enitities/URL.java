package com.url_shortner.shortner.enitities;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data

public class URL {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String url;
    @Column(unique = true)
    private String shortCode;
    private Timestamp created_at;
    private Timestamp expired_at;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "qr_id")
    private QR qr;
    @ManyToMany
    @JoinTable(
            name = "url_tags",
            joinColumns = @JoinColumn(name = "url_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    private String user_id;
    private long clicks;
}
