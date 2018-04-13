package ereceipt;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Lessee {
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty city;
    private final StringProperty postalCode;
    private final StringProperty mobile;
    private final StringProperty homeTelephone;
    private final StringProperty workTelephone;
    private final StringProperty email;
    private final ObjectProperty<LocalDate> contractFrom;
    private final ObjectProperty<LocalDate> contractUntil;
    private final ObjectProperty<Apartment> apartment;
    private final IntegerProperty rent;
    private final IntegerProperty id;

    public Lessee(String name,
                  String address,
                  String city,
                  String postalCode,
                  String mobile,
                  String homeTelephone,
                  String workTelephone,
                  String email,
                  Apartment apartment,
                  LocalDate contractFrom,
                  LocalDate contractUntil,
                  Integer rent,
                  Integer id) {
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.mobile = new SimpleStringProperty(mobile);
        this.homeTelephone = new SimpleStringProperty(homeTelephone);
        this.workTelephone = new SimpleStringProperty(workTelephone);
        this.email = new SimpleStringProperty(email);
        this.contractFrom = new SimpleObjectProperty<>(contractFrom);
        this.contractUntil = new SimpleObjectProperty<>(contractUntil);
        this.apartment = new SimpleObjectProperty<>(apartment);
        this.rent = new SimpleIntegerProperty(rent);
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

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public StringProperty cityProperty() {
        return city;
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public String getMobile() {
        return mobile.get();
    }

    public void setMobile(String mobile) {
        this.mobile.set(mobile);
    }

    public StringProperty mobileProperty() {
        return mobile;
    }

    public String getHomeTelephone() {
        return homeTelephone.get();
    }

    public void setHomeTelephone(String homeTelephone) {
        this.homeTelephone.set(homeTelephone);
    }

    public StringProperty homeTelephoneProperty() {
        return homeTelephone;
    }

    public String getWorkTelephone() {
        return workTelephone.get();
    }

    public void setWorkTelephone(String workTelephone) {
        this.workTelephone.set(workTelephone);
    }

    public StringProperty workTelephoneProperty() {
        return workTelephone;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public Apartment getApartment() {
        return apartment.get();
    }

    public void setApartment(Apartment apartment) {
        this.apartment.set(apartment);
    }

    public ObjectProperty<Apartment> apartmentProperty() {
        return apartment;
    }

    public LocalDate getContractFrom() {
        return contractFrom.get();
    }

    public void setContractFrom(LocalDate contractFrom) {
        this.contractFrom.set(contractFrom);
    }

    public final String getContractFromSQL() {
        return contractFrom.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public ObjectProperty<LocalDate> contractFromProperty() {
        return contractFrom;
    }

    public LocalDate getContractUntil() {
        return contractUntil.get();
    }

    public void setContractUntil(LocalDate contractUntil) {
        this.contractUntil.set(contractUntil);
    }

    public final String getContractUntilSQL() {
        return contractUntil.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public ObjectProperty<LocalDate> contractUntilProperty() {
        return contractUntil;
    }

    public int getRent() {
        return rent.get();
    }

    public void setRent(int rent) {
        this.rent.set(rent);
    }

    public IntegerProperty rentProperty() {
        return rent;
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
}
