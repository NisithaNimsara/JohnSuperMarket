package org.example.johnsupermarket.Models;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DealerItem {
    private final SimpleStringProperty name;
    private final SimpleStringProperty brand;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty category;

    public DealerItem(String name, String brand, double price, int quantity, String category) {
        this.name = new SimpleStringProperty(name);
        this.brand = new SimpleStringProperty(brand);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.category = new SimpleStringProperty(category);
    }

    public String getName() { return name.get(); }
    public String getBrand() { return brand.get(); }
    public double getPrice() { return price.get(); }
    public int getQuantity() { return quantity.get(); }
    public String getCategory() { return category.get(); }
}
