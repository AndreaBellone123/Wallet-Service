package com.devied.walletservice.identity;

public class IdentityRole {

    public static final String ROLE_ANONYMOUS = "ANONYMOUS";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    public static final String AUTHORITY_ANONYMOUS = "ROLE_" + ROLE_ANONYMOUS;
    public static final String AUTHORITY_USER = "ROLE_" + ROLE_USER;
    public static final String AUTHORITY_ADMIN = "ROLE_" + ROLE_ADMIN;

}
