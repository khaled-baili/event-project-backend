package com.eventproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eventproject.dto.RegisterDto;
import com.eventproject.dto.ResetPasswordDto;
import com.eventproject.dto.UpdateCred;
import com.eventproject.dto.UpdatePasswordDto;
import com.eventproject.model.*;
import com.eventproject.model.actorModel.*;
import com.eventproject.repository.RoleRepo;
import com.eventproject.service.EmailService;
import com.eventproject.service.UserService;
import com.eventproject.utility.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/users")
    public ResponseEntity<List<User>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }



    @PostMapping ("/user/save")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse;
        if (userService.checkUserExist(registerDto.getEmail())) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"email is already exist");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        } else if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"Miss match password");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }  else if (!GenericValidator.isDate(registerDto.getBirthdate(), "dd-MM-yyyy", true)) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"No valid date provided");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        } else if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"Miss match Password");
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
        Sponsor sponsor;
        Visitor visitor;
        ProductOwner productOwner ;
        String randomCode;
        Mail email;
        Role role = roleRepo.findByName(registerDto.getRole());
        if (role == null) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"Try to sign up again");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
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
                            "http://localhost:8080/api/"+visitor.getVerificationCode());
                    //emailService.sendEmail(email);
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
                            "http://localhost:8080/api/"+randomCode);
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
                    sponsor.setAccountStatus(1);
                    sponsor.setEmployeeType(registerDto.getEmployeeType());
                    randomCode = RandomString.make(64);
                    sponsor.setVerificationCode(randomCode);
                    userService.saveUser(sponsor);
                    email = new Mail(
                            sponsor,
                            "Camelsoft","Sponsor Account activation","http://localhost:8080/api/");
                    //emailService.sendEmail(email);
                    break;
            }
            apiResponse = new ApiResponse(HttpStatus.OK,"User registered successfully Ple ase check Your e-mail");
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

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
        ApiResponse apiResponse;
        if(code==null) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"verify your code activation");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        } else if (userService.verifyCode(code)) {
            apiResponse = new ApiResponse(HttpStatus.OK,"Your account is activarted please login");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else
        {
            apiResponse = new ApiResponse(HttpStatus.FORBIDDEN,"Something go wrong");
            return new ResponseEntity<>(apiResponse, FORBIDDEN);
        }

    }
    @PostMapping("/forget-password")
    public ResponseEntity<?>resetPasswordToken(@RequestParam String email) {
        ApiResponse apiResponse;
        if (email == null) {
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND,"E-mail not exist");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } else {
            if (userService.updateResetToken(email,
                    "http://localhost:8080/api/forget-password?token=")) {
                apiResponse = new ApiResponse(HttpStatus.OK,"Check your E-mail for changing your password");
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            } else {
                apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"You are not able to reset the password");
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("/forget-password/reset-password")
    public ResponseEntity<Object>resetPassword(@RequestParam String token,
                                          @RequestBody ResetPasswordDto resetPasswordDto) {
        User user = userService.findUserByResetToken(token);
        ApiResponse apiResponse ;
        if (user == null) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"Please try again, wrong information");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        } else {
            String newPassword = bCryptPasswordEncoder.encode(resetPasswordDto.getPaasswordReset());
            user.setPassword(newPassword);
            user.setResetToken(null);
            userService.saveUser(user);
            apiResponse = new ApiResponse(HttpStatus.OK,"Password changed Successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/user/update-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
        User user = userService.getUser(updatePasswordDto.getEmail());
        if (user == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,"Your not able to modify password"),
                    HttpStatus.BAD_REQUEST);
        } else if(!updatePasswordDto.getPassword().equals(updatePasswordDto.getConfirmPassword())) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,"Miss match password"),
                    HttpStatus.BAD_REQUEST);
        } else if (!bCryptPasswordEncoder.matches(updatePasswordDto.getOldPassword(),user.getPassword())) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,"The old password provided is wrong"),
                    HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(updatePasswordDto.getPassword()));
            userService.saveUser(user);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK,"Password changed successfully"),
                    HttpStatus.OK);
        }
    }

    @PutMapping("/user/update-profile")
    public ResponseEntity<?> updateProfile(@RequestParam String email,
                                           @Valid @RequestBody UpdateCred updateCred) {
        User user = userService.getUser(email);
        ApiResponse apiResponse;
        if (user == null) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST,"Update operation is denied");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (!GenericValidator.isDate(updateCred.getBirthdate(), "dd-MM-yyyy", true)) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "No valid date provided");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        Role role = user.getRole();
        Visitor visitor;
        ProductOwner productOwner;
        Sponsor sponsor;
        switch (role.getName()) {
            case "ROLE_VISITOR":
                visitor = (Visitor) user;
                visitor.setFirstname(updateCred.getFirstname());
                visitor.setLastname(updateCred.getLastname());
                visitor.setTelnumber(updateCred.getTelnumber());
                visitor.setBirthdate(updateCred.getBirthdate());
                visitor.setOccupation(updateCred.getOccupation());
                userService.saveUser(visitor);
                break;
            case "ROLE_PRODUCT_OWNER":
                productOwner = (ProductOwner) user;
                productOwner.setFirstname(updateCred.getFirstname());
                productOwner.setLastname(updateCred.getLastname());
                productOwner.setTelnumber(updateCred.getTelnumber());
                productOwner.setBirthdate(updateCred.getBirthdate());
                productOwner.setEntrepriseRole(updateCred.getEntrepriseRole());
                userService.saveUser(productOwner);
                break;
            case "ROLE_SPONSOR":
                sponsor = (Sponsor) user;
                sponsor.setFirstname(updateCred.getFirstname());
                sponsor.setLastname(updateCred.getLastname());
                sponsor.setTelnumber(updateCred.getTelnumber());
                sponsor.setBirthdate(updateCred.getBirthdate());
                sponsor.setEmployeeType(updateCred.getEmployeeType());
                userService.saveUser(sponsor);
                break;
        }
        apiResponse = new ApiResponse(HttpStatus.OK,"You update successfully your profile");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }
}
