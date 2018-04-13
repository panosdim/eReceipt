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

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getMobile() {
        return mobile.get();
    }

    public StringProperty mobileProperty() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile.set(mobile);
    }

    public String getHomeTelephone() {
        return homeTelephone.get();
    }

    public StringProperty homeTelephoneProperty() {
        return homeTelephone;
    }

    public void setHomeTelephone(String homeTelephone) {
        this.homeTelephone.set(homeTelephone);
    }

    public String getWorkTelephone() {
        return workTelephone.get();
    }

    public StringProperty workTelephoneProperty() {
        return workTelephone;
    }

    public void setWorkTelephone(String workTelephone) {
        this.workTelephone.set(workTelephone);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public Apartment getApartment() {
        return apartment.get();
    }

    public ObjectProperty<Apartment> apartmentProperty() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment.set(apartment);
    }

    public LocalDate getContractFrom() {
        return contractFrom.get();
    }

    public final String getContractFromSQL() {
        return contractFrom.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public ObjectProperty<LocalDate> contractFromProperty() {
        return contractFrom;
    }

    public void setContractFrom(LocalDate contractFrom) {
        this.contractFrom.set(contractFrom);
    }

    public LocalDate getContractUntil() {
        return contractUntil.get();
    }

    public final String getContractUntilSQL() {
        return contractUntil.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public ObjectProperty<LocalDate> contractUntilProperty() {
        return contractUntil;
    }

    public void setContractUntil(LocalDate contractUntil) {
        this.contractUntil.set(contractUntil);
    }

    public int getRent() {
        return rent.get();
    }

    public IntegerProperty rentProperty() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent.set(rent);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    @Override
    public String toString() {
        return name.get();
    }
}
