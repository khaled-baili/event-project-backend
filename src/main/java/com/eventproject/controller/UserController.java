package com.eventproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eventproject.RoleEnum;
import com.eventproject.dto.RegisterDto;
import com.eventproject.model.*;
import com.eventproject.repository.RoleRepo;
import com.eventproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.print.attribute.standard.Media;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
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
                visitor.setAccountStatus(0);
                visitor.setAccountverfication(0);
                visitor.setOccupation(registerDto.getOccupation());
                userService.saveVisitor(visitor);
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
                productOwner.setAccountStatus(0);
                productOwner.setAccountverfication(0);
                productOwner.setEntrepriseRole(registerDto.getEntrepriseRole());
                userService.saveUser(productOwner);
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
                sponsor.setAccountStatus(0);
                sponsor.setAccountverfication(0);
                sponsor.setEmployeeType(registerDto.getEmployeeType());
                userService.saveUser(sponsor);
                break;
        }
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
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
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh Token is missing");
        }
    }


//    @GetMapping("/users")
//    public ResponseEntity<List<User>>getUsers() {
//        return ResponseEntity.ok().body(userService.getUsers());
//    }
}
