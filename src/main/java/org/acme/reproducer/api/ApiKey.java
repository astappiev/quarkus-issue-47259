package org.acme.reproducer.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.security.credential.Credential;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.acme.reproducer.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Cacheable
@Table(name = "api_key")
public class ApiKey extends PanacheEntityBase implements Credential {
    public static final int LENGTH = 64;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    public User user;

    @NotEmpty
    @NotNull
    public String name;

    @Size(max = 512)
    public String url;

    @Size(max = 1024)
    public String description;

    @NotEmpty
    @NotNull
    public String apikey;

    @CreationTimestamp
    public Instant created;

    public ApiKey() {
        // required for Panache
    }

    public static ApiKey generate() {
        ApiKey apikey = new ApiKey();
        apikey.apikey = UUID.randomUUID().toString();
        return apikey;
    }

    public static Uni<List<ApiKey>> findByUser(User user) {
        return list("user.id", user.id);
    }

    public static Uni<ApiKey> findById(Object id, User user) {
        return find("id = ?1 and user.id = ?2", id, user.id).singleResult();
    }

    public static Uni<ApiKey> findByApikey(String apikey) {
        return find("apikey", apikey).singleResult();
    }
}
