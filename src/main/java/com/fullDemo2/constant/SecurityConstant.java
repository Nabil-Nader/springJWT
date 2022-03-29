package com.fullDemo2.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 300_000; // 5 minutes expressed in milliseconds
    public static final long REFRESH_TOKEN = 210_000_0; // 35 minutes expressed in milliseconds

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String JWT_TOKEN_REFRESH_HEADER = "Jwt-Token-Refresh";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String GET_KIWE = "KIWE";
    public static final String GET_ARRAYS_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "/user/login", "/user/register","/test/**"};

//            "/user/add","/test/**","/user/refreshtoken","/user/student/**" };

    //public static final String[] PUBLIC_URLS = { "**" };

    public static final String ENCRYPTION_INSTANCE_NO_PADDING="AES/GCM/NoPadding";
    public static final String ENCRYPTION_INSTANCE="AES/ECB/PKCS5Padding";


    public static final int SECRET_KEY_SIZE = 128;
    public static final String ENCRYPTION_MODE_INSTANCE = "AES";
    public static final String HASHING_ALGORITHM = "SHA-256";


    public static final String SECRET_KEY_MESSAGE = "This is top Secret project! part2";


    public static final String INIT_VECTOR = "pqLOHUioK0QjhuvA";

    public static final String PRIVATE_KEY_FOR_AES = "abC2H19lkVbQDfkkxcrtNMQdd0FloLyp";

}
