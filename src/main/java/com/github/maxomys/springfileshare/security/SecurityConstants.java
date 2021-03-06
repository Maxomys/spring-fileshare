package com.github.maxomys.springfileshare.security;

public class SecurityConstants {

    public static final String SECRET = "secretKey";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final long TOKEN_EXPIRATION_TIME = 30 * 60 * 1000; //30 min
    public static final long TOKEN_EXPIRATION_TIME_REFRESH = 24 * 60 * 60 * 1000; //24h

}
