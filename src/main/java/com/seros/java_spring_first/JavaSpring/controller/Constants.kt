package com.seros.java_spring_first.JavaSpring.controller

object Constants {
    const val BASE_URL = "/api/v1"
    const val AUTH = "/auth"

    const val LOGIN = "/login"
    const val USERS = "/users"

    const val USER_ID = "/{id}"
    const val USER_ADD_ROLE = "/addRoles/{id}"
    const val USER_UPDATE_PASSWORD = "/updatePassword/{id}"
    const val USER_DELETE = "/delete/{id}"
    const val USER_RESET_PASSWORD = "/resetPassword/{id}"
    const val GET_ALL_USER_DATA = "/getAllUserData/{id}"


    const val ROLES = "/role"
    const val ROLE_ALL = "/all"
    const val ROLE_ID = "/{id}"

}