package com.example.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Login_Response {
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("tokenType")
    private String tokenType;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("roles")
    private String[] roles;
    
    // Default constructor
    public Login_Response() {
    }
    
    // Parameterized constructor
    public Login_Response(String token, String tokenType, String username, String[] roles) {
        this.token = token;
        this.tokenType = tokenType;
        this.username = username;
        this.roles = roles;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String[] getRoles() {
        return roles;
    }
    
    public void setRoles(String[] roles) {
        this.roles = roles;
    }
    
    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}