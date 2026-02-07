package com.example.api.models.response;

public class LoginResponse {
    private String token;
    private String type;
    private String username;
    private String tokenType;
    
    public LoginResponse() {}
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
}
