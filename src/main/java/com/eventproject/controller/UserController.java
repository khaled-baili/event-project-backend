package com.eventproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eventproject.dto.RegisterDto;
import com.eventproject.model.*;
import com.eventproject.repository.RoleRepo;
import com.eventproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/users")
    public ResponseEntity<List<User>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping ("/user/save")
    public ResponseEntity<?>saveUser(@RequestBody RegisterDto registerDto) {
        if (userService.checkUserExist(registerDto.getEmail())) {
            return new ResponseEntity<>("email is already exist", HttpStatus.BAD_REQUEST);
        }
        Sponsor sponsor;
        Visitor visitor;
        ProductOwner productOwner;
        String randomCode;
        Role role = roleRepo.findByName(registerDto.getRole());
        switch (role.getName()) {
            case "ROLE_VISITOR":
                visitor = new Visitor();
                visitor.setEmail(registerDto.getEmail());
                visitor.setFirstname(registerDto.getFirstname());
                visitor.setLastname(registerDto.getLastname());
                visitor.setTelnumber(registerDto.getTelnumber());
                visitor.setBirthdate(registerDto.getBirthdate());
                visitor.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                visitor.setRole(role);
                visitor.setAccountStatus(1);
                visitor.setOccupation(registerDto.getOccupation());
                randomCode = RandomString.make(64);
                visitor.setVerificationCode(randomCode);
                userService.saveUser(visitor);
                userService.sendVerficationEmail(visitor,"http://localhost:8080/api/");
                break;
            case "ROLE_PRODUCT_OWNER":
                productOwner = new ProductOwner();
                productOwner.setEmail(registerDto.getEmail());
                productOwner.setFirstname(registerDto.getFirstname());
                productOwner.setLastname(registerDto.getLastname());
                productOwner.setTelnumber(registerDto.getTelnumber());
                productOwner.setBirthdate(registerDto.getBirthdate());
                productOwner.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                productOwner.setRole(role);
                productOwner.setAccountStatus(1);
                productOwner.setEntrepriseRole(registerDto.getEntrepriseRole());
                randomCode = RandomString.make(64);
                productOwner.setVerificationCode(randomCode);
                userService.saveUser(productOwner);
                userService.sendVerficationEmail(productOwner,"http://localhost:8080/api/");
                break;
            case "ROLE_SPONSOR":
                sponsor = new Sponsor();
                sponsor.setEmail(registerDto.getEmail());
                sponsor.setFirstname(registerDto.getFirstname());
                sponsor.setLastname(registerDto.getLastname());
                sponsor.setTelnumber(registerDto.getTelnumber());
                sponsor.setBirthdate(registerDto.getBirthdate());
                sponsor.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                sponsor.setRole(role);
                sponsor.setSponsorValidation(0);
                sponsor.setAccountStatus(1);
                sponsor.setEmployeeType(registerDto.getEmployeeType());
                randomCode = RandomString.make(64);
                sponsor.setVerificationCode(randomCode);
                userService.saveUser(sponsor);
                userService.sendVerficationEmail(sponsor,"http://localhost:8080/api");
                break;
        }
        return new ResponseEntity<>("User registered successfully Please check Your e-mail"
                , HttpStatus.OK);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                User user = userService.getUser(email);
                String access_token = JWT.create().withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60*  1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("role",user.getRole().getName()).sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                log.error("Error loging in : {} ", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh Token is missing");
        }
    }
    @GetMapping("/verify/{code}")
    public ResponseEntity<?> verifyCode(@PathVariable String code) {
        if(code==null) {
            return new ResponseEntity<>("verify your code activation", HttpStatus.BAD_REQUEST);
        } else if (userService.verifyCode(code)) {
            return new ResponseEntity<>("Your account is activarted please login", HttpStatus.OK);
        } else return new ResponseEntity<>("Something go wrong", FORBIDDEN);

    }
}
