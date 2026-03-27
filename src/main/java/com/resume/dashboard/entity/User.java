package com.resume.dashboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true, sparse = true)
    private String phoneNumber;

    private String password;

    @Indexed
    private String role;

    private UserStatus status;
    private boolean freePlanConsumed;
    private Instant freePlanConsumedAt;

    private Instant createdAt;
    private Instant lastLogin;

    public enum UserStatus {
        ACTIVE,
        BLOCKED
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public boolean isFreePlanConsumed() { return freePlanConsumed; }
    public void setFreePlanConsumed(boolean freePlanConsumed) { this.freePlanConsumed = freePlanConsumed; }

    public Instant getFreePlanConsumedAt() { return freePlanConsumedAt; }
    public void setFreePlanConsumedAt(Instant freePlanConsumedAt) { this.freePlanConsumedAt = freePlanConsumedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }
}
