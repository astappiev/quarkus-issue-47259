package org.acme.reproducer.auth;

public final class Roles {
    private Roles() {
    }

    public static final String USER = "User"; // Auth by username and password
    public static final String APPLICATION = "Application"; // Auth by api key
}
