package com.seros.java_spring_first.JavaSpring.utils;

public class Constants {
    public static final String BASE_URL = "/api/v1";
    public static final String AUTH = "/auth";
    public static final String SUCCESS = "/success";

    public static final String CONFIG_REQUEST_MATCHERS = "/api/v1/auth/**";
    public static final String CONFIG_SUCCESS = "/api/v1/auth/success";
    public static final String CONFIG_FAILURE = "/api/v1/auth/failure";

    public static final String LOGIN = "/login";
    public static final String MOBILE_LOGIN = "mobile/login";
    public static final String SIGN_UP = "/signup";
    public static final String MOBILE_SIGN_UP = "mobile/signup";
    public static final String LOGOUT = "/logout";
    public static final String FORGET_PASSWORD = "/forgetPassword";

    public static final String USERS = "/users";
    public static final String USER_ID = "/{id}";
    public static final String USER_ADD_ROLE = "/addRoles/{id}";
    public static final String USER_UPDATE_PASSWORD = "/updatePassword/{id}";
    public static final String USER_RESET_PASSWORD = "/resetPassword/{id}";
    public static final String GET_ALL_USER_DATA = "/getAllUserData/{id}";

    public static final String ROLES = "/role";
    public static final String ROLE_ALL = "/all";
    public static final String ROLE_ID = "/{id}";
}
