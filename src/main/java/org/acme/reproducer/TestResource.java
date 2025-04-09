package org.acme.reproducer;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import org.acme.reproducer.api.ApiKey;
import org.acme.reproducer.auth.Roles;

@Path("/test")
@RolesAllowed({Roles.APPLICATION})
public class TestResource {

    @Context
    SecurityIdentity securityIdentity;

    @POST
    @Path("/1")
    public Uni<String> test1() {
        ApiKey apikey = securityIdentity.getCredential(ApiKey.class);
        return Uni.createFrom().item("it works!");
    }

    @POST
    @Path("/2")
    public Uni<String> test2(Fruit fruit) {
        ApiKey apikey = securityIdentity.getCredential(ApiKey.class);
        return Uni.createFrom().item("it works too!");
    }

    @RegisterForReflection
    public static class Fruit {
        public String name;
        public String description;

        public Fruit() {
        }

        public Fruit(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
