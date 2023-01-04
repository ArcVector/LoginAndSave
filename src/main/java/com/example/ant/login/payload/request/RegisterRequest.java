package com.example.ant.login.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class RegisterRequest {
    private String username;

    @NotBlank
    @Email
    @Size(max = 75)
    private String email;
    private Set<String> roles;
   @NotBlank
   @Size(min = 8, max = 75)
    private String password;


}
