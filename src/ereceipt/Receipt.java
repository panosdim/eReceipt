package ereceipt;

import javafx.beans.property.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Receipt {
    private final ObjectProperty<Lessee> lessee;
    private final ObjectProperty<LocalDate> date;
    private final DoubleProperty amount;
    private final IntegerProperty months;
    private final DoubleProperty sum;
    private final ObjectProperty<Apartment> apartment;
    private final StringProperty year;
    private final IntegerProperty id;
    private final StringProperty address;

    Receipt(Lessee lessee,
            Apartment apartment,
            LocalDate date,
            double amount,
            int months,
            String year,
            int id) {
        this.lessee = new SimpleObjectProperty<>(lessee);
        this.apartment = new SimpleObjectProperty<>(apartment);
        this.date = new SimpleObjectProperty<>(date);
        this.amount = new SimpleDoubleProperty(amount);
        this.months = new SimpleIntegerProperty(months);
        this.sum = new SimpleDoubleProperty(amount * months);
        this.year = new SimpleStringProperty(year);
        this.id = new SimpleIntegerProperty(id);
        this.address = new SimpleStringProperty(apartment.getAddress());
    }

    private String upperCase(String str) {
        String nfdNormalizedString = Normalizer.normalize(str.toUpperCase(), Normalizer.Form.NFD);
        Pattern ptrn = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return ptrn.matcher(nfdNormalizedString).replaceAll("");
    }

    public final String getDate() {
        return date.get().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public final void setDate(LocalDate value) {
        date.set(value);
    }

    public final LocalDate getLocalDate() {
        return date.get();
    }

    public final String getDateSQL() {
        return date.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public final ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public Lessee getLessee() {
        return lessee.get();
    }

    public ObjectProperty<Lessee> lesseeProperty() {
        return lessee;
    }

    public void setLessee(Lessee lessee) {
        this.lessee.set(lessee);
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

    public final Double getAmount() {
        return amount.get();
    }

    public final void setAmount(Double value) {
        amount.set(value);
    }

    public final DoubleProperty amountProperty() {
        return amount;
    }

    public final Integer getMonths() {
        return months.get();
    }

    public final void setMonths(Integer value) {
        months.set(value);
    }

    public final IntegerProperty monthsProperty() {
        return months;
    }

    public final String getSum() {
        return String.format("%.2f", sum.get());
    }

    public final void setSum(Double value) {
        sum.set(value);
    }

    public final DoubleProperty sumProperty() {
        return sum;
    }

    public final String getYear() {
        return year.get();
    }

    public final void setYear(String value) {
        year.set(value);
    }

    public final StringProperty yearProperty() {
        return year;
    }

    public final Integer getId() {
        return id.get();
    }

    public final void setId(Integer value) {
        id.set(value);
    }

    public final IntegerProperty idProperty() {
        return id;
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
}
