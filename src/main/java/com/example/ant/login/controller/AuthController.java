package com.example.ant.login.controller;

import com.example.ant.message.MessageEnum;
import com.example.ant.login.model.Role;
import com.example.ant.login.model.RoleEnum;
import com.example.ant.login.model.User;
import com.example.ant.login.payload.request.LoginRequest;
import com.example.ant.login.payload.request.RegisterRequest;
import com.example.ant.login.payload.response.MessageResponse;
import com.example.ant.login.payload.response.UserResponse;
import com.example.ant.login.repository.RoleRepository;
import com.example.ant.login.repository.UserRepository;
import com.example.ant.login.security.jwt.JwtUtils;
import com.example.ant.login.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
   private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
   private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUserName(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.USERNAME_ALREADY_TAKEN.getMessage()));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.EMAIL_ALREADY_IN_USE.getMessage()));
        }
        // Create new user's account
        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        Set<String> rolesNamed = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (rolesNamed == null) {
            Role userRole = roleRepository.findByName(RoleEnum.USER)
                    .orElseThrow(() -> new RuntimeException(MessageEnum.ROLE_NOT_FOUND.getMessage()));
            roles.add(userRole);
        } else {
            rolesNamed.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN)
                                .orElseThrow(() -> new RuntimeException(MessageEnum.ROLE_NOT_FOUND.getMessage()));
                        roles.add(adminRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName(RoleEnum.USER)
                                .orElseThrow(() -> new RuntimeException(MessageEnum.ROLE_NOT_FOUND.getMessage()));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.USER_REGISTERED_SUCCESSFULLY.getMessage()));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse(MessageEnum.SIGN_OUT.getMessage()));
    }
}

