package org.acme.reproducer.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.acme.reproducer.auth.Roles;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.security.Principal;
import java.time.Instant;

@Entity
@Cacheable
@Table(name = "user")
public class User extends PanacheEntityBase implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Email
    @NotEmpty
    @NotNull
    public String email;

    @NotNull
    public String role = Roles.USER;

    protected User() {
    }

    public static Uni<User> findByEmail(String name) {
        return find("email", name).singleResult();
    }

    @Override
    @JsonIgnore
    public String getName() {
        return email;
    }
}
