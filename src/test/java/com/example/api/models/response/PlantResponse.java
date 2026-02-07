package com.example.api.models.response;

public class PlantResponse {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private Category category;
    
    public static class Category {
        private int id;
        private String name;
        private Object[] subCategories;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Object[] getSubCategories() { return subCategories; }
        public void setSubCategories(Object[] subCategories) { this.subCategories = subCategories; }
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
