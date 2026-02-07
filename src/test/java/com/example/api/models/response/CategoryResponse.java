package com.example.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryResponse {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("parentCategory")
    private String parentCategory;
    
    @JsonProperty("isMainCategory")
    private Boolean isMainCategory;
    
    // Default constructor
    public CategoryResponse() {
    }
    
    // Parameterized constructor
    public CategoryResponse(Long id, String name, String parentCategory, Boolean isMainCategory) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
        this.isMainCategory = isMainCategory;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getParentCategory() {
        return parentCategory;
    }
    
    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }
    
    public Boolean getIsMainCategory() {
        return isMainCategory;
    }
    
    public void setIsMainCategory(Boolean isMainCategory) {
        this.isMainCategory = isMainCategory;
    }
    
    @Override
    public String toString() {
        return "CategoryResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentCategory='" + parentCategory + '\'' +
                ", isMainCategory=" + isMainCategory +
                '}';
    }
}
