package org.example.johnsupermarket.Models;
import javafx.beans.property.SimpleStringProperty;

public class Dealer {
    private final SimpleStringProperty name;
    private final SimpleStringProperty contact;
    private final SimpleStringProperty location;

    public Dealer(String name, String contact, String location) {
        this.name = new SimpleStringProperty(name);
        this.contact = new SimpleStringProperty(contact);
        this.location = new SimpleStringProperty(location);
    }

    public String getName() { return name.get(); }
    public String getContact() { return contact.get(); }
    public String getLocation() { return location.get(); }

    public void setName(String value) { name.set(value); }
    public void setContact(String value) { contact.set(value); }
    public void setLocation(String value) { location.set(value); }
}
