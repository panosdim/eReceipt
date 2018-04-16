package eReceipt;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Apartment {
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty city;
    private final StringProperty floor;
    private final IntegerProperty id;

    public Apartment(String name, String address, String city, String floor, Integer id) {
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.floor = new SimpleStringProperty(floor);
        this.id = new SimpleIntegerProperty(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    @Override
    public String toString() {
        return name.get();
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public StringProperty cityProperty() {
        return city;
    }

    public String getFloor() {
        return floor.get();
    }

    public void setFloor(String floor) {
        this.floor.set(floor);
    }

    public StringProperty floorProperty() {
        return floor;
    }
}
