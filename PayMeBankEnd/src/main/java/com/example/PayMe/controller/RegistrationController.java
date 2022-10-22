package com.example.PayMe.controller;

import com.example.PayMe.models.RegistrationRequest;
import com.example.PayMe.security.JWT.JwtTokenUtils;
import com.example.PayMe.security.JWT.JwtUserDetailsService;
import com.example.PayMe.security.JWT.models.JwtRequest;
import com.example.PayMe.security.JWT.models.JwtResponse;
import com.example.PayMe.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)

@RequestMapping(path = "/api/v1/registration")
public class RegistrationController {
    private final RegistrationService registrationService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }



    //    @RequestMapping(value = "/authenticate")
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }



    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
        //return "registered";
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}