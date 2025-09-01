package org.example.johnsupermarket.Models;
import java.math.BigDecimal;
import java.time.LocalDate;
public class Item {
    private String code;
    private String name;
    private String brand;
    private BigDecimal price;
    private int quantity;
    private String category;
    private LocalDate date;
    private int Threshold;
    private String imagePath;

    public Item(String code, String name, String brand, BigDecimal price, int quantity,
                String category, LocalDate date, int Threshold, String imagePath) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.date = date;
        this.Threshold = Threshold;
        this.imagePath = imagePath;
    }

    public Item(String name, String brand, BigDecimal price, int quantity, String category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }
    //------------------------------------------------Getters
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public String getBrand() {
        return brand;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getCategory() {
        return category;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getImagePath() {
        return imagePath;
    }
    public int getThreshold() {
        return Threshold;
    }
    //-------------------------------------------------Setters
    public void setCode(String code) {this.code = code;}
    public void setName(String name) {this.name = name;}
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setThreshold(int Threshold) {this.Threshold = Threshold;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
    @Override
    public String toString() {
        return code + "," + name + "," + brand + "," + price + "," + quantity + "," + category + "," + date + "," + Threshold + "," + imagePath;
    }
}


