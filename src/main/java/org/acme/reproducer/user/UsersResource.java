package org.acme.reproducer.user;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import org.acme.reproducer.auth.Roles;
import org.jboss.logging.Logger;

@Path("/")
public class UsersResource {
    private static final Logger log = Logger.getLogger(UsersResource.class);

    @Context
    SecurityIdentity securityIdentity;

    @POST
    @Path("/login")
    public Uni<String> login(@Valid LoginBody body) {
        return findOrCreateUser(body.email).map(user -> Jwt.upn(user.getName()).groups(user.role).sign());
    }

    private Uni<User> findOrCreateUser(String email) {
        return User.findByEmail(email).onFailure().recoverWithUni(() -> createUser(email));
    }

    @WithTransaction
    protected Uni<User> createUser(String email) {
        User user = new User();
        user.email = email;
        user.role = Roles.USER;
        return user.persist();
    }

    @GET
    @Path("/users/me")
    @Authenticated
    public User me() {
        return (User) securityIdentity.getPrincipal();
    }

    public record LoginBody(@NotNull @NotEmpty @Email @Size(max = 255) String email) {
    }
}
