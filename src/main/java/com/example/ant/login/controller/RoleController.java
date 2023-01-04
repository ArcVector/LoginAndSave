package com.example.ant.login.controller;

import com.example.ant.message.MessageEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/role")
public class RoleController {
    @GetMapping("/all")
    public String allAccess() {
        return MessageEnum.PUBLIC_CONTENT.getMessage();
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return MessageEnum.USER_CONTENT.getMessage();
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return MessageEnum.ADMIN_CONTENT.getMessage();
    }
}
