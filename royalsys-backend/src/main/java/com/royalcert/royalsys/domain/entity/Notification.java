package com.royalcert.royalsys.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    private String link;

    @Column(name = "is_read")
    private boolean read;

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User u) { this.user = u; }
    public String getTitle() { return title; }
    public void setTitle(String s) { this.title = s; }
    public String getMessage() { return message; }
    public void setMessage(String s) { this.message = s; }
    public String getLink() { return link; }
    public void setLink(String s) { this.link = s; }
    public boolean isRead() { return read; }
    public void setRead(boolean r) { this.read = r; }
    public Instant getReadAt() { return readAt; }
    public void setReadAt(Instant i) { this.readAt = i; }
    public Instant getCreatedAt() { return createdAt; }
}
