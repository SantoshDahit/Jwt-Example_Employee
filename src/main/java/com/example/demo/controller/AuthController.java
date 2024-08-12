package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.model.JwtRequest;
import com.example.demo.model.JwtResponse;
import com.example.demo.security.JwtHelper;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/register")
    public String addEmployee(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
        return ("Employee registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@RequestBody JwtRequest jwtRequest){
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);

        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e){
            throw  new BadCredentialsException("Invalid Username or password");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){
        return "Credentials Invalid!!";
    }
}
