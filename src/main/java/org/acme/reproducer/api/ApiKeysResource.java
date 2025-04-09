package org.acme.reproducer.api;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import org.acme.reproducer.auth.Roles;
import org.acme.reproducer.user.User;

import java.util.List;

@Path("/api_keys")
@Authenticated
public class ApiKeysResource {

    @Context
    SecurityIdentity securityIdentity;

    @GET
    @RolesAllowed({Roles.USER, Roles.ADMIN})
    public Uni<List<ApiKey>> list() {
        return ApiKey.findByUser((User) securityIdentity.getPrincipal());
    }

    @POST
    @WithTransaction
    @RolesAllowed({Roles.USER, Roles.ADMIN})
    public Uni<ApiKey> create(@Valid CreateToken model) {
        ApiKey apikey = ApiKey.generate();
        apikey.name = model.name;
        apikey.url = model.url;
        apikey.description = model.description;
        apikey.user = (User) securityIdentity.getPrincipal();
        return apikey.persist();
    }

    @DELETE
    @WithTransaction
    public Uni<Void> delete(@QueryParam("id") Long id, @QueryParam("apikey") String apikey) {
        Uni<ApiKey> item;
        User user = (User) securityIdentity.getPrincipal();
        if (id != null && user != null) {
            item = ApiKey.findById(id, user);
        } else {
            item = ApiKey.findByApikey(apikey);
        }

        return item.onItem().ifNotNull().call(PanacheEntityBase::delete).replaceWithVoid();
    }

    public record CreateToken(@NotNull @NotEmpty @Size(max = 255) String name, @Size(max = 512) String url, @Size(max = 1024) String description) {
    }
}
