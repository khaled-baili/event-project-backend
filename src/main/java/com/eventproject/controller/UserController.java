package com.eventproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eventproject.dto.ForgotPasswordDto;
import com.eventproject.dto.RegisterDto;
import com.eventproject.dto.ResetPasswordDto;
import com.eventproject.model.*;
import com.eventproject.repository.RoleRepo;
import com.eventproject.service.EmailService;
import com.eventproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
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
    private final EmailService emailService;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


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
        Mail email;
        Role role = roleRepo.findByName(registerDto.getRole());
        switch (role.getName()) {
            case "ROLE_VISITOR":
                visitor = new Visitor();
                visitor.setEmail(registerDto.getEmail());
                visitor.setFirstname(registerDto.getFirstname());
                visitor.setLastname(registerDto.getLastname());
                visitor.setTelnumber(registerDto.getTelnumber());
                visitor.setBirthdate(registerDto.getBirthdate());
                visitor.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
                visitor.setRole(role);
                visitor.setAccountStatus(1);
                visitor.setOccupation(registerDto.getOccupation());
                randomCode = RandomString.make(64);
                visitor.setVerificationCode(randomCode);
                userService.saveUser(visitor);
                email = new Mail(
                        visitor,"Camelsoft","Visitor Account activation",
                        "http://localhost:8080/api/");
                emailService.sendEmail(email);
                break;
            case "ROLE_PRODUCT_OWNER":
                productOwner = new ProductOwner();
                productOwner.setEmail(registerDto.getEmail());
                productOwner.setFirstname(registerDto.getFirstname());
                productOwner.setLastname(registerDto.getLastname());
                productOwner.setTelnumber(registerDto.getTelnumber());
                productOwner.setBirthdate(registerDto.getBirthdate());
                productOwner.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
                productOwner.setRole(role);
                productOwner.setAccountStatus(1);
                productOwner.setEntrepriseRole(registerDto.getEntrepriseRole());
                randomCode = RandomString.make(64);
                productOwner.setVerificationCode(randomCode);
                userService.saveUser(productOwner);
                email = new Mail(
                        productOwner,
                        "Camelsoft","Product Owner Account activation",
                        "http://localhost:8080/api/");
                emailService.sendEmail(email);
                break;
            case "ROLE_SPONSOR":
                sponsor = new Sponsor();
                sponsor.setEmail(registerDto.getEmail());
                sponsor.setFirstname(registerDto.getFirstname());
                sponsor.setLastname(registerDto.getLastname());
                sponsor.setTelnumber(registerDto.getTelnumber());
                sponsor.setBirthdate(registerDto.getBirthdate());
                sponsor.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
                sponsor.setRole(role);
                sponsor.setSponsorValidation(0);
                sponsor.setAccountStatus(1);
                sponsor.setEmployeeType(registerDto.getEmployeeType());
                randomCode = RandomString.make(64);
                sponsor.setVerificationCode(randomCode);
                userService.saveUser(sponsor);
                email = new Mail(
                        sponsor,
                        "Camelsoft","Sponsor Account activation","http://localhost:8080/api/");
                emailService.sendEmail(email);
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
    @GetMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestParam String code) {
        if(code==null) {
            return new ResponseEntity<>("verify your code activation", HttpStatus.BAD_REQUEST);
        } else if (userService.verifyCode(code)) {
            return new ResponseEntity<>("Your account is activarted please login", HttpStatus.OK);
        } else return new ResponseEntity<>("Something go wrong", FORBIDDEN);

    }
    @PostMapping("/forget-password")
    public ResponseEntity<?>resetPasswordToken(@RequestParam String email) {
        if (email == null) {
            return new ResponseEntity<>("E-mail not exist", HttpStatus.NOT_FOUND);
        } else {
            if (userService.updateResetToken(email,
                    "http://localhost:8080/api/forget-password?token=")) {
                return new ResponseEntity<>("Check your E-mail for changing your password", HttpStatus.OK);
            } else return new ResponseEntity<>("You are not able to reset the password", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/forget-password/reset-password")
    public ResponseEntity<Object>resetPassword(@RequestParam String token,
                                          @RequestBody ResetPasswordDto resetPasswordDto) {
        User user = userService.findUserByResetToken(token);
        ApiError apiResponse ;
        if (user == null) {
            apiResponse = new ApiError(HttpStatus.BAD_REQUEST,"Please try again, wrong information");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        } else {
            String newPassword = bCryptPasswordEncoder.encode(resetPasswordDto.getPaasswordReset());
            user.setPassword(newPassword);
            user.setResetToken(null);
            userService.saveUser(user);
            apiResponse = new ApiError(HttpStatus.OK,"Password changed Successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }

}
