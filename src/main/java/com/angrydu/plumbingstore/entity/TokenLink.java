package com.angrydu.plumbingstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "token_links")
public class TokenLink {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(name = "token")
    private String token;

    @Column(name = "is_active")
    private boolean isActive;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenLink tokenLink = (TokenLink) o;
        return isActive == tokenLink.isActive && Objects.equals(id, tokenLink.id) && Objects.equals(token, tokenLink.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, isActive);
    }

    @Override
    public String toString() {
        return "TokenLink{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
