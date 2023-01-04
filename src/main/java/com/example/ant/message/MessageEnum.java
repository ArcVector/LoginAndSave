package com.example.ant.message;

public enum MessageEnum {
    USERNAME_ALREADY_TAKEN("Error: Username is already taken!"),
    EMAIL_ALREADY_IN_USE("Error: Email is already in use!"),
    ROLE_NOT_FOUND("Error: Role not found."),
    USER_REGISTERED_SUCCESSFULLY("User registered successfully!"),
    SIGN_OUT("You've been signed out!"),
    USER_CONTENT("User content"),
    ADMIN_CONTENT("Admin content"),
    PUBLIC_CONTENT("Public content"),
    FILE_NOT_FOUND("File not found"),
    COULD_NOT_STORE_FILE("Could not store file"),
    FILE_CONTAINS_INVALID_CHARACTERS("File contains invalid characters"),
    USERNAME_NOT_FOUND("User Not Found with username: "),
    CANNOT_SET_USER_AUTHENTICATION("Cannot set user authentication: {}"),
    UNAUTHORIZED("Unauthorized"),

    ;
    String message;
     MessageEnum(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
