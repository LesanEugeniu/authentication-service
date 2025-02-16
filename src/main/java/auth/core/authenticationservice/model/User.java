package auth.core.authenticationservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<LoginAttempt> loginAttempts = new ArrayList<>();

    public void addLoginAttempt(LoginAttempt loginAttempt) {
        loginAttempts.add(loginAttempt);
        loginAttempt.setUser(this);
    }

    public void removeLoginAttempt(LoginAttempt loginAttempt) {
        loginAttempts.remove(loginAttempt);
        loginAttempt.setUser(null);
    }
}
